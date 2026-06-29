package com.musiclib.core.extractor.services.media_ccc.extractors;

import static com.musiclib.core.extractor.services.media_ccc.extractors.MediaCCCParsingHelper.getImageListFromLogoImageUrl;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.channel.ChannelExtractor;
import com.musiclib.core.extractor.channel.tabs.ChannelTabs;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.linkhandler.ReadyChannelTabListLinkHandler;
import com.musiclib.core.extractor.services.media_ccc.linkHandler.MediaCCCConferenceLinkHandlerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class MediaCCCConferenceExtractor extends ChannelExtractor {
    private JsonObject conferenceData;

    public MediaCCCConferenceExtractor(final StreamingService service,
                                       final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    static JsonObject fetchConferenceData(@Nonnull final Downloader downloader,
                                          @Nonnull final String conferenceId)
            throws IOException, ExtractionException {
        final String conferenceUrl
                = MediaCCCConferenceLinkHandlerFactory.CONFERENCE_API_ENDPOINT + conferenceId;
        try {
            return JsonParser.object().from(downloader.get(conferenceUrl).responseBody());
        } catch (final JsonParserException jpe) {
            throw new ExtractionException("Could not parse json returned by URL: " + conferenceUrl);
        }
    }


    @Nonnull
    @Override
    public List<Image> getAvatars() {
        return getImageListFromLogoImageUrl(conferenceData.getString("logo_url"));
    }

    @Nonnull
    @Override
    public List<Image> getBanners() {
        return Collections.emptyList();
    }

    @Override
    public String getFeedUrl() {
        return null;
    }

    @Override
    public long getSubscriberCount() {
        return -1;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getParentChannelName() {
        return "";
    }

    @Override
    public String getParentChannelUrl() {
        return "";
    }

    @Nonnull
    @Override
    public List<Image> getParentChannelAvatars() {
        return Collections.emptyList();
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    @Nonnull
    @Override
    public List<ListLinkHandler> getTabs() throws ParsingException {
        // avoid keeping a reference to MediaCCCConferenceExtractor inside the lambda
        final JsonObject theConferenceData = conferenceData;
        return List.of(new ReadyChannelTabListLinkHandler(getUrl(), getId(), ChannelTabs.VIDEOS,
                (service, linkHandler) ->
                        new MediaCCCChannelTabExtractor(service, linkHandler, theConferenceData)));
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        conferenceData = fetchConferenceData(downloader, getId());
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return conferenceData.getString("title");
    }
}
