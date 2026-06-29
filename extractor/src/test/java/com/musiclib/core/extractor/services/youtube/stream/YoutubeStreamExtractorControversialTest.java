package com.musiclib.core.extractor.services.youtube.stream;

import static com.musiclib.core.extractor.ServiceList.YouTube;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.services.DefaultStreamExtractorTest;
import com.musiclib.core.extractor.services.youtube.InitYoutubeTest;
import com.musiclib.core.extractor.services.youtube.linkHandler.YoutubeStreamLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamExtractor;
import com.musiclib.core.extractor.stream.StreamType;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Test for {@link YoutubeStreamLinkHandlerFactory}
 */

public class YoutubeStreamExtractorControversialTest extends DefaultStreamExtractorTest
    implements InitYoutubeTest {
    private static final String ID = "T4XJQO3qol8";
    private static final String URL = YoutubeStreamExtractorDefaultTest.BASE_URL + ID;

    @Override
    protected StreamExtractor createExtractor() throws Exception {
        return YouTube.getStreamExtractor(URL);
    }

    @Override public StreamingService expectedService() { return YouTube; }
    @Override public String expectedName() { return "Burning Everyone's Koran"; }
    @Override public String expectedId() { return ID; }
    @Override public String expectedUrlContains() { return URL; }
    @Override public String expectedOriginalUrlContains() { return URL; }

    @Override public StreamType expectedStreamType() { return StreamType.VIDEO_STREAM; }
    @Override public String expectedUploaderName() { return "INTO THE FRAY"; }
    @Override public String expectedUploaderUrl() { return "https://www.youtube.com/channel/UCjNxszyFPasDdRoD9J6X-sw"; }
    @Override public long expectedUploaderSubscriberCountAtLeast() { return 900_000; }
    @Override public List<String> expectedDescriptionContains() {
        return Arrays.asList("http://www.huffingtonpost.com/2010/09/09/obama-gma-interview-quran_n_710282.html",
                "freedom");
    }
    @Override public long expectedLength() { return 219; }
    @Override public long expectedViewCountAtLeast() { return 285000; }
    @Nullable @Override public String expectedUploadDate() { return "2010-09-09 15:40:44.000"; }
    @Nullable @Override public String expectedTextualUploadDate() { return "2010-09-09T08:40:44-07:00"; }
    @Override public long expectedLikeCountAtLeast() { return 13300; }
    @Override public long expectedDislikeCountAtLeast() { return -1; }
    @Override public List<String> expectedTags() { return Arrays.asList("Books", "Burning", "Jones", "Koran", "Qur'an", "Terry", "the amazing atheist"); }
    @Override public String expectedCategory() { return "Entertainment"; }
    @Override public String expectedLicence() { return "YouTube licence"; }
}
