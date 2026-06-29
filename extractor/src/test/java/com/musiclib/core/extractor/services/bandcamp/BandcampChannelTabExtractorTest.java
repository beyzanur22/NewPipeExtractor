package com.musiclib.core.extractor.services.bandcamp;

import static com.musiclib.core.extractor.ServiceList.Bandcamp;

import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabs;
import com.musiclib.core.extractor.services.DefaultListExtractorTest;

class BandcampChannelTabExtractorTest {

    static class Tracks extends DefaultListExtractorTest<ChannelTabExtractor> {

        @Override
        protected ChannelTabExtractor createExtractor() throws Exception {
            return Bandcamp.getChannelTabExtractorFromId("2464198920", ChannelTabs.TRACKS);
        }

        @Override public StreamingService expectedService() throws Exception { return Bandcamp; }
        @Override public String expectedName() throws Exception { return ChannelTabs.TRACKS; }
        @Override public String expectedId() throws Exception { return "2464198920"; }
        @Override public String expectedUrlContains() throws Exception { return "https://wintergatan.bandcamp.com/track"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://wintergatan.bandcamp.com/track"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.STREAM; }
        @Override public boolean expectedHasMoreItems() { return false; }
    }

    static class Albums extends DefaultListExtractorTest<ChannelTabExtractor> {

        @Override
        protected ChannelTabExtractor createExtractor() throws Exception {
            return Bandcamp.getChannelTabExtractorFromId("2450875064", ChannelTabs.ALBUMS);
        }

        @Override public StreamingService expectedService() throws Exception { return Bandcamp; }
        @Override public String expectedName() throws Exception { return ChannelTabs.ALBUMS; }
        @Override public String expectedId() throws Exception { return "2450875064"; }
        @Override public String expectedUrlContains() throws Exception { return "https://toupie.bandcamp.com/album"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://toupie.bandcamp.com/album"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.PLAYLIST; }
        @Override public boolean expectedHasMoreItems() { return false; }
    }
}