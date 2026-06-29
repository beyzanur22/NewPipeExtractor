package com.musiclib.core.extractor.services.youtube.extractors.kiosk;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

public class YoutubeTrendingPodcastsEpisodesExtractor extends YoutubeDesktopBaseKioskExtractor {

    public YoutubeTrendingPodcastsEpisodesExtractor(final StreamingService streamingService,
                                                    final ListLinkHandler linkHandler,
                                                    final String kioskId) {
        super(streamingService, linkHandler, kioskId, "FEpodcasts_destination", "qgcCCAM%3D");
    }
}
