package com.musiclib.core.extractor.services.media_ccc.extractors;

import com.grack.nanojson.JsonObject;

import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.ListExtractor;
import com.musiclib.core.extractor.MultiInfoItemsCollector;
import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.services.media_ccc.extractors.infoItems.MediaCCCStreamInfoItemExtractor;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * MediaCCC does not really have channel tabs, but rather a list of videos for each conference,
 * so this class just acts as a videos channel tab extractor.
 */
public class MediaCCCChannelTabExtractor extends ChannelTabExtractor {
    @Nullable
    private JsonObject conferenceData;

    /**
     * @param conferenceData will be not-null if conference data has already been fetched by
     *                       {@link MediaCCCConferenceExtractor}. Otherwise, if this parameter is
     *                       {@code null}, conference data will be fetched anew.
     */
    public MediaCCCChannelTabExtractor(final StreamingService service,
                                       final ListLinkHandler linkHandler,
                                       @Nullable final JsonObject conferenceData) {
        super(service, linkHandler);
        this.conferenceData = conferenceData;
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws ExtractionException, IOException {
        if (conferenceData == null) {
            // only fetch conference data if we don't have it already
            conferenceData = MediaCCCConferenceExtractor.fetchConferenceData(downloader, getId());
        }
    }

    @Nonnull
    @Override
    public ListExtractor.InfoItemsPage<InfoItem> getInitialPage() {
        final MultiInfoItemsCollector collector =
                new MultiInfoItemsCollector(getServiceId());
        Objects.requireNonNull(conferenceData) // will surely be != null after onFetchPage
                .getArray("events")
                .streamAsJsonObjects()
                .forEach(event -> collector.commit(new MediaCCCStreamInfoItemExtractor(event)));
        return new ListExtractor.InfoItemsPage<>(collector, null);
    }

    @Override
    public ListExtractor.InfoItemsPage<InfoItem> getPage(final Page page) {
        return ListExtractor.InfoItemsPage.emptyPage();
    }
}
