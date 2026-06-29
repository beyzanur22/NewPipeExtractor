package com.musiclib.core.extractor.services.youtube.extractors.kiosk;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

public class YoutubeTrendingGamingVideosExtractor extends YoutubeDesktopBaseKioskExtractor {

    public YoutubeTrendingGamingVideosExtractor(final StreamingService streamingService,
                                                final ListLinkHandler linkHandler,
                                                final String kioskId) {
        super(streamingService, linkHandler, kioskId, "UCOpNcN46UbXVtpKMrmU4Abg",
                "Egh0cmVuZGluZw%3D%3D");
    }
}
