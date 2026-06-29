// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package com.musiclib.core.extractor.services.bandcamp.extractors;

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
import com.musiclib.core.extractor.stream.StreamInfoItem;
import com.musiclib.core.extractor.stream.StreamInfoItemsCollector;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampExtractorHelper.BASE_API_URL;

public class BandcampRadioExtractor extends KioskExtractor<StreamInfoItem> {

    public static final String KIOSK_RADIO = "Radio";
    public static final String RADIO_API_URL = BASE_API_URL + "/bcweekly/3/list";

    private JsonObject json = null;

    public BandcampRadioExtractor(final StreamingService streamingService,
                                  final ListLinkHandler linkHandler,
                                  final String kioskId) {
        super(streamingService, linkHandler, kioskId);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        try {
            json = JsonParser.object().from(
                    getDownloader().get(RADIO_API_URL).responseBody());
        } catch (final JsonParserException e) {
            throw new ExtractionException("Could not parse Bandcamp Radio API response", e);
        }
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return KIOSK_RADIO;
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() {
        final StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());

        final JsonArray radioShows = json.getArray("results");

        for (int i = 0; i < radioShows.size(); i++) {
            final JsonObject radioShow = radioShows.getObject(i);
            collector.commit(new BandcampRadioInfoItemExtractor(radioShow));
        }

        return new InfoItemsPage<>(collector, null);
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page) {
        return null;
    }
}
