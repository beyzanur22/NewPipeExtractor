// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package com.musiclib.core.extractor.services.bandcamp;

import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.AUDIO;
import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.COMMENTS;
import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampExtractorHelper.BASE_URL;
import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampFeaturedExtractor.FEATURED_API_URL;
import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampFeaturedExtractor.KIOSK_FEATURED;
import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampRadioExtractor.KIOSK_RADIO;
import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampRadioExtractor.RADIO_API_URL;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.ChannelExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.comments.CommentsExtractor;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.kiosk.KioskList;
import com.musiclib.core.extractor.linkhandler.LinkHandler;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.linkhandler.ReadyChannelTabListLinkHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandlerFactory;
import com.musiclib.core.extractor.playlist.PlaylistExtractor;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampChannelExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampChannelTabExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampCommentsExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampExtractorHelper;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampFeaturedExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampPlaylistExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampRadioExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampRadioStreamExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampSearchExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampStreamExtractor;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampSuggestionExtractor;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampChannelLinkHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampChannelTabLinkHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampCommentsLinkHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampFeaturedLinkHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampPlaylistLinkHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampSearchQueryHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.linkHandler.BandcampStreamLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.util.EnumSet;

public class BandcampService extends StreamingService {

    public BandcampService(final int id) {
        super(id, "Bandcamp", EnumSet.of(AUDIO, COMMENTS));
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return BandcampStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return BandcampChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelTabLHFactory() {
        return BandcampChannelTabLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return BandcampPlaylistLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return BandcampSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return BandcampCommentsLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchExtractor getSearchExtractor(final SearchQueryHandler queryHandler) {
        return new BandcampSearchExtractor(this, queryHandler);
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new BandcampSuggestionExtractor(this);
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        final KioskList kioskList = new KioskList(this);
        final ListLinkHandlerFactory h = BandcampFeaturedLinkHandlerFactory.getInstance();

        try {
            kioskList.addKioskEntry(
                    (streamingService, url, kioskId) -> new BandcampFeaturedExtractor(
                            BandcampService.this,
                            h.fromUrl(FEATURED_API_URL),
                            kioskId
                    ),
                    h,
                    KIOSK_FEATURED
            );

            kioskList.addKioskEntry(
                    (streamingService, url, kioskId) -> new BandcampRadioExtractor(
                            BandcampService.this,
                            h.fromUrl(RADIO_API_URL),
                            kioskId
                    ),
                    h,
                    KIOSK_RADIO
            );

            kioskList.setDefaultKiosk(KIOSK_FEATURED);

        } catch (final Exception e) {
            throw new ExtractionException(e);
        }

        return kioskList;
    }

    @Override
    public ChannelExtractor getChannelExtractor(final ListLinkHandler linkHandler) {
        return new BandcampChannelExtractor(this, linkHandler);
    }

    @Override
    public ChannelTabExtractor getChannelTabExtractor(final ListLinkHandler linkHandler) {
        if (linkHandler instanceof ReadyChannelTabListLinkHandler) {
            return ((ReadyChannelTabListLinkHandler) linkHandler).getChannelTabExtractor(this);
        } else {
            return new BandcampChannelTabExtractor(this, linkHandler);
        }
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(final ListLinkHandler linkHandler) {
        return new BandcampPlaylistExtractor(this, linkHandler);
    }

    @Override
    public StreamExtractor getStreamExtractor(final LinkHandler linkHandler) {
        if (BandcampExtractorHelper.isRadioUrl(linkHandler.getUrl())) {
            return new BandcampRadioStreamExtractor(this, linkHandler);
        }
        return new BandcampStreamExtractor(this, linkHandler);
    }

    @Override
    public CommentsExtractor getCommentsExtractor(final ListLinkHandler linkHandler) {
        return new BandcampCommentsExtractor(this, linkHandler);
    }
}
