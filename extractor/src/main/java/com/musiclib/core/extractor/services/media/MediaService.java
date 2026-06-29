package com.musiclib.core.extractor.services.media;

import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.AUDIO;
import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.COMMENTS;
import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.LIVE;
import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.VIDEO;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.ChannelExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.comments.CommentsExtractor;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.feed.FeedExtractor;
import com.musiclib.core.extractor.kiosk.KioskList;
import com.musiclib.core.extractor.linkhandler.LinkHandler;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.linkhandler.ReadyChannelTabListLinkHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandlerFactory;
import com.musiclib.core.extractor.localization.ContentCountry;
import com.musiclib.core.extractor.localization.Localization;
import com.musiclib.core.extractor.playlist.PlaylistExtractor;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaChannelExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaChannelTabExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaCommentsExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaFeedExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaMixPlaylistExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaMusicSearchExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaPlaylistExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaSearchExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaStreamExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaSubscriptionExtractor;
import com.musiclib.core.extractor.services.media.extractors.MediaSuggestionExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaLiveExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingGamingVideosExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingMoviesAndShowsTrailersExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingMusicExtractor;
import com.musiclib.core.extractor.services.media.extractors.kiosk.MediaTrendingPodcastsEpisodesExtractor;
import com.musiclib.core.extractor.services.media.linkHandler.MediaChannelLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaChannelTabLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaCommentsLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaLiveLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaPlaylistLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaSearchQueryHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaStreamLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaTrendingGamingVideosLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaTrendingLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaTrendingMoviesAndShowsTrailersLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaTrendingMusicLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaTrendingPodcastsEpisodesLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;

/*
 * Created by Christian Schabesberger on 23.08.15.
 *
 * Copyright (C) 2018 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * MediaService.java is part of NewPipe Extractor.
 *
 * NewPipe Extractor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe Extractor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe Extractor.  If not, see <http://www.gnu.org/licenses/>.
 */

public class MediaService extends StreamingService {

    public MediaService(final int id) {
        super(id, StringObfuscator.decode(new int[]{0x03,0x31,0x2B,0x0E,0x2B,0x3C,0x3B}), EnumSet.of(AUDIO, VIDEO, LIVE, COMMENTS));
// = "Media"
    }

    @Override
    public String getBaseUrl() {
        return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33});
// = "https://Media.com"
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return MediaStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return MediaChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelTabLHFactory() {
        return MediaChannelTabLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return MediaPlaylistLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return MediaSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public StreamExtractor getStreamExtractor(final LinkHandler linkHandler) {
        return new MediaStreamExtractor(this, linkHandler);
    }

    @Override
    public ChannelExtractor getChannelExtractor(final ListLinkHandler linkHandler) {
        return new MediaChannelExtractor(this, linkHandler);
    }

    @Override
    public ChannelTabExtractor getChannelTabExtractor(final ListLinkHandler linkHandler) {
        if (linkHandler instanceof ReadyChannelTabListLinkHandler) {
            return ((ReadyChannelTabListLinkHandler) linkHandler).getChannelTabExtractor(this);
        } else {
            return new MediaChannelTabExtractor(this, linkHandler);
        }
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(final ListLinkHandler linkHandler) {
        if (MediaParsingHelper.isMediaMixId(linkHandler.getId())) {
            return new MediaMixPlaylistExtractor(this, linkHandler);
        } else {
            return new MediaPlaylistExtractor(this, linkHandler);
        }
    }

    @Override
    public SearchExtractor getSearchExtractor(final SearchQueryHandler query) {
        final List<String> contentFilters = query.getContentFilters();

        if (!contentFilters.isEmpty() && contentFilters.get(0).startsWith("music_")) {
            return new MediaMusicSearchExtractor(this, query);
        } else {
            return new MediaSearchExtractor(this, query);
        }
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new MediaSuggestionExtractor(this);
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        final KioskList list = new KioskList(this);
        final ListLinkHandlerFactory trendingLHF = MediaTrendingLinkHandlerFactory.INSTANCE;
        final ListLinkHandlerFactory runningLivesLHF =
                MediaLiveLinkHandlerFactory.INSTANCE;
        final ListLinkHandlerFactory trendingPodcastsEpisodesLHF =
                MediaTrendingPodcastsEpisodesLinkHandlerFactory.INSTANCE;
        final ListLinkHandlerFactory trendingGamingVideosLHF =
                MediaTrendingGamingVideosLinkHandlerFactory.INSTANCE;
        final ListLinkHandlerFactory trendingMoviesAndShowsLHF =
                MediaTrendingMoviesAndShowsTrailersLinkHandlerFactory.INSTANCE;
        final ListLinkHandlerFactory trendingMusicLHF =
                MediaTrendingMusicLinkHandlerFactory.INSTANCE;

        try {
            list.addKioskEntry(
                    (streamingService, url, id) -> new MediaLiveExtractor(
                            MediaService.this,
                            runningLivesLHF.fromUrl(url),
                            id),
                    runningLivesLHF,
                    MediaLiveLinkHandlerFactory.KIOSK_ID
            );
            list.addKioskEntry(
                    (streamingService, url, id) -> new MediaTrendingPodcastsEpisodesExtractor(
                            MediaService.this,
                            trendingPodcastsEpisodesLHF.fromUrl(url),
                            id),
                    trendingPodcastsEpisodesLHF,
                    MediaTrendingPodcastsEpisodesLinkHandlerFactory.KIOSK_ID
            );
            list.addKioskEntry(
                    (streamingService, url, id) -> new MediaTrendingGamingVideosExtractor(
                            MediaService.this,
                            trendingGamingVideosLHF.fromUrl(url),
                            id),
                    trendingGamingVideosLHF,
                    MediaTrendingGamingVideosLinkHandlerFactory.KIOSK_ID
            );
            list.addKioskEntry(
                    (streamingService, url, id) ->
                            new MediaTrendingMoviesAndShowsTrailersExtractor(
                                    MediaService.this,
                                    trendingMoviesAndShowsLHF.fromUrl(url),
                                    id),
                    trendingMoviesAndShowsLHF,
                    MediaTrendingMoviesAndShowsTrailersLinkHandlerFactory.KIOSK_ID
            );
            list.addKioskEntry(
                    (streamingService, url, id) -> new MediaTrendingMusicExtractor(
                            MediaService.this,
                            trendingMusicLHF.fromUrl(url),
                            id),
                    trendingMusicLHF,
                    MediaTrendingMusicLinkHandlerFactory.KIOSK_ID
            );
            // Deprecated (i.e. removed from the interface of Media) since July 21, 2025
            list.addKioskEntry(
                    (streamingService, url, id) -> new MediaTrendingExtractor(
                            MediaService.this,
                            trendingLHF.fromUrl(url),
                            id
                    ),
                    trendingLHF,
                    MediaTrendingExtractor.KIOSK_ID
            );
            list.setDefaultKiosk(MediaLiveLinkHandlerFactory.KIOSK_ID);
        } catch (final Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return new MediaSubscriptionExtractor(this);
    }

    @Nonnull
    @Override
    public FeedExtractor getFeedExtractor(final String channelUrl) throws ExtractionException {
        return new MediaFeedExtractor(this, getChannelLHFactory().fromUrl(channelUrl));
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return MediaCommentsLinkHandlerFactory.getInstance();
    }

    @Override
    public CommentsExtractor getCommentsExtractor(final ListLinkHandler urlIdHandler)
            throws ExtractionException {
        return new MediaCommentsExtractor(this, urlIdHandler);
    }

    /*//////////////////////////////////////////////////////////////////////////
    // Localization
    //////////////////////////////////////////////////////////////////////////*/

    // https://www.Media.com/picker_ajax?action_language_json=1
    private static final List<Localization> SUPPORTED_LANGUAGES = Localization.listFrom(
            "en-GB"
            /*"af", "am", "ar", "az", "be", "bg", "bn", "bs", "ca", "cs", "da", "de",
            "el", "en", "en-GB", "es", "es-419", "es-US", "et", "eu", "fa", "fi", "fil", "fr",
            "fr-CA", "gl", "gu", "hi", "hr", "hu", "hy", "id", "is", "it", "iw", "ja",
            "ka", "kk", "km", "kn", "ko", "ky", "lo", "lt", "lv", "mk", "ml", "mn",
            "mr", "ms", "my", "ne", "nl", "no", "pa", "pl", "pt", "pt-PT", "ro", "ru",
            "si", "sk", "sl", "sq", "sr", "sr-Latn", "sv", "sw", "ta", "te", "th", "tr",
            "uk", "ur", "uz", "vi", "zh-CN", "zh-HK", "zh-TW", "zu"*/
    );

    // https://www.Media.com/picker_ajax?action_country_json=1
    private static final List<ContentCountry> SUPPORTED_COUNTRIES = ContentCountry.listFrom(
            "DZ", "AR", "AU", "AT", "AZ", "BH", "BD", "BY", "BE", "BO", "BA", "BR", "BG", "KH",
            "CA", "CL", "CO", "CR", "HR", "CY", "CZ", "DK", "DO", "EC", "EG", "SV", "EE", "FI",
            "FR", "GE", "DE", "GH", "GR", "GT", "HN", "HK", "HU", "IS", "IN", "ID", "IQ", "IE",
            "IL", "IT", "JM", "JP", "JO", "KZ", "KE", "KW", "LA", "LV", "LB", "LY", "LI", "LT",
            "LU", "MY", "MT", "MX", "ME", "MA", "NP", "NL", "NZ", "NI", "NG", "MK", "NO", "OM",
            "PK", "PA", "PG", "PY", "PE", "PH", "PL", "PT", "PR", "QA", "RO", "RU", "SA", "SN",
            "RS", "SG", "SK", "SI", "ZA", "KR", "ES", "LK", "SE", "CH", "TW", "TZ", "TH", "TN",
            "TR", "UG", "UA", "AE", "GB", "US", "UY", "VE", "VN", "YE", "ZW"
    );

    @Override
    public List<Localization> getSupportedLocalizations() {
        return SUPPORTED_LANGUAGES;
    }

    @Override
    public List<ContentCountry> getSupportedCountries() {
        return SUPPORTED_COUNTRIES;
    }
}
