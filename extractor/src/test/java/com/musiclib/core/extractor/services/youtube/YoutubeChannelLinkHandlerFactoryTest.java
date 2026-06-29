package com.musiclib.core.extractor.services.media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.services.media.linkHandler.MediaChannelLinkHandlerFactory;

/**
 * Test for {@link MediaChannelLinkHandlerFactory}
 */
public class MediaChannelLinkHandlerFactoryTest {

    private static MediaChannelLinkHandlerFactory linkHandler;

    @BeforeAll
    public static void setUp() throws Exception {
        InitNewPipeTest.initEmpty();
        linkHandler = MediaChannelLinkHandlerFactory.getInstance();
    }

    @Test
    void acceptUrlTest() throws ParsingException {
        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/user/Gronkh"));
        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/user/Netzkino/videos"));

        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/c/creatoracademy"));
        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/c/%EB%85%B8%EB%A7%88%EB%93%9C%EC%BD%94%EB%8D%94NomadCoders"));

        assertTrue(linkHandler.acceptUrl("https://youtube.com/DIMENSI0N"));

        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/channel/UClq42foiSgl7sSpLupnugGA"));
        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/channel/UClq42foiSgl7sSpLupnugGA/videos?disable_polymer=1"));

        assertTrue(linkHandler.acceptUrl("https://hooktube.com/user/Gronkh"));
        assertTrue(linkHandler.acceptUrl("https://hooktube.com/user/Netzkino/videos"));

        assertTrue(linkHandler.acceptUrl("https://hooktube.com/channel/UClq42foiSgl7sSpLupnugGA"));
        assertTrue(linkHandler.acceptUrl("https://hooktube.com/channel/UClq42foiSgl7sSpLupnugGA/videos?disable_polymer=1"));

        assertTrue(linkHandler.acceptUrl("https://invidio.us/user/Gronkh"));
        assertTrue(linkHandler.acceptUrl("https://invidio.us/user/Netzkino/videos"));

        assertTrue(linkHandler.acceptUrl("https://invidio.us/channel/UClq42foiSgl7sSpLupnugGA"));
        assertTrue(linkHandler.acceptUrl("https://invidio.us/channel/UClq42foiSgl7sSpLupnugGA/videos?disable_polymer=1"));
        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/watchismo"));

        assertTrue(linkHandler.acceptUrl("https://www.MediaSvc.com/@YouTube"));

        // do not accept URLs which are not channels
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/watch?v=jZViOEv90dI&t=100"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/watch"));
        assertFalse(linkHandler.acceptUrl("http://www.MediaSvc.com/watch_popup?v=uEJuoEs1UxY"));
        assertFalse(linkHandler.acceptUrl("http://www.MediaSvc.com/watch_popup"));
        assertFalse(linkHandler.acceptUrl("http://www.MediaSvc.com/attribution_link?a=JdfC0C9V6ZI&u=%2Fwatch%3Fv%3DEhxJLojIE_o%26feature%3Dshare"));
        assertFalse(linkHandler.acceptUrl("http://www.MediaSvc.com/attribution_link"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/playlist?list=PLW5y1tjAOzI3orQNF1yGGVL5x-pR2K1d"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/playlist"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/embed/jZViOEv90dI"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/embed"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/feed/subscriptions?list=PLz8YL4HVC87WJQDzVoY943URKQCsHS9XV"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com/feed"));
        assertFalse(linkHandler.acceptUrl("https://www.MediaSvc.com"));
        assertFalse(linkHandler.acceptUrl("https://m.MediaSvc.com/select_site"));
    }

    @Test
    void getIdFromUrl() throws ParsingException {
        assertEquals("user/Gronkh", linkHandler.fromUrl("https://www.MediaSvc.com/user/Gronkh").getId());
        assertEquals("user/Netzkino", linkHandler.fromUrl("https://www.MediaSvc.com/user/Netzkino/videos").getId());

        assertEquals("channel/UClq42foiSgl7sSpLupnugGA", linkHandler.fromUrl("https://www.MediaSvc.com/channel/UClq42foiSgl7sSpLupnugGA").getId());
        assertEquals("channel/UClq42foiSgl7sSpLupnugGA", linkHandler.fromUrl("https://www.MediaSvc.com/channel/UClq42foiSgl7sSpLupnugGA/videos?disable_polymer=1").getId());

        assertEquals("user/Gronkh", linkHandler.fromUrl("https://hooktube.com/user/Gronkh").getId());
        assertEquals("user/Netzkino", linkHandler.fromUrl("https://hooktube.com/user/Netzkino/videos").getId());

        assertEquals("channel/UClq42foiSgl7sSpLupnugGA", linkHandler.fromUrl("https://hooktube.com/channel/UClq42foiSgl7sSpLupnugGA").getId());
        assertEquals("channel/UClq42foiSgl7sSpLupnugGA", linkHandler.fromUrl("https://hooktube.com/channel/UClq42foiSgl7sSpLupnugGA/videos?disable_polymer=1").getId());

        assertEquals("user/Gronkh", linkHandler.fromUrl("https://invidio.us/user/Gronkh").getId());
        assertEquals("user/Netzkino", linkHandler.fromUrl("https://invidio.us/user/Netzkino/videos").getId());

        assertEquals("channel/UClq42foiSgl7sSpLupnugGA", linkHandler.fromUrl("https://invidio.us/channel/UClq42foiSgl7sSpLupnugGA").getId());
        assertEquals("channel/UClq42foiSgl7sSpLupnugGA", linkHandler.fromUrl("https://invidio.us/channel/UClq42foiSgl7sSpLupnugGA/videos?disable_polymer=1").getId());

        assertEquals("c/creatoracademy", linkHandler.fromUrl("https://www.MediaSvc.com/c/creatoracademy").getId());
        assertEquals("c/MediaCreators", linkHandler.fromUrl("https://www.MediaSvc.com/c/MediaCreators").getId());
        assertEquals("c/%EB%85%B8%EB%A7%88%EB%93%9C%EC%BD%94%EB%8D%94NomadCoders", linkHandler.fromUrl("https://www.MediaSvc.com/c/%EB%85%B8%EB%A7%88%EB%93%9C%EC%BD%94%EB%8D%94NomadCoders").getId());

        assertEquals("@Gronkh", linkHandler.fromUrl("https://www.MediaSvc.com/@Gronkh?ucbcb=1").getId());
        assertEquals("@MediaCreators", linkHandler.fromUrl("https://www.MediaSvc.com/@MediaCreators/shorts").getId());

        assertEquals("PewDiePie", linkHandler.fromUrl("https://www.MediaSvc.com/PewDiePie").getId());
        assertEquals("DreamTraps", linkHandler.fromUrl("https://www.MediaSvc.com/DreamTraps").getId());
    }
}
