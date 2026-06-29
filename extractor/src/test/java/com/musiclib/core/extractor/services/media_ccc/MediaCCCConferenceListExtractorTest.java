package com.musiclib.core.extractor.services.media_ccc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.musiclib.core.extractor.ServiceList.MediaCCC;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.musiclib.core.extractor.ExtractorAsserts;
import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.kiosk.KioskExtractor;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCConferenceKiosk;

import java.util.List;


/**
 * Test {@link MediaCCCConferenceKiosk}
 */
public class MediaCCCConferenceListExtractorTest extends DefaultSimpleExtractorTest<KioskExtractor> {

    @Override
    protected KioskExtractor createExtractor() throws Exception {
        return MediaCCC.getKioskList().getExtractorById("conferences", null);
    }

    @Test
    void getConferencesListTest() throws Exception {
        ExtractorAsserts.assertGreaterOrEqual(174, extractor().getInitialPage().getItems().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "FrOSCon 2016",
            "ChaosWest @ 35c3",
            "CTreffOS chaOStalks",
            "Datenspuren 2015",
            "Chaos Singularity 2017",
            "SIGINT10",
            "Vintage Computing Festival Berlin 2015",
            "FIfFKon 2015",
            "33C3: trailers",
            "Blinkenlights"
    })
    void conferenceTypeTest(final String name) throws Exception {
        final List<InfoItem> itemList = extractor().getInitialPage().getItems();
        assertTrue(itemList.stream().anyMatch(item -> name.equals(item.getName())));
    }
}
