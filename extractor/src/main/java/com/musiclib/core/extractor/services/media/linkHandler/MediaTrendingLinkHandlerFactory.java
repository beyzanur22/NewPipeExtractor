/*
 * Created by Christian Schabesberger on 12.08.17.
 *
 * Copyright (C) 2018 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * MediaTrendingLinkHandlerFactory.java is part of NewPipe Extractor.
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

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isInvidiousURL;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.isMediaURL;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.ListLinkHandlerFactory;
import com.musiclib.core.extractor.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.musiclib.core.extractor.services.media.StringObfuscator;

public final class MediaTrendingLinkHandlerFactory extends ListLinkHandlerFactory {

    public static final MediaTrendingLinkHandlerFactory INSTANCE =
            new MediaTrendingLinkHandlerFactory();

    private MediaTrendingLinkHandlerFactory() {
    }

    public String getUrl(final String id,
                         final List<String> contentFilters,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        return StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x38,0x3B,0x3B,0x3A,0x71,0x2A,0x2C,0x3B,0x30,0x3A,0x37,0x30,0x39});
// = "https://www.Media.com/feed/trending"
    }

    @Override
    public String getId(final String url) throws ParsingException, UnsupportedOperationException {
        return "Trending";
    }

    @Override
    public boolean onAcceptUrl(final String url) {
        final URL urlObj;
        try {
            urlObj = Utils.stringToURL(url);
        } catch (final MalformedURLException e) {
            return false;
        }

        final String urlPath = urlObj.getPath();
        return Utils.isHTTP(urlObj) && (isMediaURL(urlObj) || isInvidiousURL(urlObj))
                && urlPath.equals("/feed/trending");
    }
}
