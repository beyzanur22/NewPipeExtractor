package com.musiclib.core.extractor.services.media_ccc.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.kiosk.KioskExtractor;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.localization.DateWrapper;
import com.musiclib.core.extractor.stream.StreamInfoItem;
import com.musiclib.core.extractor.stream.StreamInfoItemsCollector;

import java.io.IOException;
import java.util.Comparator;

import javax.annotation.Nonnull;

public class MediaCCCRecentKiosk extends KioskExtractor<StreamInfoItem> {

    public static final String KIOSK_ID = "recent";

    private JsonObject doc;

    public MediaCCCRecentKiosk(final StreamingService streamingService,
                               final ListLinkHandler linkHandler,
                               final String kioskId) {
        super(streamingService, linkHandler, kioskId);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        final String site = downloader.get("https://api.media.ccc.de/public/events/recent",
                getExtractorLocalization()).responseBody();
        try {
            doc = JsonParser.object().from(site);
        } catch (final JsonParserException jpe) {
            throw new ExtractionException("Could not parse json.", jpe);
        }
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        final JsonArray events = doc.getArray("events");

        // Streams in the recent kiosk are not ordered by the release date.
        // Sort them to have the latest stream at the beginning of the list.
        final var comparator = Comparator.comparing(StreamInfoItem::getUploadDate,
                        Comparator.nullsLast(Comparator.comparing(DateWrapper::getInstant)))
                .reversed();
        final var collector = new StreamInfoItemsCollector(getServiceId(), comparator);

        events.streamAsJsonObjects()
                .map(MediaCCCRecentKioskExtractor::new)
                // #813 / voc/voctoweb#609 -> returns faulty data -> filter it out
                .filter(extractor -> extractor.getDuration() > 0)
                .forEach(collector::commit);

        return new InfoItemsPage<>(collector, null);
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page)
            throws IOException, ExtractionException {
        return InfoItemsPage.emptyPage();
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return KIOSK_ID;
    }
}
