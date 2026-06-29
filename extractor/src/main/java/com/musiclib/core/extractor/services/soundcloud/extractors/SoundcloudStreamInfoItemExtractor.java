package com.musiclib.core.extractor.services.soundcloud.extractors;

import com.grack.nanojson.JsonObject;

import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.localization.DateWrapper;
import com.musiclib.core.extractor.stream.StreamInfoItemExtractor;
import com.musiclib.core.extractor.stream.StreamType;

import javax.annotation.Nonnull;
import java.util.List;

import static com.musiclib.core.extractor.services.soundcloud.SoundcloudParsingHelper.getAllImagesFromArtworkOrAvatarUrl;
import static com.musiclib.core.extractor.services.soundcloud.SoundcloudParsingHelper.getAllImagesFromTrackObject;
import static com.musiclib.core.extractor.services.soundcloud.SoundcloudParsingHelper.parseDate;
import static com.musiclib.core.extractor.utils.Utils.replaceHttpWithHttps;

public class SoundcloudStreamInfoItemExtractor implements StreamInfoItemExtractor {

    private final JsonObject itemObject;

    public SoundcloudStreamInfoItemExtractor(final JsonObject itemObject) {
        this.itemObject = itemObject;
    }

    @Override
    public String getUrl() {
        return replaceHttpWithHttps(itemObject.getString("permalink_url"));
    }

    @Override
    public String getName() {
        return itemObject.getString("title");
    }

    @Override
    public long getDuration() {
        return itemObject.getLong("duration") / 1000L;
    }

    @Override
    public String getUploaderName() {
        return itemObject.getObject("user").getString("username");
    }

    @Override
    public String getUploaderUrl() {
        return replaceHttpWithHttps(itemObject.getObject("user").getString("permalink_url"));
    }

    @Nonnull
    @Override
    public List<Image> getUploaderAvatars() {
        return getAllImagesFromArtworkOrAvatarUrl(
                itemObject.getObject("user").getString("avatar_url"));
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return itemObject.getObject("user").getBoolean("verified");
    }

    @Override
    public String getTextualUploadDate() {
        return itemObject.getString("created_at");
    }

    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        return parseDate(getTextualUploadDate());
    }

    @Override
    public long getViewCount() {
        return itemObject.getLong("playback_count");
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return getAllImagesFromTrackObject(itemObject);
    }

    @Override
    public StreamType getStreamType() {
        return StreamType.AUDIO_STREAM;
    }

    @Override
    public boolean isAd() {
        return false;
    }
}
