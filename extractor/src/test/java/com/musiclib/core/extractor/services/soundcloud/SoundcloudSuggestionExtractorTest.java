package com.musiclib.core.extractor.services.soundcloud;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static com.musiclib.core.extractor.ServiceList.SoundCloud;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.services.DefaultSimpleUntypedExtractorTest;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.io.IOException;

/**
 * Test for {@link SuggestionExtractor}
 */
public class SoundcloudSuggestionExtractorTest extends DefaultSimpleUntypedExtractorTest<SuggestionExtractor> {

    @Override
    protected SuggestionExtractor createExtractor() throws Exception {
        return SoundCloud.getSuggestionExtractor();
    }

    @Test
    public void testIfSuggestions() throws IOException, ExtractionException {
        assertFalse(extractor().suggestionList("lil uzi vert").isEmpty());
    }
}
