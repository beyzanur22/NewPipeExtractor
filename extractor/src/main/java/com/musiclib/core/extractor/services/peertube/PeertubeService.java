package com.musiclib.core.extractor.services.peertube;

import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.COMMENTS;
import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.VIDEO;

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
import com.musiclib.core.extractor.linkhandler.SearchQueryHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandlerFactory;
import com.musiclib.core.extractor.playlist.PlaylistExtractor;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeAccountExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeChannelExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeChannelTabExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeCommentsExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubePlaylistExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeSearchExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeStreamExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeSuggestionExtractor;
import com.musiclib.core.extractor.services.peertube.extractors.PeertubeTrendingExtractor;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeChannelLinkHandlerFactory;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeChannelTabLinkHandlerFactory;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeCommentsLinkHandlerFactory;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubePlaylistLinkHandlerFactory;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeSearchQueryHandlerFactory;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeStreamLinkHandlerFactory;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeTrendingLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.util.EnumSet;
import java.util.List;

public class PeertubeService extends StreamingService {

    private PeertubeInstance instance;

    public PeertubeService(final int id) {
        this(id, PeertubeInstance.DEFAULT_INSTANCE);
    }

    public PeertubeService(final int id, final PeertubeInstance instance) {
        super(id, "PeerTube", EnumSet.of(VIDEO, COMMENTS));
        this.instance = instance;
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return PeertubeStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return PeertubeChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelTabLHFactory() {
        return PeertubeChannelTabLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return PeertubePlaylistLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return PeertubeSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return PeertubeCommentsLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchExtractor getSearchExtractor(final SearchQueryHandler queryHandler) {
        final List<String> contentFilters = queryHandler.getContentFilters();
        return new PeertubeSearchExtractor(this, queryHandler,
                !contentFilters.isEmpty() && contentFilters.get(0).startsWith("sepia_"));
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new PeertubeSuggestionExtractor(this);
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    @Override
    public ChannelExtractor getChannelExtractor(final ListLinkHandler linkHandler)
            throws ExtractionException {

        if (linkHandler.getUrl().contains("/video-channels/")) {
            return new PeertubeChannelExtractor(this, linkHandler);
        } else {
            return new PeertubeAccountExtractor(this, linkHandler);
        }
    }

    @Override
    public ChannelTabExtractor getChannelTabExtractor(final ListLinkHandler linkHandler)
            throws ExtractionException {
        return new PeertubeChannelTabExtractor(this, linkHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(final ListLinkHandler linkHandler)
            throws ExtractionException {
        return new PeertubePlaylistExtractor(this, linkHandler);
    }

    @Override
    public StreamExtractor getStreamExtractor(final LinkHandler linkHandler)
            throws ExtractionException {
        return new PeertubeStreamExtractor(this, linkHandler);
    }

    @Override
    public CommentsExtractor getCommentsExtractor(final ListLinkHandler linkHandler)
            throws ExtractionException {
        return new PeertubeCommentsExtractor(this, linkHandler);
    }

    @Override
    public String getBaseUrl() {
        return instance.getUrl();
    }

    public PeertubeInstance getInstance() {
        return this.instance;
    }

    public void setInstance(final PeertubeInstance instance) {
        this.instance = instance;
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        final PeertubeTrendingLinkHandlerFactory h =
                PeertubeTrendingLinkHandlerFactory.getInstance();

        final KioskList.KioskExtractorFactory kioskFactory = (streamingService, url, id) ->
                new PeertubeTrendingExtractor(
                        PeertubeService.this,
                        h.fromId(id),
                        id
                );

        final KioskList list = new KioskList(this);

        // add kiosks here e.g.:

        try {
            list.addKioskEntry(kioskFactory, h, PeertubeTrendingLinkHandlerFactory.KIOSK_TRENDING);
            list.addKioskEntry(kioskFactory, h,
                    PeertubeTrendingLinkHandlerFactory.KIOSK_MOST_LIKED);
            list.addKioskEntry(kioskFactory, h, PeertubeTrendingLinkHandlerFactory.KIOSK_RECENT);
            list.addKioskEntry(kioskFactory, h, PeertubeTrendingLinkHandlerFactory.KIOSK_LOCAL);
            list.setDefaultKiosk(PeertubeTrendingLinkHandlerFactory.KIOSK_TRENDING);
        } catch (final Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }
}
