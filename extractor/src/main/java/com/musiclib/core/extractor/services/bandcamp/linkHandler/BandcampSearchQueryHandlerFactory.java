// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package com.musiclib.core.extractor.services.bandcamp.linkHandler;

import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampExtractorHelper.BASE_URL;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import java.util.List;

public final class BandcampSearchQueryHandlerFactory extends SearchQueryHandlerFactory {

    private static final BandcampSearchQueryHandlerFactory INSTANCE
            = new BandcampSearchQueryHandlerFactory();

    private BandcampSearchQueryHandlerFactory() {
    }

    public static BandcampSearchQueryHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String query,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return BASE_URL + "/search?q=" + Utils.encodeUrlUtf8(query) + "&page=1";
    }
}
