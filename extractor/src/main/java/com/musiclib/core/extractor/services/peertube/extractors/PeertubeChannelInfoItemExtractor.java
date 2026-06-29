package com.musiclib.core.extractor.services.peertube.extractors;

import com.grack.nanojson.JsonObject;
import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.ListExtractor;
import com.musiclib.core.extractor.channel.ChannelInfoItemExtractor;
import com.musiclib.core.extractor.exceptions.ParsingException;

import javax.annotation.Nonnull;
import java.util.List;

import static com.musiclib.core.extractor.services.peertube.PeertubeParsingHelper.getAvatarsFromOwnerAccountOrVideoChannelObject;

public class PeertubeChannelInfoItemExtractor implements ChannelInfoItemExtractor {

    private final JsonObject item;
    private final String baseUrl;

    public PeertubeChannelInfoItemExtractor(@Nonnull final JsonObject item,
                                            @Nonnull final String baseUrl) {
        this.item = item;
        this.baseUrl = baseUrl;
    }

    @Override
    public String getName() throws ParsingException {
        return item.getString("displayName");
    }

    @Override
    public String getUrl() throws ParsingException {
        return item.getString("url");
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return getAvatarsFromOwnerAccountOrVideoChannelObject(baseUrl, item);
    }

    @Override
    public String getDescription() throws ParsingException {
        return item.getString("description");
    }

    @Override
    public long getSubscriberCount() throws ParsingException {
        return item.getInt("followersCount");
    }

    @Override
    public long getStreamCount() throws ParsingException {
        return ListExtractor.ITEM_COUNT_UNKNOWN;
    }

    @Override
    public boolean isVerified() throws ParsingException {
        return false;
    }
}
