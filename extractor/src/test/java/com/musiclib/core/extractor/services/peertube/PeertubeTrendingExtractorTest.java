package com.musiclib.core.extractor.services.peertube;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.musiclib.core.extractor.ServiceList.PeerTube;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestRelatedItems;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.services.BaseListExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeTrendingExtractor;

public class PeertubeTrendingExtractorTest {

    public static class Trending extends DefaultSimpleExtractorTest<PeertubeTrendingExtractor>
        implements BaseListExtractorTest {

        @Override
        protected PeertubeTrendingExtractor createExtractor() throws Exception {
            // setting instance might break test when running in parallel
            PeerTube.setInstance(new PeertubeInstance("https://framatube.org", "Framatube"));
            return (PeertubeTrendingExtractor) PeerTube.getKioskList()
                .getExtractorById("Trending", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(PeerTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            assertEquals("Trending", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("Trending", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws ParsingException {
            assertEquals("https://framatube.org/api/v1/videos?sort=-trending", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("https://framatube.org/api/v1/videos?sort=-trending", extractor().getOriginalUrl());
        }

        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Test
        public void testMoreRelatedItems() throws Exception {
            defaultTestMoreItems(extractor());
        }
    }
}
