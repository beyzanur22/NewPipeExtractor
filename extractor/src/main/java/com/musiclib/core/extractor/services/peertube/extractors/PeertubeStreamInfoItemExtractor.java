package com.musiclib.core.extractor.services.peertube.extractors;

import com.grack.nanojson.JsonObject;
import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.ServiceList;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.localization.DateWrapper;
import com.musiclib.core.extractor.stream.StreamInfoItemExtractor;
import com.musiclib.core.extractor.stream.StreamType;
import com.musiclib.core.extractor.utils.JsonUtils;

import javax.annotation.Nonnull;
import java.util.List;

import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.getAvatarsFromOwnerAccountOrVideoChannelObject;
import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.getThumbnailsFromPlaylistOrVideoItem;

public class PeertubeStreamInfoItemExtractor implements StreamInfoItemExtractor {

    protected final JsonObject item;
    private String baseUrl;

    public PeertubeStreamInfoItemExtractor(final JsonObject item, final String baseUrl) {
        this.item = item;
        this.baseUrl = baseUrl;
    }

    @Override
    public String getUrl() throws ParsingException {
        final String uuid = JsonUtils.getString(item, "uuid");
        return ServiceList.PeerTube.getStreamLHFactory().fromId(uuid, baseUrl).getUrl();
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return getThumbnailsFromPlaylistOrVideoItem(baseUrl, item);
    }

    @Override
    public String getName() throws ParsingException {
        return JsonUtils.getString(item, "name");
    }

    @Override
    public boolean isAd() {
        return false;
    }

    @Override
    public long getViewCount() {
        return item.getLong("views");
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        final String name = JsonUtils.getString(item, "account.name");
        final String host = JsonUtils.getString(item, "account.host");

        return ServiceList.PeerTube.getChannelLHFactory()
                .fromId("accounts/" + name + "@" + host, baseUrl).getUrl();
    }

    @Nonnull
    @Override
    public List<Image> getUploaderAvatars() {
        return getAvatarsFromOwnerAccountOrVideoChannelObject(baseUrl, item.getObject("account"));
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return false;
    }

    @Override
    public String getUploaderName() throws ParsingException {
        return JsonUtils.getString(item, "account.displayName");
    }

    @Override
    public String getTextualUploadDate() throws ParsingException {
        return JsonUtils.getString(item, "publishedAt");
    }

    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        return DateWrapper.fromInstant(getTextualUploadDate());
    }

    @Override
    public StreamType getStreamType() {
        return item.getBoolean("isLive") ? StreamType.LIVE_STREAM : StreamType.VIDEO_STREAM;
    }

    @Override
    public long getDuration() {
        return item.getLong("duration");
    }

    protected void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
