package com.musiclib.core.extractor.services.media_ccc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static com.musiclib.core.extractor.ExtractorAsserts.assertGreater;
import static com.musiclib.core.extractor.ServiceList.MediaCCC;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.kiosk.KioskExtractor;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.stream.StreamInfoItem;

import java.util.List;
import java.util.stream.Stream;

public class MediaCCCRecentListExtractorTest extends DefaultSimpleExtractorTest<KioskExtractor>
    implements InitNewPipeTest {

    @Override
    protected KioskExtractor createExtractor() throws Exception {
        return MediaCCC.getKioskList().getExtractorById("recent", null);
    }

    @Test
    void testStreamList() throws Exception {
        final List<StreamInfoItem> items = extractor().getInitialPage().getItems();
        assertFalse(items.isEmpty(), "No items returned");

        assertAll(items.stream().flatMap(this::getAllConditionsForItem));
    }

    private Stream<Executable> getAllConditionsForItem(final StreamInfoItem item) {
        return Stream.of(
                () -> assertFalse(
                        isNullOrEmpty(item.getName()),
                        "Name=[" + item.getName() + "] of " + item + " is empty or null"
                ),
                () -> assertGreater(0,
                        item.getDuration(),
                        "Duration[=" + item.getDuration() + "] of " + item + " is <= 0"
                )
        );
    }
}
