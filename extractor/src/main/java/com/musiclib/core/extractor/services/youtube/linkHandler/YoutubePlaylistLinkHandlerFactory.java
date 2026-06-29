package com.musiclib.core.extractor.services.youtube.linkHandler;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.LinkHandler;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.services.youtube.YoutubeParsingHelper;
import com.musiclib.core.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.musiclib.core.extractor.services.youtube.StringObfuscator;

public final class YoutubePlaylistLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final YoutubePlaylistLinkHandlerFactory INSTANCE =
            new YoutubePlaylistLinkHandlerFactory();

    private YoutubePlaylistLinkHandlerFactory() {
    }

    public static YoutubePlaylistLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String id, final List<String> contentFilters,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x2E,0x32,0x3F,0x27,0x32,0x37,0x2D,0x2A,0x61,0x32,0x37,0x2D,0x2A,0x63}) + id;
// = "https://www.youtube.com/playlist?list="
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        try {
            final URL urlObj = Utils.stringToURL(url);

            if (!Utils.isHTTP(urlObj) || !(YoutubeParsingHelper.isYoutubeURL(urlObj)
                    || YoutubeParsingHelper.isInvidiousURL(urlObj))) {
                throw new ParsingException(StringObfuscator.decode(new int[]{42, 54, 59, 126, 43, 44, 50, 126, 57, 55, 40, 59, 48, 126, 55, 45, 126, 48, 49, 42, 126, 63, 126, 7, 49, 43, 10, 43, 60, 59, 115, 11, 12, 18}));
            }

            final String path = urlObj.getPath();
            if (!path.equals("/watch") && !path.equals("/playlist")) {
                throw new ParsingException("the url given is neither a video nor a playlist URL");
            }

            final String listID = Utils.getQueryValue(urlObj, "list");

            if (listID == null) {
                throw new ParsingException("the URL given does not include a playlist");
            }

            if (!listID.matches("[a-zA-Z0-9_-]{10,}")) {
                throw new ParsingException(
                        "the list-ID given in the URL does not match the list pattern");
            }

            return listID;
        } catch (final Exception exception) {
            throw new ParsingException("Error could not parse URL: " + exception.getMessage(),
                    exception);
        }
    }

    @Override
    public boolean onAcceptUrl(final String url) {
        try {
            getId(url);
        } catch (final ParsingException e) {
            return false;
        }
        return true;
    }

    /**
     * If it is a mix (auto-generated playlist) URL, return a {@link LinkHandler} where the URL is
     * like {@code https://youtube.com/watch?v=videoId&list=playlistId}
     * <p>Otherwise use super</p>
     */
    @Override
    public ListLinkHandler fromUrl(final String url) throws ParsingException {
        try {
            final URL urlObj = Utils.stringToURL(url);
            final String listID = Utils.getQueryValue(urlObj, "list");
            if (listID != null && YoutubeParsingHelper.isYoutubeMixId(listID)) {
                String videoID = Utils.getQueryValue(urlObj, "v");
                if (videoID == null) {
                    videoID = YoutubeParsingHelper.extractVideoIdFromMixId(listID);
                }
               final String newUrl = StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x29,0x3F,0x2A,0x3D,0x36,0x61,0x28,0x63}) + videoID
    + "&list=" + listID;
// = "https://www.youtube.com/watch?v="
                return new ListLinkHandler(new LinkHandler(url, newUrl, listID));
            }
        } catch (final MalformedURLException exception) {
            throw new ParsingException("Error could not parse URL: " + exception.getMessage(),
                exception);
        }
        return super.fromUrl(url);
    }
}
