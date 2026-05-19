package org.schabi.newpipe.extractor.services.youtube.extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.exceptions.ContentNotAvailableException;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.feed.FeedExtractor;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;

import java.io.IOException;

import javax.annotation.Nonnull;
import org.schabi.newpipe.extractor.services.youtube.StringObfuscator;

public class YoutubeFeedExtractor extends FeedExtractor {
    private static final String WEBSITE_CHANNEL_BASE_URL = StringObfuscator.decode(new int[]{
    0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,
    0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,
    0x3D,0x36,0x3F,0x30,0x30,0x3B,0x32,0x71
});

    public YoutubeFeedExtractor(final StreamingService service, final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    private Document document;

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        final String channelIdOrUser = getLinkHandler().getId();
        final String feedUrl = YoutubeParsingHelper.getFeedUrlFrom(channelIdOrUser);

        final Response response = downloader.get(feedUrl);
        if (response.responseCode() == 404) {
            throw new ContentNotAvailableException("Could not get feed: 404 - not found");
        }
        document = Jsoup.parse(response.responseBody());
    }

    @Nonnull
    @Override
    public ListExtractor.InfoItemsPage<StreamInfoItem> getInitialPage() {
        final Elements entries = document.select("feed > entry");
        final StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());

        for (final Element entryElement : entries) {
            collector.commit(new YoutubeFeedInfoItemExtractor(entryElement));
        }

        return new InfoItemsPage<>(collector, null);
    }

    @Nonnull
    @Override
    public String getId() {
        return getUrl().replace(WEBSITE_CHANNEL_BASE_URL, "");
    }

    @Nonnull
    @Override
    public String getUrl() {
        final Element authorUriElement = document.select("feed > author > uri")
                .first();
        if (authorUriElement != null) {
            final String authorUriElementText = authorUriElement.text();
            if (!authorUriElementText.equals("")) {
                return authorUriElementText;
            }
        }

        final Element linkElement = document.select("feed > link[rel*=alternate]")
                .first();
        if (linkElement != null) {
            return linkElement.attr("href");
        }

        return "";
    }

    @Nonnull
    @Override
    public String getName() {
        final Element nameElement = document.select("feed > author > name")
                .first();
        if (nameElement == null) {
            return "";
        }

        return nameElement.text();
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page) {
        return InfoItemsPage.emptyPage();
    }
}
