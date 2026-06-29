package com.musiclib.core.extractor.services.peertube.extractors;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.MultiInfoItemsCollector;
import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.tabs.ChannelTabExtractor;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.downloader.Response;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper;
import com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeChannelLinkHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.COUNT_KEY;
import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.ITEMS_PER_PAGE;
import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.START_KEY;
import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.collectItemsFrom;
import static com.musiclib.core.extractor.services.peertube.linkHandler.PeertubeChannelTabLinkHandlerFactory.getUrlSuffix;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

public class PeertubeChannelTabExtractor extends ChannelTabExtractor {
    private final String baseUrl;

    public PeertubeChannelTabExtractor(final StreamingService service,
                                       final ListLinkHandler linkHandler)
            throws ParsingException {
        super(service, linkHandler);
        baseUrl = getBaseUrl();
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader) {
    }

    @Nonnull
    @Override
    public InfoItemsPage<InfoItem> getInitialPage() throws IOException, ExtractionException {
        return getPage(new Page(baseUrl + PeertubeChannelLinkHandlerFactory.API_ENDPOINT
                + getId() + getUrlSuffix(getName()) + "?" + START_KEY + "=0&" + COUNT_KEY + "="
                + ITEMS_PER_PAGE));
    }

    @Override
    public InfoItemsPage<InfoItem> getPage(final Page page)
            throws IOException, ExtractionException {
        if (page == null || isNullOrEmpty(page.getUrl())) {
            throw new IllegalArgumentException("Page doesn't contain an URL");
        }

        final Response response = getDownloader().get(page.getUrl());

        JsonObject pageJson = null;
        if (response != null && !Utils.isBlank(response.responseBody())) {
            try {
                pageJson = JsonParser.object().from(response.responseBody());
            } catch (final Exception e) {
                throw new ParsingException("Could not parse json data for account info", e);
            }
        }

        if (pageJson == null) {
            throw new ExtractionException("Unable to get account channel list");
        }
        PeertubeParsingHelper.validate(pageJson);

        final MultiInfoItemsCollector collector = new MultiInfoItemsCollector(getServiceId());
        collectItemsFrom(collector, pageJson, getBaseUrl());

        return new InfoItemsPage<>(collector,
                PeertubeParsingHelper.getNextPage(page.getUrl(), pageJson.getLong("total")));
    }
}
