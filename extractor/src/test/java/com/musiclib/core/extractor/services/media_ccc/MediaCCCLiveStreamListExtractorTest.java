package com.musiclib.core.extractor.services.media_ccc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static com.musiclib.core.extractor.ServiceList.MediaCCC;

import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.kiosk.KioskExtractor;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;

public class MediaCCCLiveStreamListExtractorTest extends DefaultSimpleExtractorTest<KioskExtractor> {

    @Override
    protected KioskExtractor createExtractor() throws Exception {
        return MediaCCC.getKioskList().getExtractorById("live", null);
    }

    @Test
    public void getConferencesListTest() {
        assertDoesNotThrow(() -> extractor().getInitialPage().getItems());
    }

}
