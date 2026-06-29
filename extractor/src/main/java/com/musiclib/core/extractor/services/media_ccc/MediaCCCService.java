package com.musiclib.core.extractor.services.media_ccc;

import static com.musiclib.core.extractor.StreamingService.ServiceInfo.MediaCapability.AUDIO;
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
import com.musiclib.core.extractor.linkhandler.ReadyChannelTabListLinkHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandler;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandlerFactory;
import com.musiclib.core.extractor.playlist.PlaylistExtractor;
import com.musiclib.core.extractor.search.SearchExtractor;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCChannelTabExtractor;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCConferenceExtractor;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCConferenceKiosk;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCLiveStreamExtractor;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCLiveStreamKiosk;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCParsingHelper;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCRecentKiosk;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCSearchExtractor;
import com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCStreamExtractor;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCConferenceLinkHandlerFactory;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCConferencesListLinkHandlerFactory;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCLiveListLinkHandlerFactory;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCRecentListLinkHandlerFactory;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCSearchQueryHandlerFactory;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCStreamLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamExtractor;
import com.musiclib.core.extractor.subscription.SubscriptionExtractor;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.util.EnumSet;

public class MediaCCCService extends StreamingService {
    public MediaCCCService(final int id) {
        super(id, "media.ccc.de", EnumSet.of(AUDIO, VIDEO));
    }

    @Override
    public SearchExtractor getSearchExtractor(final SearchQueryHandler query) {
        return new MediaCCCSearchExtractor(this, query);
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return MediaCCCStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return MediaCCCConferenceLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelTabLHFactory() {
        // there is just one channel tab in MediaCCC, the one containing conferences, so there is
        // no need for a specific channel tab link handler, but we can just use the channel one
        return MediaCCCConferenceLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return null;
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return MediaCCCSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public StreamExtractor getStreamExtractor(final LinkHandler linkHandler) {
        if (MediaCCCParsingHelper.isLiveStreamId(linkHandler.getId())) {
            return new MediaCCCLiveStreamExtractor(this, linkHandler);
        }
        return new MediaCCCStreamExtractor(this, linkHandler);
    }

    @Override
    public ChannelExtractor getChannelExtractor(final ListLinkHandler linkHandler) {
        return new MediaCCCConferenceExtractor(this, linkHandler);
    }

    @Override
    public ChannelTabExtractor getChannelTabExtractor(final ListLinkHandler linkHandler) {
        if (linkHandler instanceof ReadyChannelTabListLinkHandler) {
            // conference data has already been fetched, let the ReadyChannelTabListLinkHandler
            // create a MediaCCCChannelTabExtractor with that data
            return ((ReadyChannelTabListLinkHandler) linkHandler).getChannelTabExtractor(this);
        } else {
            // conference data has not been fetched yet, so pass null instead
            return new MediaCCCChannelTabExtractor(this, linkHandler, null);
        }
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(final ListLinkHandler linkHandler) {
        return null;
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return null;
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        final KioskList list = new KioskList(this);
        final var conferencesLHF = MediaCCCConferencesListLinkHandlerFactory.getInstance();
        final var recentLHF = MediaCCCRecentListLinkHandlerFactory.getInstance();
        final var liveLHF = MediaCCCLiveListLinkHandlerFactory.getInstance();

        // add kiosks here e.g.:
        try {
            list.addKioskEntry(
                    (streamingService, url, kioskId) -> new MediaCCCConferenceKiosk(
                            MediaCCCService.this,
                            conferencesLHF.fromUrl(url),
                            kioskId
                    ),
                    conferencesLHF,
                    MediaCCCConferenceKiosk.KIOSK_ID
            );

            list.addKioskEntry(
                    (streamingService, url, kioskId) -> new MediaCCCRecentKiosk(
                            MediaCCCService.this,
                            recentLHF.fromUrl(url),
                            kioskId
                    ),
                    recentLHF,
                    MediaCCCRecentKiosk.KIOSK_ID
            );

            list.addKioskEntry(
                    (streamingService, url, kioskId) -> new MediaCCCLiveStreamKiosk(
                            MediaCCCService.this,
                            liveLHF.fromUrl(url),
                            kioskId
                    ),
                    liveLHF,
                    MediaCCCLiveStreamKiosk.KIOSK_ID
            );

            list.setDefaultKiosk(MediaCCCRecentKiosk.KIOSK_ID);
        } catch (final Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return null;
    }

    @Override
    public CommentsExtractor getCommentsExtractor(final ListLinkHandler linkHandler) {
        return null;
    }

    @Override
    public String getBaseUrl() {
        return "https://media.ccc.de";
    }

}
