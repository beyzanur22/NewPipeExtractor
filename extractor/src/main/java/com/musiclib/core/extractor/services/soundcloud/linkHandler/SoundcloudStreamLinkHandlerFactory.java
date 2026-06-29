package com.musiclib.core.extractor.services.soundcloud.linkHandler;

import java.util.regex.Pattern;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.services.soundcloud.SoundcloudParsingHelper;
import com.musiclib.core.extractor.utils.Parser;
import com.musiclib.core.extractor.utils.Utils;

public final class SoundcloudStreamLinkHandlerFactory extends LinkHandlerFactory {
    private static final SoundcloudStreamLinkHandlerFactory INSTANCE
            = new SoundcloudStreamLinkHandlerFactory();

    private static final String ON_URL_PATTERN = "^https?://on\\.soundcloud\\.com/[0-9a-zA-Z]+$";
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^https?://(?:www\\.|m\\.)?"
        + "soundcloud.com/[0-9a-z_-]+"
        + "/(?!(?:tracks|albums|sets|reposts|followers|following)/?$)[0-9a-z_-]+/?(?:[#?].*)?$"
        + "|" + ON_URL_PATTERN
        );

    private static final Pattern API_URL_PATTERN = Pattern.compile(
        "^https?://api-v2\\.soundcloud.com"
        + "/(tracks|albums|sets|reposts|followers|following)/([0-9a-z_-]+)/"
        );

    private SoundcloudStreamLinkHandlerFactory() {
    }

    public static SoundcloudStreamLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String id) throws ParsingException, UnsupportedOperationException {
        try {
            return SoundcloudParsingHelper.resolveUrlWithEmbedPlayer(
                    "https://api.soundcloud.com/tracks/" + id);
        } catch (final Exception e) {
            throw new ParsingException(e.getMessage(), e);
        }
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        if (Parser.isMatch(API_URL_PATTERN, url)) {
            return Parser.matchGroup1(API_URL_PATTERN, url);
        }
        Utils.checkUrl(URL_PATTERN, url);

        try {
            return SoundcloudParsingHelper.resolveIdWithWidgetApi(url);
        } catch (final Exception e) {
            throw new ParsingException(e.getMessage(), e);
        }
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        return Parser.isMatch(URL_PATTERN, url.toLowerCase());
    }
}
