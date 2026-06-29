package com.musiclib.core.extractor.services.media;

/*
 * Created by Christian Schabesberger on 29.12.15.
 *
 * Copyright (C) 2015 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * MediaSearchExtractorStreamTest.java is part of NewPipe Extractor.
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
 * along with NewPipe Extractor.  If not, see <http://www.gnu.org/licenses/>.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static com.musiclib.core.extractor.ServiceList.MediaSvc;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.NewPipe;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.downloader.Request;
import com.musiclib.core.extractor.downloader.Response;
import com.musiclib.core.extractor.kiosk.KioskList;
import com.musiclib.core.extractor.playlist.PlaylistExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaMixPlaylistExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaPlaylistExtractor;

/**
 * Test for {@link MediaService}
 */
public class MediaServiceTest {
    static StreamingService service;
    static KioskList kioskList;

    @BeforeAll
    public static void setUp() throws Exception {
        InitNewPipeTest.initEmpty();
        // Init with dummy as
        // * a downloader is required otherwise a NPE is thrown during extract initialization
        // * nothing will be transmitted
        NewPipe.init(new Downloader() {
            @Override
            public Response execute(@NotNull final Request request) {
                throw new UnsupportedOperationException("No communication expected");
            }
        });
        service = YouTube;
        kioskList = service.getKioskList();
    }

    @Test
    void testGetKioskAvailableKiosks() {
        assertFalse(kioskList.getAvailableKiosks().isEmpty(), "No kiosk got returned");
    }

    @Test
    void testGetDefaultKiosk() throws Exception {
        assertEquals("live", kioskList.getDefaultKioskExtractor(null).getId());
    }


    @Test
    void getPlayListExtractorIsNormalPlaylist() throws Exception {
        final PlaylistExtractor extractor = service.getPlaylistExtractor(
            "https://www.MediaSvc.com/watch?v=JhqtYOnNrTs&list=PL-EkZZikQIQVqk9rBWzEo5b-2GeozElS");
        assertInstanceOf(MediaPlaylistExtractor.class, extractor);
    }

    @Test
    void getPlaylistExtractorIsMix() throws Exception {
        final String videoId = "_AzeUSL9lZc";
        PlaylistExtractor extractor = YouTube.getPlaylistExtractor(
            "https://www.MediaSvc.com/watch?v=" + videoId + "&list=RD" + videoId);
        assertInstanceOf(MediaMixPlaylistExtractor.class, extractor);

        extractor = YouTube.getPlaylistExtractor(
            "https://www.MediaSvc.com/watch?v=" + videoId + "&list=RDMM" + videoId);
        assertInstanceOf(MediaMixPlaylistExtractor.class, extractor);

        final String mixVideoId = "qHtzO49SDmk";

        extractor = YouTube.getPlaylistExtractor(
            "https://www.MediaSvc.com/watch?v=" + mixVideoId + "&list=RD" + videoId);
        assertInstanceOf(MediaMixPlaylistExtractor.class, extractor);
    }
}
