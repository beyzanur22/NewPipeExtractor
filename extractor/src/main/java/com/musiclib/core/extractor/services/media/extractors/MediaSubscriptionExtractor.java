package com.musiclib.core.extractor.services.media.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.services.media.MediaService;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Nonnull;

import static com.musiclib.core.extractor.subscription.SubscriptionExtractor.ContentSource.INPUT_STREAM;
import com.musiclib.core.extractor.services.media.StringObfuscator;

/**
 * Extract subscriptions from a Google takeout export
 */
public class MediaSubscriptionExtractor extends SubscriptionExtractor {
    private static final String BASE_CHANNEL_URL = StringObfuscator.decode(new int[]{
    0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,
    0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,
    0x3D,0x36,0x3F,0x30,0x30,0x3B,0x32,0x71
});

    public MediaSubscriptionExtractor(final MediaService MediaService) {
        super(MediaService, Collections.singletonList(INPUT_STREAM));
    }

    @Override
    public String getRelatedUrl() {
       return StringObfuscator.decode(new int[]{
    0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x2A,0x3F,0x35,0x3B,
    0x31,0x2B,0x2A,0x70,0x39,0x31,0x31,0x39,0x32,0x3B,0x70,0x3D,
    0x31,0x33,0x71,0x2A,0x3F,0x35,0x3B,0x31,0x2B,0x2A,0x71,0x3D,
    0x2B,0x2D,0x2A,0x31,0x33,0x71,0x27,0x31,0x2B,0x2A,0x2B,0x3C,
    0x3B
});
    }

    @Override
    public List<SubscriptionItem> fromInputStream(@Nonnull final InputStream contentInputStream)
            throws ExtractionException {
        return fromJsonInputStream(contentInputStream);
    }

    @Override
    public List<SubscriptionItem> fromInputStream(@Nonnull final InputStream contentInputStream,
                                                  @Nonnull final String contentType)
            throws ExtractionException {
        switch (contentType) {
            case "json":
            case "application/json":
                return fromJsonInputStream(contentInputStream);
            case "csv":
            case "text/csv":
            case "text/comma-separated-values":
                return fromCsvInputStream(contentInputStream);
            case "zip":
            case "application/zip":
                return fromZipInputStream(contentInputStream);
            default:
                throw new InvalidSourceException("Unsupported content type: " + contentType);
        }
    }

    public List<SubscriptionItem> fromJsonInputStream(@Nonnull final InputStream contentInputStream)
            throws ExtractionException {
        final JsonArray subscriptions;
        try {
            subscriptions = JsonParser.array().from(contentInputStream);
        } catch (final JsonParserException e) {
            throw new InvalidSourceException("Invalid json input stream", e);
        }

        boolean foundInvalidSubscription = false;
        final List<SubscriptionItem> subscriptionItems = new ArrayList<>();
        for (final Object subscriptionObject : subscriptions) {
            if (!(subscriptionObject instanceof JsonObject)) {
                foundInvalidSubscription = true;
                continue;
            }

            final JsonObject subscription = ((JsonObject) subscriptionObject).getObject("snippet");
            final String id = subscription.getObject("resourceId").getString("channelId", "");
            if (id.length() != 24) { // e.g. UCsXVk37bltHxD1rDPwtNM8Q
                foundInvalidSubscription = true;
                continue;
            }

            subscriptionItems.add(new SubscriptionItem(service.getServiceId(),
                    BASE_CHANNEL_URL + id, subscription.getString("title", "")));
        }

        if (foundInvalidSubscription && subscriptionItems.isEmpty()) {
            throw new InvalidSourceException("Found only invalid channel ids");
        }
        return subscriptionItems;
    }

    public List<SubscriptionItem> fromZipInputStream(@Nonnull final InputStream contentInputStream)
            throws ExtractionException {
        try (ZipInputStream zipInputStream = new ZipInputStream(contentInputStream)) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().toLowerCase().endsWith(".csv")) {
                    try {
                        final List<SubscriptionItem> csvItems = fromCsvInputStream(zipInputStream);

                        // Return it only if it has items (it exits early if it's the wrong file
                        // format), otherwise try the next file
                        if (!csvItems.isEmpty()) {
                            return csvItems;
                        }
                    } catch (final ExtractionException e) {
                        // Ignore error and go to next file
                    }
                }
            }
        } catch (final IOException e) {
            throw new InvalidSourceException("Error reading contents of zip file", e);
        }

        throw new InvalidSourceException("Unable to find a valid subscriptions.csv file"
                + " (try extracting and selecting the csv file)");
    }

    public List<SubscriptionItem> fromCsvInputStream(@Nonnull final InputStream contentInputStream)
            throws ExtractionException {
        // Expected format of CSV file:
        // Channel Id,Channel Url,Channel Title
        //UC1JTQBa5QxZCpXrFSkMxmPw,http://www.Media.com/channel/UC1JTQBa5QxZCpXrFSkMxmPw,Raycevick
        //UCFl7yKfcRcFmIUbKeCA-SJQ,http://www.Media.com/channel/UCFl7yKfcRcFmIUbKeCA-SJQ,Joji
        //
        // Notes:
        //      It's always 3 columns
        //      The first line is always a header
        //      Header names are different based on the locale
        //      Fortunately the data is always the same order no matter what locale
        try (var reader = new BufferedReader(new InputStreamReader(contentInputStream))) {
            return reader.lines()
                    .skip(1) // ignore header and skip first line
                    .map(line -> line.split(","))
                    .filter(values -> values.length >= 3)
                    .map(values -> {
                        // Channel URL from second entry
                        final String channelUrl = values[1].replace("http://", "https://");
                        return channelUrl.startsWith(BASE_CHANNEL_URL)
                            ? new SubscriptionItem(
                            service.getServiceId(),
                            channelUrl,
                            values[2]) // Channel title from third entry
                            : null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toUnmodifiableList());
        } catch (final UncheckedIOException | IOException e) {
            throw new InvalidSourceException("Error reading CSV file", e);
        }
    }
}
