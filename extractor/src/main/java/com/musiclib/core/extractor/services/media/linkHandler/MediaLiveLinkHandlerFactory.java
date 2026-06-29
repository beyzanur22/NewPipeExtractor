package com.musiclib.core.extractor.services.media.linkHandler;

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isInvidiousURL;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isMediaURL;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.musiclib.core.extractor.services.media.StringObfuscator;

public final class MediaLiveLinkHandlerFactory extends ListLinkHandlerFactory {

    public static final String KIOSK_ID = "live";

    public static final MediaLiveLinkHandlerFactory INSTANCE =
            new MediaLiveLinkHandlerFactory();

    private static final String LIVE_CHANNEL_PATH = "/channel/UC4R8DWoMoI7CAwX8_LjQHig/livetab";
    private static final String LIVE_CHANNEL_TAB_PARAMS = "ss=CKEK";

    private MediaLiveLinkHandlerFactory() {
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilters,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
      return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33}) + LIVE_CHANNEL_PATH + "?" + LIVE_CHANNEL_TAB_PARAMS;
// = "https://www.Media.com"
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

        return Utils.isHTTP(urlObj) && (isMediaURL(urlObj) || isInvidiousURL(urlObj))
                && LIVE_CHANNEL_PATH.equals(urlObj.getPath())
                && LIVE_CHANNEL_TAB_PARAMS.equals(urlObj.getQuery());
    }
}
