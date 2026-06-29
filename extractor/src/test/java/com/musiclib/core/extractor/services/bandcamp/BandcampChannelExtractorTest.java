// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package com.musiclib.core.extractor.services.bandcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ExtractorAsserts.assertTabsContain;
import static com.musiclib.core.extractor.ServiceList.Bandcamp;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.channel.ChannelExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabs;
import com.musiclib.core.extractor.services.BaseChannelExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;

public class BandcampChannelExtractorTest extends DefaultSimpleExtractorTest<ChannelExtractor>
    implements BaseChannelExtractorTest {

    @Override
    protected ChannelExtractor createExtractor() throws Exception {
        return Bandcamp.getChannelExtractor("https://toupie.bandcamp.com/releases");
    }

    @Test
    @Override
    public void testDescription() throws Exception {
        assertEquals("making music:)", extractor().getDescription());
    }

    @Test
    @Override
    public void testAvatars() throws Exception {
        BandcampTestUtils.testImages(extractor().getAvatars());
    }

    @Test
    @Override
    public void testBanners() throws Exception {
        BandcampTestUtils.testImages(extractor().getBanners());
    }

    @Test
    @Override
    public void testFeedUrl() throws Exception {
        assertNull(extractor().getFeedUrl());
    }

    @Test
    @Override
    public void testSubscriberCount() throws Exception {
        assertEquals(-1, extractor().getSubscriberCount());
    }

    @Test
    @Override
    public void testVerified() throws Exception {
        assertFalse(extractor().isVerified());
    }

    @Test
    @Override
    public void testServiceId() {
        assertEquals(Bandcamp.getServiceId(), extractor().getServiceId());
    }

    @Test
    @Override
    public void testName() throws Exception {
        assertEquals("toupie", extractor().getName());
    }

    @Test
    @Override
    public void testId() throws Exception {
        assertEquals("2450875064", extractor().getId());
    }

    @Test
    @Override
    public void testUrl() throws Exception {
        assertEquals("https://toupie.bandcamp.com", extractor().getUrl());
    }

    @Test
    @Override
    public void testOriginalUrl() throws Exception {
        assertEquals("https://toupie.bandcamp.com", extractor().getUrl());
    }

    @Test
    @Override
    public void testTabs() throws Exception {
        assertTabsContain(extractor().getTabs(), ChannelTabs.ALBUMS);
    }

    @Test
    @Override
    public void testTags() throws Exception {
        assertTrue(extractor().getTags().isEmpty());
    }
}
