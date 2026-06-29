package com.musiclib.core.extractor.services.media.linkHandler;

import com.musiclib.core.extractor.exceptions.FoundAdException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;

import java.util.List;
import com.musiclib.core.extractor.services.media.StringObfuscator;

public final class MediaCommentsLinkHandlerFactory extends ListLinkHandlerFactory {

    private static final MediaCommentsLinkHandlerFactory INSTANCE
            = new MediaCommentsLinkHandlerFactory();

    private MediaCommentsLinkHandlerFactory() {
    }

    public static MediaCommentsLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String id) throws ParsingException, UnsupportedOperationException {
       return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x29,0x3F,0x2A,0x3D,0x36,0x61,0x28,0x63}) + id;
// = "https://www.Media.com/watch?v="
    }

    @Override
    public String getId(final String urlString)
            throws ParsingException, UnsupportedOperationException {
        // We need the same id, avoids duplicate code
        return MediaStreamLinkHandlerFactory.getInstance().getId(urlString);
    }

    @Override
    public boolean onAcceptUrl(final String url) throws FoundAdException {
        try {
            getId(url);
            return true;
        } catch (final FoundAdException fe) {
            throw fe;
        } catch (final ParsingException e) {
            return false;
        }
    }

    @Override
    public String getUrl(final String id,
                         final List<String> contentFilter,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return getUrl(id);
    }
}
