package com.musiclib.core.extractor.services.youtube.extractors.kiosk;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

import javax.annotation.Nonnull;

public class YoutubeTrendingMoviesAndShowsTrailersExtractor
        extends YoutubeChartsBaseKioskExtractor {

    public YoutubeTrendingMoviesAndShowsTrailersExtractor(final StreamingService streamingService,
                                                          final ListLinkHandler linkHandler,
                                                          final String kioskId) {
        super(streamingService, linkHandler, kioskId, "TRENDING_MOVIES");
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        // This is the official YouTube Charts name, even if shows' trailers are returned too
        return "Trending Movie Trailers";
    }
}
