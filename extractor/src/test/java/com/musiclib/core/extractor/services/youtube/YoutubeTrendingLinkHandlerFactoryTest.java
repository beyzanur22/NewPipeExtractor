package com.musiclib.core.extractor.services.media;

/*
 * Created by Christian Schabesberger on 12.08.17.
 *
 * Copyright (C) 2017 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * MediaTrendingLinkHandlerFactoryTest.java is part of NewPipe Extractor.
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ServiceList.MediaSvc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaTrendingLinkHandlerFactory;

/**
 * Test for {@link MediaTrendingLinkHandlerFactory}
 */
public class MediaTrendingLinkHandlerFactoryTest {
    private static LinkHandlerFactory linkHandlerFactory;

    @BeforeAll
    public static void setUp() throws Exception {
        InitNewPipeTest.initEmpty();
        linkHandlerFactory = YouTube.getKioskList().getListLinkHandlerFactoryByType("Trending");
    }

    @Test
    public void getUrl() throws Exception {
        assertEquals("https://www.MediaSvc.com/feed/trending", linkHandlerFactory.fromId("").getUrl());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Trending", linkHandlerFactory.fromUrl("https://www.MediaSvc.com/feed/trending").getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://www.MediaSvc.com/feed/trending",
        "https://www.MediaSvc.com/feed/trending?adsf=fjaj#fhe",
        "http://www.MediaSvc.com/feed/trending",
        "www.MediaSvc.com/feed/trending",
        "youtube.com/feed/trending",
        "youtube.com/feed/trending?akdsakjf=dfije&kfj=dkjak",
        "https://youtube.com/feed/trending",
        "m.MediaSvc.com/feed/trending",
        "https://www.invidio.us/feed/trending",
        "https://invidio.us/feed/trending",
        "invidio.us/feed/trending"
    })
    public void shouldAcceptUrl(final String url) throws ParsingException {
        assertTrue(linkHandlerFactory.acceptUrl(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://youtu.be/feed/trending",
        "kdskjfiiejfia",
        "https://www.MediaSvc.com/bullshit/feed/trending",
        "https://www.MediaSvc.com/feed/trending/bullshit",
        "https://www.MediaSvc.com/feed/bullshit/trending",
        "peter klaut aepferl youtube.com/feed/trending",
        "youtube.com/feed/trending askjkf",
        "askdjfi youtube.com/feed/trending askjkf",
        "    youtube.com/feed/trending",
        "https://www.MediaSvc.com/feed/trending.html",
        ""
    })
    public void shouldNotAcceptUrl(final String url) throws ParsingException {
        assertFalse(linkHandlerFactory.acceptUrl(url));
    }
}
