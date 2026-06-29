package com.musiclib.core.extractor.services.media;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.ExtractorAsserts;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.exceptions.ParsingException;

class MediaJavaScriptExtractorTest {

    void initNewPipe(final String useCase) {
        InitNewPipeTest.initNewPipe(this.getClass(), useCase);
        MediaTestsUtils.ensureStateless();
    }

    @Test
    void testExtractJavaScriptUrlIframe() throws ParsingException {
        initNewPipe("urlWithIframeResource");

        assertTrue(MediaJavaScriptExtractor.extractJavaScriptUrlWithIframeResource()
                .endsWith("base.js"));
    }

    @Test
    void testExtractJavaScriptUrlEmbed() throws ParsingException {
        initNewPipe("embedWatchPage");

        assertTrue(MediaJavaScriptExtractor.extractJavaScriptUrlWithEmbedWatchPage("d4IGg5dqeO8")
                .endsWith("base.js"));
    }

    @Test
    void testExtractJavaScript__success() throws ParsingException {
        initNewPipe("playerCode");

        final String playerJsCode = MediaJavaScriptExtractor.extractJavaScriptPlayerCode("d4IGg5dqeO8");
        assertPlayerJsCode(playerJsCode);
    }

    @Test
    void testExtractJavaScript__invalidVideoId__success() throws ParsingException {
        initNewPipe("playerCodeInvalidVideoId");

        String playerJsCode = MediaJavaScriptExtractor.extractJavaScriptPlayerCode("not_a_video_id");
        assertPlayerJsCode(playerJsCode);

        playerJsCode = MediaJavaScriptExtractor.extractJavaScriptPlayerCode("11-chars123");
        assertPlayerJsCode(playerJsCode);

    }

    private void assertPlayerJsCode(final String playerJsCode) {
        ExtractorAsserts.assertContains(" Copyright The Closure Library Authors.\n"
                + " SPDX-License-Identifier: Apache-2.0", playerJsCode);
        ExtractorAsserts.assertContains("var _yt_player", playerJsCode);
    }
}