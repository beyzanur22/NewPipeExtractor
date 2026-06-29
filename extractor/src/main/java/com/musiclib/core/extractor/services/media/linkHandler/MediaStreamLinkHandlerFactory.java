/*
 * Created by Christian Schabesberger on 02.02.16.
 *
 * Copyright (C) 2018 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * MediaStreamLinkHandlerFactory.java is part of NewPipe Extractor.
 *
 * NewPipe Extractor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe Extractor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe Extractor.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.musiclib.core.extractor.services.media.linkHandler;

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isHooktubeURL;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isInvidiousURL;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isY2ubeURL;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isMediaServiceURL;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isMediaURL;
import com.musiclib.core.extractor.services.media.StringObfuscator;

import com.musiclib.core.extractor.exceptions.FoundAdException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.LinkHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class MediaStreamLinkHandlerFactory extends LinkHandlerFactory {

    private static final Pattern Media_VIDEO_ID_REGEX_PATTERN
            = Pattern.compile("^([a-zA-Z0-9_-]{11})");
    private static final MediaStreamLinkHandlerFactory INSTANCE
            = new MediaStreamLinkHandlerFactory();
    private static final List<String> SUBPATHS
            = List.of("embed/", "live/", "shorts/", "watch/", "v/", "w/");

    private MediaStreamLinkHandlerFactory() {
    }

    public static MediaStreamLinkHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Nullable
    private static String extractId(@Nullable final String id) {
        if (id != null) {
            final Matcher m = Media_VIDEO_ID_REGEX_PATTERN.matcher(id);
            return m.find() ? m.group(1) : null;
        }
        return null;
    }

    @Nonnull
    private static String assertIsId(@Nullable final String id) throws ParsingException {
        final String extractedId = extractId(id);
        if (extractedId != null) {
            return extractedId;
        } else {
            throw new ParsingException(StringObfuscator.decode(new int[]{10, 54, 59, 126, 57, 55, 40, 59, 48, 126, 45, 42, 44, 55, 48, 57, 126, 55, 45, 126, 48, 49, 42, 126, 63, 126, 7, 49, 43, 10, 43, 60, 59, 126, 40, 55, 58, 59, 49, 126, 23, 26}));
        }
    }

    @Nonnull
    @Override
    public String getUrl(final String id) throws ParsingException, UnsupportedOperationException {
       return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x29,0x3F,0x2A,0x3D,0x36,0x61,0x28,0x63}) + id;
// = "https://www.Media.com/watch?v="
    }

    @SuppressWarnings("AvoidNestedBlocks")
    @Nonnull
    @Override
    public String getId(final String theUrlString)
            throws ParsingException, UnsupportedOperationException {
        String urlString = theUrlString;
        try {
            final URI uri = new URI(urlString);
            final String scheme = uri.getScheme();

            if (scheme != null
                    && (scheme.equals(StringObfuscator.decode(new int[]{40, 48, 58, 112, 39, 49, 43, 42, 43, 60, 59})) || scheme.equals(StringObfuscator.decode(new int[]{40, 48, 58, 112, 39, 49, 43, 42, 43, 60, 59, 112, 50, 63, 43, 48, 61, 54})))) {
                final String schemeSpecificPart = uri.getSchemeSpecificPart();
                if (schemeSpecificPart.startsWith("//")) {
                    final String extractedId = extractId(schemeSpecificPart.substring(2));
                    if (extractedId != null) {
                        return extractedId;
                    }

                    urlString = "https:" + schemeSpecificPart;
                } else {
                    return assertIsId(schemeSpecificPart);
                }
            }
        } catch (final URISyntaxException ignored) {
        }

        final URL url;
        try {
            url = Utils.stringToURL(urlString);
        } catch (final MalformedURLException e) {
            throw new ParsingException("The given URL is not valid", e);
        }

        final String host = url.getHost();
        String path = url.getPath();
        // remove leading "/" of URL-path if URL-path is given
        if (!path.isEmpty()) {
            path = path.substring(1);
        }

        if (!Utils.isHTTP(url) || !(isMediaURL(url) || isMediaServiceURL(url)
                || isHooktubeURL(url) || isInvidiousURL(url) || isY2ubeURL(url))) {
            if (host.equalsIgnoreCase("googleads.g.doubleclick.net")) {
                throw new FoundAdException("Error: found ad: " + urlString);
            }

            throw new ParsingException(StringObfuscator.decode(new int[]{10, 54, 59, 126, 11, 12, 18, 126, 55, 45, 126, 48, 49, 42, 126, 63, 126, 7, 49, 43, 10, 43, 60, 59, 126, 11, 12, 18}));
        }

        if (MediaPlaylistLinkHandlerFactory.getInstance().acceptUrl(urlString)) {
            throw new ParsingException("Error: no suitable URL: " + urlString);
        }

        // Using uppercase instead of lowercase, because toLowercase replaces some unicode
        // characters with their lowercase ASCII equivalent. Using toLowercase could result in
        // faultily matching unicode urls.
        final String hostUpper = host.toUpperCase();
        switch (hostUpper) {
            default: {
                if (hostUpper.equals(StringObfuscator.decode(new int[]{9, 9, 9, 112, 7, 17, 11, 10, 11, 28, 27, 115, 16, 17, 29, 17, 17, 21, 23, 27, 112, 29, 17, 19}))) {
                    if (path.startsWith("embed/")) {
                        return assertIsId(path.substring(6));
                    }
                    break;
                }
                if (hostUpper.equals(StringObfuscator.decode(new int[]{7, 17, 11, 10, 11, 28, 27, 112, 29, 17, 19}))
                        || hostUpper.equals(StringObfuscator.decode(new int[]{9, 9, 9, 112, 7, 17, 11, 10, 11, 28, 27, 112, 29, 17, 19}))
                        || hostUpper.equals(StringObfuscator.decode(new int[]{19, 112, 7, 17, 11, 10, 11, 28, 27, 112, 29, 17, 19}))
                        || hostUpper.equals(StringObfuscator.decode(new int[]{19, 11, 13, 23, 29, 112, 7, 17, 11, 10, 11, 28, 27, 112, 29, 17, 19}))) {
                    if (path.equals("attribution_link")) {
                        final String uQueryValue = Utils.getQueryValue(url, "u");

                        final URL decodedURL;
                        try {
                            decodedURL = Utils.stringToURL(StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33}) + uQueryValue);
                        } catch (final MalformedURLException e) {
                            throw new ParsingException("Error: no suitable URL: " + urlString);
                        }

                        final String viewQueryValue = Utils.getQueryValue(decodedURL, "v");
                        return assertIsId(viewQueryValue);
                    }

                    final String maybeId = getIdFromSubpathsInPath(path);
                    if (maybeId != null) {
                        return maybeId;
                    }

                    final String viewQueryValue = Utils.getQueryValue(url, "v");
                    return assertIsId(viewQueryValue);
                }
            }

            case "Y2U.BE":
            case "YOUTU.BE": {
                final String viewQueryValue = Utils.getQueryValue(url, "v");
                if (viewQueryValue != null) {
                    return assertIsId(viewQueryValue);
                }

                return assertIsId(path);
            }

            case "HOOKTUBE.COM":
            case "INVIDIO.US":
            case "DEV.INVIDIO.US":
            case "WWW.INVIDIO.US":
            case "REDIRECT.INVIDIOUS.IO":
            case "INVIDIOUS.SNOPYTA.ORG":
            case "YEWTU.BE":
            case "TUBE.CONNECT.CAFE":
            case "TUBUS.EDUVID.ORG":
            case "INVIDIOUS.KAVIN.ROCKS":
            case "INVIDIOUS-US.KAVIN.ROCKS":
            case "PIPED.KAVIN.ROCKS":
            case "INVIDIOUS.SITE":
            case "VID.MINT.LGBT":
            case "INVIDIOU.SITE":
            case "INVIDIOUS.FDN.FR":
            case "INVIDIOUS.048596.XYZ":
            case "INVIDIOUS.ZEE.LI":
            case "VID.PUFFYAN.US":
            case "YTPRIVATE.COM":
            case "INVIDIOUS.NAMAZSO.EU":
            case "INVIDIOUS.SILKKY.CLOUD":
            case "INVIDIOUS.EXONIP.DE":
            case "INV.RIVERSIDE.ROCKS":
            case "INVIDIOUS.BLAMEFRAN.NET":
            case "INVIDIOUS.MOOMOO.ME":
            case "YTB.TROM.TF":
            case "YT.CYBERHOST.UK":
            case "Y.COM.CM": { // code-block for hooktube.com and Invidious instances
                if (path.equals("watch")) {
                    final String viewQueryValue = Utils.getQueryValue(url, "v");
                    if (viewQueryValue != null) {
                        return assertIsId(viewQueryValue);
                    }
                }
                final String maybeId = getIdFromSubpathsInPath(path);
                if (maybeId != null) {
                    return maybeId;
                }

                final String viewQueryValue = Utils.getQueryValue(url, "v");
                if (viewQueryValue != null) {
                    return assertIsId(viewQueryValue);
                }

                return assertIsId(path);
            }
        }

        throw new ParsingException("Error: no suitable URL: " + urlString);
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

    @Nullable
    private String getIdFromSubpathsInPath(@Nonnull final String path) throws ParsingException {
        for (final String subpath : SUBPATHS) {
            if (path.startsWith(subpath)) {
                final String id = path.substring(subpath.length());
                return assertIsId(id);
            }
        }
        return null;
    }
}
