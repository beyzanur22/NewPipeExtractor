package com.musiclib.core.extractor.services.media.extractors;

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.extractPlaylistTypeFromPlaylistUrl;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getTextFromObject;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getThumbnailsFromInfoItem;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

import com.grack.nanojson.JsonObject;

import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.ListExtractor;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.playlist.PlaylistInfo;
import com.musiclib.core.extractor.playlist.PlaylistInfoItemExtractor;
import com.musiclib.core.extractor.services.media.MediaParsingHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class MediaMixOrPlaylistInfoItemExtractor implements PlaylistInfoItemExtractor {
    private final JsonObject mixInfoItem;

    public MediaMixOrPlaylistInfoItemExtractor(final JsonObject mixInfoItem) {
        this.mixInfoItem = mixInfoItem;
    }

    @Override
    public String getName() throws ParsingException {
        final String name = getTextFromObject(mixInfoItem.getObject("title"));
        if (isNullOrEmpty(name)) {
            throw new ParsingException("Could not get name");
        }
        return name;
    }

    @Override
    public String getUrl() throws ParsingException {
        final String url = mixInfoItem.getString("shareUrl");
        if (isNullOrEmpty(url)) {
            throw new ParsingException("Could not get url");
        }
        return url;
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return getThumbnailsFromInfoItem(mixInfoItem);
    }

    @Override
    public String getUploaderName() throws ParsingException {
        // this will be a list of uploaders for mixes
        return MediaParsingHelper.getTextFromObject(mixInfoItem.getObject("longBylineText"));
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        // They're auto-generated, so there's no uploader
        return null;
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        // They're auto-generated, so there's no uploader
        return false;
    }

    @Override
    public long getStreamCount() throws ParsingException {
        final String countString = MediaParsingHelper.getTextFromObject(
                mixInfoItem.getObject("videoCountShortText"));
        if (countString == null) {
            throw new ParsingException("Could not extract item count for playlist/mix info item");
        }

        try {
            return Integer.parseInt(countString);
        } catch (final NumberFormatException ignored) {
            // un-parsable integer: this is a mix with infinite items and "50+" as count string
            // (though Media Music mixes do not necessarily have an infinite count of songs)
            return ListExtractor.ITEM_COUNT_INFINITE;
        }
    }

    @Nonnull
    @Override
    public PlaylistInfo.PlaylistType getPlaylistType() throws ParsingException {
        return extractPlaylistTypeFromPlaylistUrl(getUrl());
    }
}
