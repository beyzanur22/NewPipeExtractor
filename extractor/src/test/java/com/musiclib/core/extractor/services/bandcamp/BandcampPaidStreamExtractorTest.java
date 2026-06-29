package com.musiclib.core.extractor.services.bandcamp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.musiclib.core.extractor.ServiceList.Bandcamp;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.PaidContentException;
import com.musiclib.core.extractor.stream.StreamExtractor;

public class BandcampPaidStreamExtractorTest implements InitNewPipeTest {

    @Test
    public void testPaidTrack() throws ExtractionException {
        final StreamExtractor extractor = Bandcamp.getStreamExtractor(
            "https://radicaldreamland.bandcamp.com/track/hackmud-continuous-mix");
        assertThrows(PaidContentException.class, extractor::fetchPage);
    }
}
