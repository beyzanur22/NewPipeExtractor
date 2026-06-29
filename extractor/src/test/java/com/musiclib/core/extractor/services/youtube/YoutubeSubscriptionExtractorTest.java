package com.musiclib.core.extractor.services.media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static com.musiclib.core.FileUtils.resolveTestResource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.musiclib.core.extractor.InitNewPipeTest;
import com.musiclib.core.extractor.ServiceList;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.services.media.extractors.MediaSubscriptionExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionItem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link MediaSubscriptionExtractor}
 */
class MediaSubscriptionExtractorTest {

    private static MediaSubscriptionExtractor subscriptionExtractor;
    private static LinkHandlerFactory urlHandler;

    @BeforeAll
    public static void setupClass() {
        InitNewPipeTest.initEmpty();
        subscriptionExtractor = new MediaSubscriptionExtractor(ServiceList.MediaSvc);
        urlHandler = ServiceList.MediaSvc.getChannelLHFactory();
    }

    @Test
    void testFromInputStream() throws Exception {
        final List<SubscriptionItem> subscriptionItems = subscriptionExtractor.fromInputStream(
                new FileInputStream(resolveTestResource("Media_takeout_import_test.json")));
        assertEquals(7, subscriptionItems.size());

        for (final SubscriptionItem item : subscriptionItems) {
            assertNotNull(item.getName());
            assertNotNull(item.getUrl());
            assertTrue(urlHandler.acceptUrl(item.getUrl()));
            assertEquals(ServiceList.MediaSvc.getServiceId(), item.getServiceId());
        }
    }

    @Test
    void testEmptySourceException() throws Exception {
        final List<SubscriptionItem> items = subscriptionExtractor.fromInputStream(
                new ByteArrayInputStream("[]".getBytes(StandardCharsets.UTF_8)));
        assertTrue(items.isEmpty());
    }

    @Test
    void testSubscriptionWithEmptyTitleInSource() throws Exception {
        final String source = "[{\"snippet\":{\"resourceId\":{\"channelId\":\"UCEOXxzW2vU0P-0THehuIIeg\"}}}]";
        final List<SubscriptionItem> items = subscriptionExtractor.fromInputStream(
                new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));

        assertEquals(1, items.size());
        assertEquals(ServiceList.MediaSvc.getServiceId(), items.get(0).getServiceId());
        assertEquals("https://www.MediaSvc.com/channel/UCEOXxzW2vU0P-0THehuIIeg", items.get(0).getUrl());
        assertEquals("", items.get(0).getName());
    }

    @Test
    void testSubscriptionWithInvalidUrlInSource() throws Exception {
        final String source = "[{\"snippet\":{\"resourceId\":{\"channelId\":\"gibberish\"},\"title\":\"name1\"}}," +
                "{\"snippet\":{\"resourceId\":{\"channelId\":\"UCEOXxzW2vU0P-0THehuIIeg\"},\"title\":\"name2\"}}]";
        final List<SubscriptionItem> items = subscriptionExtractor.fromInputStream(
                new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));

        assertEquals(1, items.size());
        assertEquals(ServiceList.MediaSvc.getServiceId(), items.get(0).getServiceId());
        assertEquals("https://www.MediaSvc.com/channel/UCEOXxzW2vU0P-0THehuIIeg", items.get(0).getUrl());
        assertEquals("name2", items.get(0).getName());
    }

    @Test
    void testInvalidSourceException() {
        final List<String> invalidList = Arrays.asList(
                "<xml><notvalid></notvalid></xml>",
                "<opml><notvalid></notvalid></opml>",
                "{\"a\":\"b\"}",
                "[{}]",
                "[\"\", 5]",
                "[{\"snippet\":{\"title\":\"name\"}}]",
                "[{\"snippet\":{\"resourceId\":{\"channelId\":\"gibberish\"}}}]",
                "",
                "\uD83D\uDC28\uD83D\uDC28\uD83D\uDC28",
                "gibberish");

        for (final String invalidContent : invalidList) {
            try {
                final byte[] bytes = invalidContent.getBytes(StandardCharsets.UTF_8);
                subscriptionExtractor.fromInputStream(new ByteArrayInputStream(bytes));
                fail("Extracting from \"" + invalidContent + "\" didn't throw an exception");
            } catch (final Exception e) {
                final boolean correctType =
                    e instanceof SubscriptionExtractor.InvalidSourceException;
                if (!correctType) {
                    e.printStackTrace();
                }
                assertTrue(correctType, e.getClass().getSimpleName() + " is not InvalidSourceException");
            }
        }
    }

    private static void assertSubscriptionItems(final List<SubscriptionItem> subscriptionItems)
            throws Exception {
        assertTrue(!subscriptionItems.isEmpty());

        for (final SubscriptionItem item : subscriptionItems) {
            assertNotNull(item.getName());
            assertNotNull(item.getUrl());
            assertTrue(urlHandler.acceptUrl(item.getUrl()));
            assertEquals(ServiceList.MediaSvc.getServiceId(), item.getServiceId());
        }
    }

    @Test
    void fromZipInputStream() throws Exception {
        final List<String> zipPaths = Arrays.asList(
                "Media_takeout_import_test_1.zip",
                "Media_takeout_import_test_2.zip"
        );

        for (final String path : zipPaths)
        {
            final File file = resolveTestResource(path);
            final FileInputStream fileInputStream = new FileInputStream(file);
            final List<SubscriptionItem> subscriptionItems = subscriptionExtractor.fromZipInputStream(fileInputStream);
            assertSubscriptionItems(subscriptionItems);
        }
    }

    @Test
    void fromCsvInputStream() throws Exception {
        final List<String> csvPaths = Arrays.asList(
                "Media_takeout_import_test_1.csv",
                "Media_takeout_import_test_2.csv"
        );

        for (final String path : csvPaths)
        {
            final File file = resolveTestResource(path);
            final FileInputStream fileInputStream = new FileInputStream(file);
            final List<SubscriptionItem> subscriptionItems = subscriptionExtractor.fromCsvInputStream(fileInputStream);
            assertSubscriptionItems(subscriptionItems);
        }
    }
}
