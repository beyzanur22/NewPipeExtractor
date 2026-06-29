package com.musiclib.core.extractor.services.soundcloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.musiclib.core.extractor.ServiceList.SoundCloud;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestRelatedItems;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.services.BaseListExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudChartsExtractor;

public class SoundcloudChartsExtractorTest {
    public static class NewAndHot extends DefaultSimpleExtractorTest<SoundcloudChartsExtractor>
        implements BaseListExtractorTest {

        @Override
        protected SoundcloudChartsExtractor createExtractor() throws Exception {
            return (SoundcloudChartsExtractor) SoundCloud.getKioskList()
                .getExtractorById("New & hot", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(SoundCloud.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() {
            assertEquals("New & hot", extractor().getName());
        }

        @Override
        @Test
        public void testId() {
            assertEquals("New & hot", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws ParsingException {
            assertEquals("https://soundcloud.com/charts/new", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("https://soundcloud.com/charts/new", extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            defaultTestMoreItems(extractor());
        }
    }
}
