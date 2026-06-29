// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package com.musiclib.core.extractor.services.bandcamp.linkHandler;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.services.bandcamp.extractors.BandcampExtractorHelper;
import com.musiclib.core.extractor.utils.Utils;

import static com.musiclib.core.extractor.services.bandcamp.extractors.BandcampExtractorHelper.BASE_URL;

/**
 * <p>Tracks don't have standalone ids, they are always in combination with the band id.
 * That's why id = url.</p>
 *
 * <p>Radio (bandcamp weekly) shows do have ids.</p>
 */
public final class BandcampStreamLinkHandlerFactory extends LinkHandlerFactory {

    private static final BandcampStreamLinkHandlerFactory INSTANCE
            = new BandcampStreamLinkHandlerFactory();

    private BandcampStreamLinkHandlerFactory() {
    }

    public static BandcampStreamLinkHandlerFactory getInstance() {
        return INSTANCE;
    }


    /**
     * @see BandcampStreamLinkHandlerFactory
     */
    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        if (BandcampExtractorHelper.isRadioUrl(url)) {
            return url.split("bandcamp.com/\\?show=")[1];
        } else {
            return getUrl(url);
        }
    }

    /**
     * Clean up url
     * @see BandcampStreamLinkHandlerFactory
     */
    @Override
    public String getUrl(final String input)
            throws ParsingException, UnsupportedOperationException {
        if (input.matches("\\d+")) {
            return BASE_URL + "/?show=" + input;
        } else {
            return Utils.replaceHttpWithHttps(input);
        }
    }

    /**
     * Accepts URLs that point to a bandcamp radio show or that are a bandcamp
     * domain and point to a track.
     */
    @Override
    public boolean onAcceptUrl(final String url) throws ParsingException {

        // Accept Bandcamp radio
        if (BandcampExtractorHelper.isRadioUrl(url)) {
            return true;
        }

        // Don't accept URLs that don't point to a track
        if (!url.toLowerCase().matches("https?://.+\\..+/track/.+")) {
            return false;
        }

        // Test whether domain is supported
        return BandcampExtractorHelper.isArtistDomain(url);
    }
}
