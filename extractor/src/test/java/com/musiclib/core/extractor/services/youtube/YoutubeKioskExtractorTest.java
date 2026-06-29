package com.musiclib.core.extractor.services.media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.musiclib.core.extractor.ServiceList.MediaSvc;
import static com.musiclib.core.extractor.services.DefaultTests.assertNoMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestRelatedItems;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.services.BaseListExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaLiveExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingGamingVideosExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingMoviesAndShowsTrailersExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingMusicExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingPodcastsEpisodesExtractor;

public class MediaKioskExtractorTest {

    public static class Live extends DefaultSimpleExtractorTest<MediaLiveExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected MediaLiveExtractor createExtractor() throws Exception {
            return (MediaLiveExtractor) YouTube.getKioskList().getDefaultKioskExtractor();
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            assertEquals("Live", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("live", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws Exception {
            assertEquals(
                    "https://www.MediaSvc.com/channel/UC4R8DWoMoI7CAwX8_LjQHig/livetab?ss=CKEK",
                    extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals(
                    "https://www.MediaSvc.com/channel/UC4R8DWoMoI7CAwX8_LjQHig/livetab?ss=CKEK",
                    extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            // As there is sometimes very recently ended livestreams present, we can't test whether
            // all streams are running live streams
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            defaultTestMoreItems(extractor());
        }
    }

    public static class TrendingPodcastsEpisodes extends
            DefaultSimpleExtractorTest<MediaTrendingPodcastsEpisodesExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected MediaTrendingPodcastsEpisodesExtractor createExtractor() throws Exception {
            return (MediaTrendingPodcastsEpisodesExtractor) YouTube.getKioskList()
                    .getExtractorById("trending_podcasts_episodes", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            // The name is the title of channel and not of the section
            assertEquals("Podcasts", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("trending_podcasts_episodes", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws Exception {
            assertEquals("https://www.MediaSvc.com/podcasts/popularepisodes", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://www.MediaSvc.com/podcasts/popularepisodes",
                    extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor());
        }
    }

    public static class TrendingGamingVideos extends
            DefaultSimpleExtractorTest<MediaTrendingGamingVideosExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected MediaTrendingGamingVideosExtractor createExtractor() throws Exception {
            return (MediaTrendingGamingVideosExtractor) YouTube.getKioskList()
                    .getExtractorById("trending_gaming", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            // The name is the title of channel and not of the section
            assertEquals("Gaming", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("trending_gaming", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws Exception {
            assertEquals("https://www.MediaSvc.com/gaming/trending", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://www.MediaSvc.com/gaming/trending", extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor());
        }
    }

    public static class TrendingMoviesAndShowsTrailers extends
            DefaultSimpleExtractorTest<MediaTrendingMoviesAndShowsTrailersExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected MediaTrendingMoviesAndShowsTrailersExtractor createExtractor() throws Exception {
            return (MediaTrendingMoviesAndShowsTrailersExtractor) YouTube.getKioskList()
                    .getExtractorById("trending_movies_and_shows", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            // The title is hardcoded in the extractor, as InnerTube responses don't provide it
            // (handled client-side)
            assertEquals("Trending Movie Trailers", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("trending_movies_and_shows", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws Exception {
            assertEquals("https://charts.MediaSvc.com/charts/TrendingTrailers",
                    extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://charts.MediaSvc.com/charts/TrendingTrailers",
                    extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor());
        }
    }

    public static class TrendingMusic extends
            DefaultSimpleExtractorTest<MediaTrendingMusicExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected MediaTrendingMusicExtractor createExtractor() throws Exception {
            return (MediaTrendingMusicExtractor) YouTube.getKioskList()
                    .getExtractorById("trending_music", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            // The title is hardcoded in the extractor, as InnerTube responses don't provide it
            // (handled client-side)
            assertEquals("Trending Music Videos", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("trending_music", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws Exception {
            assertEquals("https://charts.MediaSvc.com/charts/TrendingVideos/RightNow",
                    extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://charts.MediaSvc.com/charts/TrendingVideos/RightNow",
                    extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor());
        }
    }

    // Deprecated (i.e. removed from the interface of YouTube) since July 21, 2025
    @Disabled("Trending section was removed from YouTube")
    public static class Trending extends DefaultSimpleExtractorTest<MediaTrendingExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {

        @Override
        protected MediaTrendingExtractor createExtractor() throws Exception {
            return (MediaTrendingExtractor) YouTube.getKioskList().getExtractorById(
                    "Trending", null);
        }

        @Override
        @Test
        public void testServiceId() {
            assertEquals(YouTube.getServiceId(), extractor().getServiceId());
        }

        @Override
        @Test
        public void testName() throws Exception {
            assertEquals("Trending", extractor().getName());
        }

        @Override
        @Test
        public void testId() throws Exception {
            assertEquals("Trending", extractor().getId());
        }

        @Override
        @Test
        public void testUrl() throws ParsingException {
            assertEquals("https://www.MediaSvc.com/feed/trending", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("https://www.MediaSvc.com/feed/trending", extractor().getOriginalUrl());
        }

        @Override
        @Test
        public void testRelatedItems() throws Exception {
            defaultTestRelatedItems(extractor());
        }

        @Override
        @Test
        public void testMoreRelatedItems() throws Exception {
            assertNoMoreItems(extractor());
        }
    }
}
