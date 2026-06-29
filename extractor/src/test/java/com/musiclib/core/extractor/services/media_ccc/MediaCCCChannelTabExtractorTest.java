package com.musiclib.core.extractor.services.media_ccc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.musiclib.core.extractor.ServiceList.MediaCCC;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabs;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;

/**
 * Test that it is possible to create and use a channel tab extractor ({@link
 * com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCChannelTabExtractor}) without
 * passing through the conference extractor
 */
public class MediaCCCChannelTabExtractorTest {
    public static class CCCamp2023 extends DefaultSimpleExtractorTest<ChannelTabExtractor> {
        @Override
        protected ChannelTabExtractor createExtractor() throws Exception {
            return MediaCCC.getChannelTabExtractorFromId("camp2023", ChannelTabs.VIDEOS);
        }

        @Test
        void testName() {
            assertEquals(ChannelTabs.VIDEOS, extractor().getName());
        }

        @Test
        void testGetUrl() throws Exception {
            assertEquals("https://media.ccc.de/c/camp2023", extractor().getUrl());
        }

        @Test
        void testGetOriginalUrl() throws Exception {
            assertEquals("https://media.ccc.de/c/camp2023", extractor().getOriginalUrl());
        }

        @Test
        void testGetInitalPage() throws Exception {
            assertEquals(177, extractor().getInitialPage().getItems().size());
        }
    }
}
