package com.musiclib.core.extractor.services.youtube.linkHandler;

import static com.musiclib.core.extractor.services.youtube.YoutubeParsingHelper.isInvidiousURL;
import static com.musiclib.core.extractor.services.youtube.YoutubeParsingHelper.isYoutubeURL;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.musiclib.core.extractor.services.youtube.StringObfuscator;

public final class YoutubeTrendingGamingVideosLinkHandlerFactory extends ListLinkHandlerFactory {

    public static final String KIOSK_ID = "trending_gaming";

    public static final YoutubeTrendingGamingVideosLinkHandlerFactory INSTANCE =
            new YoutubeTrendingGamingVideosLinkHandlerFactory();

    private YoutubeTrendingGamingVideosLinkHandlerFactory() {
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilters,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
     return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x39,0x3F,0x33,0x37,0x30,0x39,0x71,0x2A,0x2C,0x3B,0x30,0x3A,0x37,0x30,0x39});
// = "https://www.youtube.com/gaming/trending"
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        return KIOSK_ID;
    }

    @Override
    public boolean onAcceptUrl(final String url) {
        final URL urlObj;
        try {
            urlObj = Utils.stringToURL(url);
        } catch (final MalformedURLException e) {
            return false;
        }

        return Utils.isHTTP(urlObj) && (isYoutubeURL(urlObj) || isInvidiousURL(urlObj))
                && "/gaming/trending".equals(urlObj.getPath());
    }
}
