package com.musiclib.core.extractor.services.media.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;

import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.playlist.PlaylistInfoItemExtractor;
import com.musiclib.core.extractor.services.media.MediaParsingHelper;
import com.musiclib.core.extractor.services.media.linkHandler.MediaPlaylistLinkHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import javax.annotation.Nonnull;
import java.util.List;

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getImagesFromThumbnailsArray;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getTextFromObject;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getUrlFromObject;

public class MediaPlaylistInfoItemExtractor implements PlaylistInfoItemExtractor {
    private final JsonObject playlistInfoItem;

    public MediaPlaylistInfoItemExtractor(final JsonObject playlistInfoItem) {
        this.playlistInfoItem = playlistInfoItem;
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        try {
            JsonArray thumbnails = playlistInfoItem.getArray("thumbnails")
                    .getObject(0)
                    .getArray("thumbnails");
            if (thumbnails.isEmpty()) {
                thumbnails = playlistInfoItem.getObject("thumbnail")
                        .getArray("thumbnails");
            }

            return getImagesFromThumbnailsArray(thumbnails);
        } catch (final Exception e) {
            throw new ParsingException("Could not get thumbnails", e);
        }
    }

    @Override
    public String getName() throws ParsingException {
        try {
            return getTextFromObject(playlistInfoItem.getObject("title"));
        } catch (final Exception e) {
            throw new ParsingException("Could not get name", e);
        }
    }

    @Override
    public String getUrl() throws ParsingException {
        try {
            final String id = playlistInfoItem.getString("playlistId");
            return MediaPlaylistLinkHandlerFactory.getInstance().getUrl(id);
        } catch (final Exception e) {
            throw new ParsingException("Could not get url", e);
        }
    }

    @Override
    public String getUploaderName() throws ParsingException {
        try {
            return getTextFromObject(playlistInfoItem.getObject("longBylineText"));
        } catch (final Exception e) {
            throw new ParsingException("Could not get uploader name", e);
        }
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        try {
            return getUrlFromObject(playlistInfoItem.getObject("longBylineText"));
        } catch (final Exception e) {
            throw new ParsingException("Could not get uploader url", e);
        }
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        try {
            return MediaParsingHelper.isVerified(playlistInfoItem.getArray("ownerBadges"));
        } catch (final Exception e) {
            throw new ParsingException("Could not get uploader verification info", e);
        }
    }

    @Override
    public long getStreamCount() throws ParsingException {
        String videoCountText = playlistInfoItem.getString("videoCount");
        if (videoCountText == null) {
            videoCountText = getTextFromObject(playlistInfoItem.getObject("videoCountText"));
        }

        if (videoCountText == null) {
            videoCountText = getTextFromObject(playlistInfoItem.getObject("videoCountShortText"));
        }

        if (videoCountText == null) {
            throw new ParsingException("Could not get stream count");
        }

        try {
            return Long.parseLong(Utils.removeNonDigitCharacters(videoCountText));
        } catch (final Exception e) {
            throw new ParsingException("Could not get stream count", e);
        }
    }
}
