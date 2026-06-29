package com.musiclib.core.extractor.services.youtube;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ServiceList.YouTube;
import static com.musiclib.core.extractor.services.DefaultTests.assertNoMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestRelatedItems;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.exceptions.ContentNotAvailableException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.services.BaseListExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.youtube.extractors.YoutubeFeedExtractor;

public class YoutubeFeedExtractorTest {

    public static class Kurzgesagt extends DefaultSimpleExtractorTest<YoutubeFeedExtractor>
        implements BaseListExtractorTest, InitYoutubeTest {

        @Override
        protected YoutubeFeedExtractor createExtractor() throws Exception {
            return (YoutubeFeedExtractor) YouTube
                .getFeedExtractor("https://www.youtube.com/user/Kurzgesagt");
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() {
            assertTrue(extractor().getName().startsWith("Kurzgesagt"));
        }

        @Override
        @Test
        public void testId() {
            assertEquals("UCsXVk37bltHxD1rDPwtNM8Q", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() {
            assertEquals("https://www.youtube.com/channel/UCsXVk37bltHxD1rDPwtNM8Q", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("https://www.youtube.com/user/Kurzgesagt", extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor());
        }
    }

    public static class NotAvailable implements InitYoutubeTest {

        @Test
        void AccountTerminatedFetch() throws Exception {
            final YoutubeFeedExtractor extractor = (YoutubeFeedExtractor) YouTube
                    .getFeedExtractor("https://www.youtube.com/channel/UCTGjY2I-ZUGnwVoWAGRd7XQ");
            assertThrows(ContentNotAvailableException.class, extractor::fetchPage);
        }
    }
}