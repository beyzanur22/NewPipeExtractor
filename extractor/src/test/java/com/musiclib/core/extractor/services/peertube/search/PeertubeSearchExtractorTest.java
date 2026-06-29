package com.musiclib.core.extractor.services.peertube.search;

import static com.musiclib.core.extractor.ServiceList.PeerTube;
import static com.musiclib.core.extractor.services.DefaultTests.assertNoDuplicatedItems;
import static com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeSearchQueryHandlerFactory.VIDEOS;
import static java.util.Collections.singletonList;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.ListExtractor.InfoItemsPage;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.DefaultSearchExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.peertube.PeertubeInstance;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeSearchQueryHandlerFactory;

import javax.annotation.Nullable;

public class PeertubeSearchExtractorTest {

    public static class All extends DefaultSearchExtractorTest {
        private static final String QUERY = "fsf";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            // setting instance might break test when running in parallel
            PeerTube.setInstance(new PeertubeInstance("https://framatube.org", "Framatube"));
            return PeerTube.getSearchExtractor(QUERY);
        }

        @Override public StreamingService expectedService() { return PeerTube; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "/search/videos?search=" + QUERY; }
        @Override public String expectedOriginalUrlContains() { return "/search/videos?search=" + QUERY; }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
    }

    public static class SepiaSearch extends DefaultSearchExtractorTest {
        private static final String QUERY = "kde";

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            // setting instance might break test when running in parallel
            PeerTube.setInstance(new PeertubeInstance("https://framatube.org", "Framatube"));
            return PeerTube.getSearchExtractor(QUERY, singletonList(PeertubeSearchQueryHandlerFactory.SEPIA_VIDEOS), "");
        }

        @Override public StreamingService expectedService() { return PeerTube; }
        @Override public String expectedName() { return QUERY; }
        @Override public String expectedId() { return QUERY; }
        @Override public String expectedUrlContains() { return "/search/videos?search=" + QUERY; }
        @Override public String expectedOriginalUrlContains() { return "/search/videos?search=" + QUERY; }
        @Override public String expectedSearchString() { return QUERY; }
        @Nullable @Override public String expectedSearchSuggestion() { return null; }
    }

    public static class PagingTest extends DefaultSimpleExtractorTest<SearchExtractor> {

        @Override
        protected SearchExtractor createExtractor() throws Exception {
            return PeerTube.getSearchExtractor("internet", singletonList(VIDEOS), "");
        }

        @Test
        void duplicatedItemsCheck() throws Exception {
            final InfoItemsPage<InfoItem> page1 = extractor().getInitialPage();
            final InfoItemsPage<InfoItem> page2 = extractor().getPage(page1.getNextPage());

            assertNoDuplicatedItems(PeerTube, page1, page2);
        }
    }
}
