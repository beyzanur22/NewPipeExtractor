package com.musiclib.core.extractor.services.media.extractors.kiosk;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

public class MediaTrendingPodcastsEpisodesExtractor extends MediaDesktopBaseKioskExtractor {

    public MediaTrendingPodcastsEpisodesExtractor(final StreamingService streamingService,
                                                    final ListLinkHandler linkHandler,
                                                    final String kioskId) {
        super(streamingService, linkHandler, kioskId, "FEpodcasts_destination", "qgcCCAM%3D");
    }
}
