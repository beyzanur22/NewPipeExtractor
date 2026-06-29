package com.musiclib.core.extractor.services.soundcloud.search;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ServiceList.SoundCloud;
import static com.musiclib.core.extractor.services.DefaultTests.assertNoDuplicatedItems;
import static com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudSearchQueryHandlerFactory.PLAYLISTS;
import static com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudSearchQueryHandlerFactory.TRACKS;
import static com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudSearchQueryHandlerFactory.USERS;
import static java.util.Collections.singletonList;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.ListExtractor.InfoItemsPage;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.ChannelInfoItem;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.DefaultSearchExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.utils.Utils;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

public class SoundcloudSearchExtractorTest {

    public static class All extends DefaultSearchExtractorTest {
        private static final String QUERY = "lill uzi vert";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor(QUERY);
        }

        // @formatter:off
        @Override public StreamingService expectedService() { return SoundCloud; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "soundcloud.com/search?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedOriginalUrlContains() { return "soundcloud.com/search?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
        // @formatter:on
    }

    public static class Tracks extends DefaultSearchExtractorTest {
        private static final String QUERY = "lill uzi vert";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor(QUERY, singletonList(TRACKS), "");
        }

        // @formatter:off
        @Override public StreamingService expectedService() { return SoundCloud; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "soundcloud.com/search/tracks?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedOriginalUrlContains() { return "soundcloud.com/search/tracks?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.STREAM; }
        // @formatter:on
    }

    public static class Users extends DefaultSearchExtractorTest {
        private static final String QUERY = "lill uzi vert";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor(QUERY, singletonList(USERS), "");
        }

        // @formatter:off
        @Override public StreamingService expectedService() { return SoundCloud; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "soundcloud.com/search/users?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedOriginalUrlContains() { return "soundcloud.com/search/users?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.CHANNEL; }
        // @formatter:on
    }

    public static class Playlists extends DefaultSearchExtractorTest {
        private static final String QUERY = "lill uzi vert";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor(QUERY, singletonList(PLAYLISTS), "");
        }

        // @formatter:off
        @Override public StreamingService expectedService() { return SoundCloud; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "soundcloud.com/search/playlists?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedOriginalUrlContains() { return "soundcloud.com/search/playlists?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.PLAYLIST; }
        // @formatter:on
    }

    public static class PagingTest extends DefaultSimpleExtractorTest<SearchExtractor> {

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor("cirque du soleil", singletonList(TRACKS), "");
        }

        @Test
        public void duplicatedItemsCheck() throws Exception {
            final InfoItemsPage<InfoItem> page1 = extractor().getInitialPage();
            final InfoItemsPage<InfoItem> page2 = extractor().getPage(page1.getNextPage());

            assertNoDuplicatedItems(SoundCloud, page1, page2);
        }
    }

    public static class UserVerified extends DefaultSearchExtractorTest {
        private static final String QUERY = "David Guetta";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor(QUERY, singletonList(USERS), "");
        }

        @Override public StreamingService expectedService() { return SoundCloud; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "soundcloud.com/search/users?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedOriginalUrlContains() { return "soundcloud.com/search/users?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }

        @Override public InfoItem.InfoType expectedInfoItemType() { return InfoItem.InfoType.CHANNEL; }

        @Test
        void testIsVerified() throws IOException, ExtractionException {
            final List<InfoItem> items = extractor().getInitialPage().getItems();
            boolean verified = false;
            for (final InfoItem item : items) {
                if (item.getUrl().equals("https://soundcloud.com/davidguetta")) {
                    verified = ((ChannelInfoItem) item).isVerified();
                    break;
                }
            }
            assertTrue(verified);
        }
    }

    public static class NoNextPage extends DefaultSearchExtractorTest {

        private static final String QUERY = "wpghüä";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return SoundCloud.getSearchExtractor(QUERY);
        }

        @Override public boolean expectedHasMoreItems() { return false; }
        @Override public StreamingService expectedService() throws Exception { return SoundCloud; }
        @Override public String expectedName() throws Exception { return QUERY; }
        @Override public String expectedId() throws Exception { return QUERY; }
        @Override public String expectedUrlContains() { return "soundcloud.com/search?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedOriginalUrlContains() { return "soundcloud.com/search?q=" + Utils.encodeUrlUtf8(QUERY); }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
    }
}
