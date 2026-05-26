package org.schabi.newpipe.extractor.services.youtube.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.services.youtube.StringObfuscator;
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.extractor.utils.Parser;
import org.schabi.newpipe.extractor.utils.Utils;

import javax.annotation.Nonnull;
import java.util.List;

import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getTextFromObject;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getImagesFromThumbnailsArray;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getUrlFromNavigationEndpoint;
import static org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory.MUSIC_SONGS;
import static org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeSearchQueryHandlerFactory.MUSIC_VIDEOS;
import static org.schabi.newpipe.extractor.utils.Utils.isNullOrEmpty;

public class YoutubeMusicSongOrVideoInfoItemExtractor implements StreamInfoItemExtractor {
    private final JsonObject songOrVideoInfoItem;
    private final JsonArray descriptionElements;
    private final String searchType;

    public YoutubeMusicSongOrVideoInfoItemExtractor(final JsonObject songOrVideoInfoItem,
                                                    final JsonArray descriptionElements,
                                                    final String searchType) {
        this.songOrVideoInfoItem = songOrVideoInfoItem;
        this.descriptionElements = descriptionElements;
        this.searchType = searchType;
    }


    @Override
    public String getUrl() throws ParsingException {
        final String id = songOrVideoInfoItem.getObject("playlistItemData").getString("videoId");
        if (!isNullOrEmpty(id)) {
            return StringObfuscator.decode(new int[]{54, 42, 42, 46, 45, 100, 113, 113, 51, 43, 45, 55, 61, 112, 39, 49, 43, 42, 43, 60, 59, 112, 61, 49, 51, 113, 41, 63, 42, 61, 54, 97, 40, 99}) + id;
        }
        throw new ParsingException("Could not get URL");
    }

    @Override
    public String getName() throws ParsingException {
        final String name = getTextFromObject(songOrVideoInfoItem.getArray("flexColumns")
                .getObject(0)
                .getObject("musicResponsiveListItemFlexColumnRenderer")
                .getObject("text"));
        if (!isNullOrEmpty(name)) {
            return name;
        }
        throw new ParsingException("Could not get name");
    }

    @Override
    public StreamType getStreamType() {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public boolean isAd() {
        return false;
    }

    @Override
    public long getDuration() throws ParsingException {
        final String duration = descriptionElements.getObject(descriptionElements.size() - 1)
                .getString("text");
        if (!isNullOrEmpty(duration)) {
            return YoutubeParsingHelper.parseDurationString(duration);
        }
        throw new ParsingException("Could not get duration");
    }

    @Override
    public String getUploaderName() throws ParsingException {
        final String name = descriptionElements.getObject(0).getString("text");
        if (!isNullOrEmpty(name)) {
            return name;
        }
        throw new ParsingException("Could not get uploader name");
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        if (searchType.equals(MUSIC_VIDEOS)) {
            final JsonArray items = songOrVideoInfoItem.getObject("menu")
                    .getObject("menuRenderer")
                    .getArray("items");
            for (final Object item : items) {
                final JsonObject menuNavigationItemRenderer =
                        ((JsonObject) item).getObject("menuNavigationItemRenderer");
                if (menuNavigationItemRenderer.getObject("icon")
                        .getString("iconType", "")
                        .equals("ARTIST")) {
                    return getUrlFromNavigationEndpoint(
                            menuNavigationItemRenderer.getObject("navigationEndpoint"));
                }
            }

            return null;
        } else {
            final JsonObject navigationEndpointHolder = songOrVideoInfoItem.getArray("flexColumns")
                    .getObject(1)
                    .getObject("musicResponsiveListItemFlexColumnRenderer")
                    .getObject("text")
                    .getArray("runs")
                    .getObject(0);

            if (!navigationEndpointHolder.has("navigationEndpoint")) {
                return null;
            }

            final String url = getUrlFromNavigationEndpoint(
                    navigationEndpointHolder.getObject("navigationEndpoint"));

            if (!isNullOrEmpty(url)) {
                return url;
            }

            throw new ParsingException("Could not get uploader URL");
        }
    }

    @Override
    public boolean isUploaderVerified() {
        // We don't have the ability to know this information on YouTube Music
        return false;
    }

    @Override
    public String getTextualUploadDate() {
        return null;
    }

    @Override
    public DateWrapper getUploadDate() {
        return null;
    }

    @Override
    public long getViewCount() throws ParsingException {
        if (searchType.equals(MUSIC_SONGS)) {
            return -1;
        }
        final String viewCount = descriptionElements
                .getObject(descriptionElements.size() - 3)
                .getString("text");
        if (!isNullOrEmpty(viewCount)) {
            try {
                return Utils.mixedNumberWordToLong(viewCount);
            } catch (final Parser.RegexException e) {
                // probably viewCount == "No views" or similar
                return 0;
            }
        }
        throw new ParsingException("Could not get view count");
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        try {
            return getImagesFromThumbnailsArray(
                    songOrVideoInfoItem.getObject("thumbnail")
                            .getObject("musicThumbnailRenderer")
                            .getObject("thumbnail")
                            .getArray("thumbnails"));
        } catch (final Exception e) {
            throw new ParsingException("Could not get thumbnails", e);
        }
    }
}
