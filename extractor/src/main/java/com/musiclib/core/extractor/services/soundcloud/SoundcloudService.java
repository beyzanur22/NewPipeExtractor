package com.musiclib.core.extractor.services.soundcloud;

import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.AUDIO;
import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.COMMENTS;

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
import com.musiclib.core.extractor.localization.ContentCountry;
import com.musiclib.core.extractor.playlist.PlaylistExtractor;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudChannelExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudChannelTabExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudChartsExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudCommentsExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudPlaylistExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudSearchExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudStreamExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudSubscriptionExtractor;
import com.musiclib.core.extractor.services.soundcloud.extractors.SoundcloudSuggestionExtractor;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudChannelLinkHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudChannelTabLinkHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudChartsLinkHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudCommentsLinkHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudPlaylistLinkHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudSearchQueryHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.linkHandler.SoundcloudStreamLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;

import java.util.EnumSet;
import java.util.List;

public class SoundcloudService extends StreamingService {

    public SoundcloudService(final int id) {
        super(id, "SoundCloud", EnumSet.of(AUDIO, COMMENTS));
    }

    @Override
    public String getBaseUrl() {
        return "https://soundcloud.com";
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return SoundcloudSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return SoundcloudStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return SoundcloudChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelTabLHFactory() {
        return SoundcloudChannelTabLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return SoundcloudPlaylistLinkHandlerFactory.getInstance();
    }

    @Override
    public List<ContentCountry> getSupportedCountries() {
        // Country selector here: https://soundcloud.com/charts/top?genre=all-music
        return ContentCountry.listFrom(
                "AU", "CA", "DE", "FR", "GB", "IE", "NL", "NZ", "US"
        );
    }

    @Override
    public StreamExtractor getStreamExtractor(final LinkHandler linkHandler) {
        return new SoundcloudStreamExtractor(this, linkHandler);
    }

    @Override
    public ChannelExtractor getChannelExtractor(final ListLinkHandler linkHandler) {
        return new SoundcloudChannelExtractor(this, linkHandler);
    }

    @Override
    public ChannelTabExtractor getChannelTabExtractor(final ListLinkHandler linkHandler) {
        return new SoundcloudChannelTabExtractor(this, linkHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(final ListLinkHandler linkHandler) {
        return new SoundcloudPlaylistExtractor(this, linkHandler);
    }

    @Override
    public SearchExtractor getSearchExtractor(final SearchQueryHandler queryHandler) {
        return new SoundcloudSearchExtractor(this, queryHandler);
    }

    @Override
    public SoundcloudSuggestionExtractor getSuggestionExtractor() {
        return new SoundcloudSuggestionExtractor(this);
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        final KioskList list = new KioskList(this);

        final SoundcloudChartsLinkHandlerFactory h =
                SoundcloudChartsLinkHandlerFactory.getInstance();
        final KioskList.KioskExtractorFactory chartsFactory = (streamingService, url, id) ->
                new SoundcloudChartsExtractor(SoundcloudService.this,
                        h.fromUrl(url), id);

        // add kiosks here e.g.:
        try {
            list.addKioskEntry(chartsFactory, h, "New & hot");
            list.setDefaultKiosk("New & hot");
        } catch (final Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return new SoundcloudSubscriptionExtractor(this);
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return SoundcloudCommentsLinkHandlerFactory.getInstance();
    }

    @Override
    public CommentsExtractor getCommentsExtractor(final ListLinkHandler linkHandler)
            throws ExtractionException {
        return new SoundcloudCommentsExtractor(this, linkHandler);
    }
}
