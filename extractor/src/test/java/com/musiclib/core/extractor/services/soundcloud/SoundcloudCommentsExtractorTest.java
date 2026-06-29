package com.musiclib.core.extractor.services.soundcloud;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ServiceList.SoundCloud;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.ListExtractor.InfoItemsPage;
import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.comments.CommentsExtractor;
import com.musiclib.core.extractor.comments.CommentsInfoItem;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;

import java.io.IOException;

public class SoundcloudCommentsExtractorTest {

    /**
     * Regression test for <a href="https://github.com/TeamNewPipe/NewPipeExtractor/issues/1243">
     * issue #1243</a>: when the SoundCloud API returns {@code "next_href": null} (no more pages),
     * a subsequent call to {@link CommentsExtractor#getPage(Page)} with a null URL must not throw
     * {@link IllegalArgumentException} ("Page doesn't contain an URL"). Instead the extractor
     * must return {@link InfoItemsPage#emptyPage()}.
     *
     * <p>The crash manifests during pagination: the last page of comments stores
     * {@code new Page(null)} as the next page, and when Paging 3 tries to fetch it the
     * exception propagates and kills the app.</p>
     */
    @Nested
    class TrackWithComments extends DefaultSimpleExtractorTest<CommentsExtractor> {
        // This track is known to reproduce issue #1243: it has comments, but when pagination
        // exhausts the pages the API returns next_href=null, which previously caused a crash.
        private static final String URL = "https://soundcloud.com/user-722618400/a-real-playa";

        @Override
        protected CommentsExtractor createExtractor() throws Exception {
            return SoundCloud.getCommentsExtractor(URL);
        }

        /**
         * The initial page must load successfully without throwing any exception.
         */
        @Test
        void testGetInitialPageSucceeds() throws IOException, ExtractionException {
            final InfoItemsPage<CommentsInfoItem> page = extractor().getInitialPage();
            // The track has comments; we only assert the call itself does not throw
            // and that the result is a valid (non-null) page.
            assertTrue(page.getErrors().isEmpty(),
                    "Expected no extractor errors on initial page");
        }

        /**
         * Regression test for issue #1243: calling {@link CommentsExtractor#getPage(Page)} with a
         * {@link Page} whose URL is null (which is what gets stored when {@code next_href} is
         * absent in the API response) must return {@link InfoItemsPage#emptyPage()} rather than
         * throw {@link IllegalArgumentException}.
         */
        @Test
        void testGetPageWithNullUrlReturnsEmptyPage() throws IOException, ExtractionException {
            final InfoItemsPage<CommentsInfoItem> page = extractor().getPage(new Page((String) null));
            assertTrue(page.getItems().isEmpty(),
                    "Expected empty items when page URL is null");
            assertFalse(page.hasNextPage(),
                    "Expected no next page when page URL is null");
        }
    }

    /**
     * Tests a SoundCloud track that has no comments.
     *
     * <p>Verifies that the extractor handles an empty collection gracefully:
     * the initial page must load without error, return no items, and have no next page.</p>
     */
    @Nested
    class TrackWithNoComments extends DefaultSimpleExtractorTest<CommentsExtractor> {
        private static final String URL = "https://soundcloud.com/user285130010/jdkskls";

        @Override
        protected CommentsExtractor createExtractor() throws Exception {
            return SoundCloud.getCommentsExtractor(URL);
        }

        /**
         * The initial page must load successfully, return an empty items list,
         * and report no next page.
         */
        @Test
        void testGetInitialPageIsEmpty() throws IOException, ExtractionException {
            final InfoItemsPage<CommentsInfoItem> page = extractor().getInitialPage();
            assertTrue(page.getErrors().isEmpty(),
                    "Expected no extractor errors on initial page");
            assertTrue(page.getItems().isEmpty(),
                    "Expected no comments for a track with no comments");
            assertFalse(page.hasNextPage(),
                    "Expected no next page for a track with no comments");
        }
    }
}
