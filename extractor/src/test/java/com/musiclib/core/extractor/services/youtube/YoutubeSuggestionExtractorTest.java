/*
 * Created by Christian Schabesberger on 18.11.16.
 *
 * Copyright (C) 2016 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * MediaSuggestionExtractorTest.java is part of NewPipe Extractor.
 *
 * NewPipe Extractor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe Extractor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe Extractor.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.musiclib.core.extractor.services.media;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static com.musiclib.core.extractor.ServiceList.YouTube;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.musiclib.core.downloader.DownloaderFactory;
import com.musiclib.core.extractor.NewPipe;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.localization.Localization;
import com.musiclib.core.extractor.services.media.extractors.MediaSuggestionExtractor;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.io.IOException;

/**
 * Test for {@link MediaSuggestionExtractor}
 */
class MediaSuggestionExtractorTest {

    private static SuggestionExtractor suggestionExtractor;

    @BeforeAll
    public static void setUp() throws Exception {
        MediaTestsUtils.ensureStateless();
        NewPipe.init(DownloaderFactory.getDownloader(MediaSuggestionExtractorTest.class), new Localization("de", "DE"));
        suggestionExtractor = YouTube.getSuggestionExtractor();
    }

    @Test
    void testIfSuggestions() throws IOException, ExtractionException {
        assertFalse(suggestionExtractor.suggestionList("hello").isEmpty());
    }
}
