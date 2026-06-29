package com.musiclib.core.extractor.services.media.extractors;

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getTextFromObject;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getThumbnailsFromInfoItem;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

import com.grack.nanojson.JsonObject;

import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.localization.DateWrapper;
import com.musiclib.core.extractor.services.media.linkHandler.MediaStreamLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamInfoItemExtractor;
import com.musiclib.core.extractor.stream.StreamType;
import com.musiclib.core.extractor.utils.Utils;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link StreamInfoItemExtractor} for Media's {@code reelItemRenderer}s.
 *
 * <p>
 * {@code reelItemRenderer}s were returned on Media for their short-form contents on almost every
 * place and every major client. They provide a limited amount of information and do not provide
 * the exact view count, any uploader info (name, URL, avatar, verified status) and the upload date.
 * </p>
 *
 * <p>
 * At the time this documentation has been updated, they are being replaced by
 * {@code shortsLockupViewModel}s. See {@link MediaShortsLockupInfoItemExtractor} for an
 * extractor for this new UI data type.
 * </p>
 */
public class MediaReelInfoItemExtractor implements StreamInfoItemExtractor {

    @Nonnull
    private final JsonObject reelInfo;

    public MediaReelInfoItemExtractor(@Nonnull final JsonObject reelInfo) {
        this.reelInfo = reelInfo;
    }

    @Override
    public String getName() throws ParsingException {
        return getTextFromObject(reelInfo.getObject("headline"));
    }

    @Override
    public String getUrl() throws ParsingException {
        try {
            final String videoId = reelInfo.getString("videoId");
            return MediaStreamLinkHandlerFactory.getInstance().getUrl(videoId);
        } catch (final Exception e) {
            throw new ParsingException("Could not get URL", e);
        }
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return getThumbnailsFromInfoItem(reelInfo);
    }

    @Override
    public StreamType getStreamType() throws ParsingException {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public long getViewCount() throws ParsingException {
        final String viewCountText = getTextFromObject(reelInfo.getObject("viewCountText"));
        if (!isNullOrEmpty(viewCountText)) {
            // This approach is language dependent
            if (viewCountText.toLowerCase().contains("no views")) {
                return 0;
            }

            return Utils.mixedNumberWordToLong(viewCountText);
        }

        throw new ParsingException("Could not get short view count");
    }

    @Override
    public boolean isShortFormContent() {
        return true;
    }

    // All the following properties cannot be obtained from reelItemRenderers

    @Override
    public boolean isAd() throws ParsingException {
        return false;
    }

    @Override
    public long getDuration() throws ParsingException {
        return -1;
    }

    @Override
    public String getUploaderName() throws ParsingException {
        return null;
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        return null;
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return false;
    }

    @Nullable
    @Override
    public String getTextualUploadDate() throws ParsingException {
        return null;
    }

    @Nullable
    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        return null;
    }
}
