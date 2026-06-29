package com.musiclib.core.extractor.services.media;

import com.musiclib.core.extractor.ExtractorAsserts;
import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.services.DefaultTests;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

/**
 * Utility class for YouTube tests.
 */
public final class MediaTestsUtils {
    private MediaTestsUtils() {
        // No impl
    }

    /**
     * Clears static YT states.
     *
     * <p>
     * This method needs to be called to generate all mocks of a test when running different tests
     * at the same time.
     * </p>
     */
    public static void ensureStateless() {
        MediaParsingHelper.setConsentAccepted(false);
        MediaParsingHelper.resetClientVersion();
        MediaParsingHelper.setNumberGenerator(new Random(1));
        MediaJavaScriptPlayerManager.clearAllCaches();
    }

    /**
     * Test that YouTube images of a {@link Collection} respect
     * {@link DefaultTests#defaultTestImageCollection(Collection) default requirements} and contain
     * the string {@code yt} in their URL.
     *
     * @param images a YouTube {@link Image} {@link Collection}
     */
    public static void testImages(@Nullable final Collection<Image> images) {
        DefaultTests.defaultTestImageCollection(images);
        // Disable NPE warning because if the collection is null, an AssertionError would be thrown
        // by DefaultTests.defaultTestImageCollection
        //noinspection DataFlowIssue
        images.forEach(image ->
            ExtractorAsserts.assertContains("yt", image.getUrl()));
    }
}
