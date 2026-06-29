package com.musiclib.core.extractor.services.bandcamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ServiceList.Bandcamp;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.ListExtractor;
import com.musiclib.core.extractor.comments.CommentsExtractor;
import com.musiclib.core.extractor.comments.CommentsInfoItem;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.DefaultTests;
import com.musiclib.core.extractor.utils.Utils;

import java.io.IOException;

public class BandcampCommentsExtractorTest extends DefaultSimpleExtractorTest<CommentsExtractor> {

    @Override
    protected CommentsExtractor createExtractor() throws Exception {
        return Bandcamp.getCommentsExtractor("https://floatingpoints.bandcamp.com/album/promises");
    }

    @Test
    void hasComments() throws IOException, ExtractionException {
        assertTrue(extractor().getInitialPage().getItems().size() >= 3);
    }

    @Test
    void testGetCommentsAllData() throws IOException, ExtractionException {
        final ListExtractor.InfoItemsPage<CommentsInfoItem> comments = extractor().getInitialPage();
        assertTrue(comments.hasNextPage());

        DefaultTests.defaultTestListOfItems(Bandcamp, comments.getItems(), comments.getErrors());
        for (final CommentsInfoItem c : comments.getItems()) {
            assertFalse(Utils.isBlank(c.getUploaderName()));
            BandcampTestUtils.testImages(c.getUploaderAvatars());
            assertFalse(Utils.isBlank(c.getCommentText().getContent()));
            assertFalse(Utils.isBlank(c.getName()));
            BandcampTestUtils.testImages(c.getThumbnails());
            assertFalse(Utils.isBlank(c.getUrl()));
            assertEquals(-1, c.getLikeCount());
            assertTrue(Utils.isBlank(c.getTextualLikeCount()));
        }
    }
}
