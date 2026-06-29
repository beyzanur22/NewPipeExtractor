package com.musiclib.core.extractor.services.youtube;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.musiclib.core.extractor.ServiceList.YouTube;
import static com.musiclib.core.extractor.services.DefaultTests.assertNoMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestMoreItems;
import static com.musiclib.core.extractor.services.DefaultTests.defaultTestRelatedItems;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.services.BaseListExtractorTest;
import com.musiclib.core.extractor.services.DefaultSimpleExtractorTest;
import com.musiclib.core.extractor.services.youtube.extractors.kiosk.YoutubeLiveExtractor;
import com.musiclib.core.extractor.services.youtube.extractors.kiosk.YoutubeTrendingExtractor;
import com.musiclib.core.extractor.services.youtube.extractors.kiosk.YoutubeTrendingGamingVideosExtractor;
import com.musiclib.core.extractor.services.youtube.extractors.kiosk.YoutubeTrendingMoviesAndShowsTrailersExtractor;
import com.musiclib.core.extractor.services.youtube.extractors.kiosk.YoutubeTrendingMusicExtractor;
import com.musiclib.core.extractor.services.youtube.extractors.kiosk.YoutubeTrendingPodcastsEpisodesExtractor;

public class YoutubeKioskExtractorTest {

    public static class Live extends DefaultSimpleExtractorTest<YoutubeLiveExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected YoutubeLiveExtractor createExtractor() throws Exception {
            return (YoutubeLiveExtractor) YouTube.getKioskList().getDefaultKioskExtractor();
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
                    "https://www.youtube.com/channel/UC4R8DWoMoI7CAwX8_LjQHig/livetab?ss=CKEK",
                    extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals(
                    "https://www.youtube.com/channel/UC4R8DWoMoI7CAwX8_LjQHig/livetab?ss=CKEK",
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
            DefaultSimpleExtractorTest<YoutubeTrendingPodcastsEpisodesExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected YoutubeTrendingPodcastsEpisodesExtractor createExtractor() throws Exception {
            return (YoutubeTrendingPodcastsEpisodesExtractor) YouTube.getKioskList()
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
            assertEquals("https://www.youtube.com/podcasts/popularepisodes", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://www.youtube.com/podcasts/popularepisodes",
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
            DefaultSimpleExtractorTest<YoutubeTrendingGamingVideosExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected YoutubeTrendingGamingVideosExtractor createExtractor() throws Exception {
            return (YoutubeTrendingGamingVideosExtractor) YouTube.getKioskList()
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
            assertEquals("https://www.youtube.com/gaming/trending", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://www.youtube.com/gaming/trending", extractor().getOriginalUrl());
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
            DefaultSimpleExtractorTest<YoutubeTrendingMoviesAndShowsTrailersExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected YoutubeTrendingMoviesAndShowsTrailersExtractor createExtractor() throws Exception {
            return (YoutubeTrendingMoviesAndShowsTrailersExtractor) YouTube.getKioskList()
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
            assertEquals("https://charts.youtube.com/charts/TrendingTrailers",
                    extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://charts.youtube.com/charts/TrendingTrailers",
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
            DefaultSimpleExtractorTest<YoutubeTrendingMusicExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {
        @Override
        protected YoutubeTrendingMusicExtractor createExtractor() throws Exception {
            return (YoutubeTrendingMusicExtractor) YouTube.getKioskList()
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
            assertEquals("https://charts.youtube.com/charts/TrendingVideos/RightNow",
                    extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws Exception {
            assertEquals("https://charts.youtube.com/charts/TrendingVideos/RightNow",
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
    public static class Trending extends DefaultSimpleExtractorTest<YoutubeTrendingExtractor>
            implements BaseListExtractorTest, InitYoutubeTest {

        @Override
        protected YoutubeTrendingExtractor createExtractor() throws Exception {
            return (YoutubeTrendingExtractor) YouTube.getKioskList().getExtractorById(
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
            assertEquals("https://www.youtube.com/feed/trending", extractor().getUrl());
        }

        @Override
        @Test
        public void testOriginalUrl() throws ParsingException {
            assertEquals("https://www.youtube.com/feed/trending", extractor().getOriginalUrl());
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
