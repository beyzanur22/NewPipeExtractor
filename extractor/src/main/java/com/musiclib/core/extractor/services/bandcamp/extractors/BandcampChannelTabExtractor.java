package com.musiclib.core.extractor.services.bandcamp.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.MultiInfoItemsCollector;
import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabs;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.services.bandcamp.extractors.streaminfoitem.BandcampDiscographStreamInfoItemExtractor;

import javax.annotation.Nonnull;
import java.io.IOException;

public class BandcampChannelTabExtractor extends ChannelTabExtractor {
    private JsonArray discography;
    private final String filter;

    public BandcampChannelTabExtractor(final StreamingService service,
                                       final ListLinkHandler linkHandler) {
        super(service, linkHandler);

        final String tab = linkHandler.getContentFilters().get(0);
        switch (tab) {
            case ChannelTabs.TRACKS:
                filter = "track";
                break;
            case ChannelTabs.ALBUMS:
                filter = "album";
                break;
            default:
                throw new IllegalArgumentException("Unsupported channel tab: " + tab);
        }
    }

    public static BandcampChannelTabExtractor fromDiscography(final StreamingService service,
                                                              final ListLinkHandler linkHandler,
                                                              final JsonArray discography) {
        final BandcampChannelTabExtractor tabExtractor =
                new BandcampChannelTabExtractor(service, linkHandler);
        tabExtractor.discography = discography;
        return tabExtractor;
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader) throws ParsingException {
        if (discography == null) {
            discography = BandcampExtractorHelper.getArtistDetails(getId())
                    .getArray("discography");
        }
    }

    @Nonnull
    @Override
    public InfoItemsPage<InfoItem> getInitialPage() throws IOException, ExtractionException {
        final MultiInfoItemsCollector collector = new MultiInfoItemsCollector(getServiceId());

        for (final Object discograph : discography) {
            // A discograph is as an item appears in a discography
            if (!(discograph instanceof JsonObject)) {
                continue;
            }

            final JsonObject discographJsonObject = (JsonObject) discograph;
            final String itemType = discographJsonObject.getString("item_type", "");

            if (!itemType.equals(filter)) {
                continue;
            }

            switch (itemType) {
                case "track":
                    collector.commit(new BandcampDiscographStreamInfoItemExtractor(
                            discographJsonObject, getUrl()));
                    break;
                case "album":
                    collector.commit(new BandcampAlbumInfoItemExtractor(
                            discographJsonObject, getUrl()));
                    break;
            }
        }

        return new InfoItemsPage<>(collector, null);
    }

    @Override
    public InfoItemsPage<InfoItem> getPage(final Page page) {
        return null;
    }
}
