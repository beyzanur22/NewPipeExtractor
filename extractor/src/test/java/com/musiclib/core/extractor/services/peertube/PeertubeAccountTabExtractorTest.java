package com.musiclib.core.extractor.services.peertube;

import static com.musiclib.core.extractor.ServiceList.PeerTube;

import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabs;
import com.musiclib.core.extractor.services.DefaultListExtractorTest;

class PeertubeAccountTabExtractorTest {

    static class Videos extends DefaultListExtractorTest<ChannelTabExtractor> {

        @Override
        protected ChannelTabExtractor createExtractor() throws Exception {
            // setting instance might break test when running in parallel
            PeerTube.setInstance(new PeertubeInstance("https://framatube.org", "Framatube"));
            return PeerTube.getChannelTabExtractorFromId("accounts/framasoft", ChannelTabs.VIDEOS);
        }

        @Override public StreamingService expectedService() throws Exception { return PeerTube; }
        @Override public String expectedName() throws Exception { return ChannelTabs.VIDEOS; }
        @Override public String expectedId() throws Exception { return "accounts/framasoft"; }
        @Override public String expectedUrlContains() throws Exception { return "https://framatube.org/accounts/framasoft/videos"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://framatube.org/accounts/framasoft/videos"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.STREAM; }
        @Override public boolean expectedHasMoreItems() { return true; }
    }

    static class Channels extends DefaultListExtractorTest<ChannelTabExtractor> {

        @Override
        protected ChannelTabExtractor createExtractor() throws Exception {
            // setting instance might break test when running in parallel
            PeerTube.setInstance(new PeertubeInstance("https://framatube.org", "Framatube"));
            return PeerTube.getChannelTabExtractorFromId("accounts/framasoft", ChannelTabs.CHANNELS);
        }

        @Override public StreamingService expectedService() throws Exception { return PeerTube; }
        @Override public String expectedName() throws Exception { return ChannelTabs.CHANNELS; }
        @Override public String expectedId() throws Exception { return "accounts/framasoft"; }
        @Override public String expectedUrlContains() throws Exception { return "https://framatube.org/accounts/framasoft/video-channels"; }
        @Override public String expectedOriginalUrlContains() throws Exception { return "https://framatube.org/accounts/framasoft/video-channels"; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.CHANNEL; }
        @Override public boolean expectedHasMoreItems() { return true; }
    }
}
