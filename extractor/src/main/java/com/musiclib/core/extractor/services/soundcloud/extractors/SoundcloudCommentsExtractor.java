package com.musiclib.core.extractor.services.soundcloud.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import com.musiclib.core.extractor.NewPipe;
import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.comments.CommentsExtractor;
import com.musiclib.core.extractor.comments.CommentsInfoItem;
import com.musiclib.core.extractor.comments.CommentsInfoItemsCollector;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.downloader.Response;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.exceptions.ReCaptchaException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

import java.io.IOException;

import javax.annotation.Nonnull;

import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

public class SoundcloudCommentsExtractor extends CommentsExtractor {
    public SoundcloudCommentsExtractor(final StreamingService service,
                                       final ListLinkHandler uiHandler) {
        super(service, uiHandler);
    }

    @Nonnull
    @Override
    public InfoItemsPage<CommentsInfoItem> getInitialPage() throws ExtractionException,
            IOException {
        return getPage(getUrl());
    }

    @Override
    public InfoItemsPage<CommentsInfoItem> getPage(final Page page) throws ExtractionException,
            IOException {
        if (page == null || isNullOrEmpty(page.getUrl())) {
            return InfoItemsPage.emptyPage();
        }
        return getPage(page.getUrl());
    }

    @Nonnull
    private InfoItemsPage<CommentsInfoItem> getPage(@Nonnull final String url)
            throws ParsingException, IOException, ReCaptchaException {
        final Downloader downloader = NewPipe.getDownloader();
        final Response response = downloader.get(url);

        final JsonObject json;
        try {
            json = JsonParser.object().from(response.responseBody());
        } catch (final JsonParserException e) {
            throw new ParsingException("Could not parse json", e);
        }

        final CommentsInfoItemsCollector collector = new CommentsInfoItemsCollector(
                getServiceId());

        collectStreamsFrom(collector, json.getArray("collection"));
        final String nextHref = json.getString("next_href");
        return new InfoItemsPage<>(collector, isNullOrEmpty(nextHref) ? null : new Page(nextHref));
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader) { }

    private void collectStreamsFrom(final CommentsInfoItemsCollector collector,
                                    final JsonArray entries) throws ParsingException {
        final String url = getUrl();
        for (final Object comment : entries) {
            collector.commit(new SoundcloudCommentsInfoItemExtractor((JsonObject) comment, url));
        }
    }
}
