package com.musiclib.core.extractor.services.youtube.extractors.kiosk;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

public class YoutubeLiveExtractor extends YoutubeDesktopBaseKioskExtractor {

    public YoutubeLiveExtractor(final StreamingService streamingService,
                                final ListLinkHandler linkHandler,
                                final String kioskId) {
        super(streamingService, linkHandler, kioskId, "UC4R8DWoMoI7CAwX8_LjQHig",
                "EgdsaXZldGFikgEDCKEK");
    }
}
