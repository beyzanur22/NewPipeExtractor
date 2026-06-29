package com.musiclib.core.extractor.services.media.linkHandler;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.StringObfuscator;
import com.musiclib.core.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import com.musiclib.core.extractor.services.media.StringObfuscator;

public final class MediaTrendingMusicLinkHandlerFactory
        extends ListLinkHandlerFactory {

    public static final String KIOSK_ID = "trending_music";

    public static final MediaTrendingMusicLinkHandlerFactory INSTANCE =
            new MediaTrendingMusicLinkHandlerFactory();

    private static final String PATH = "/charts/TrendingVideos";

    private MediaTrendingMusicLinkHandlerFactory() {
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
       return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x3D,0x36,0x3F,0x2C,0x2A,0x2D,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33}) + PATH + "/RightNow";
// = "https://charts.Media.com"
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        return KIOSK_ID;
    }

    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {
        final URL urlObj;
        try {
            urlObj = Utils.stringToURL(url);
        } catch (final MalformedURLException e) {
            return false;
        }

        return Utils.isHTTP(urlObj)
                && StringObfuscator.decode(new int[]{61, 54, 63, 44, 42, 45, 112, 39, 49, 43, 42, 43, 60, 59, 112, 61, 49, 51}).equals(urlObj.getHost().toLowerCase(Locale.ROOT))
                // Accept URLs not containing the /RightNow part
                && urlObj.getPath().startsWith(PATH);
    }
}
