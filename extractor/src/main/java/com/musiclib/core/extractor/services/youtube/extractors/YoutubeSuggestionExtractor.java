/*
 * Created by Christian Schabesberger on 28.09.16.
 *
 * Copyright (C) 2015 Christian Schabesberger <chris.schabesberger@mailbox.org>
 * YoutubeSuggestionExtractor.java is part of NewPipe Extractor.
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

package com.musiclib.core.extractor.services.youtube.extractors;

import static com.musiclib.core.extractor.utils.Utils.isBlank;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import com.musiclib.core.extractor.NewPipe;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.downloader.Response;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;
import com.musiclib.core.extractor.utils.Utils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.musiclib.core.extractor.services.youtube.StringObfuscator;

public class YoutubeSuggestionExtractor extends SuggestionExtractor {

    public YoutubeSuggestionExtractor(final StreamingService service) {
        super(service);
    }

      @Override
    public List<String> suggestionList(final String query) throws IOException, ExtractionException {
        final String url = StringObfuscator.decode(new int[]{
            0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x2D,0x2B,0x39,0x39,
            0x3B,0x2D,0x2A,0x2F,0x2B,0x3B,0x2C,0x37,0x3B,0x2D,0x73,0x3D,
            0x32,0x37,0x3B,0x30,0x2A,0x2D,0x68,0x70,0x27,0x31,0x2B,0x2A,
            0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x3D,0x31,0x33,0x2E,
            0x32,0x3B,0x2A,0x3B,0x71,0x2D,0x3B,0x3F,0x2C,0x3D,0x36
        })
                + "?client=" + StringObfuscator.decode(new int[]{0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B})
                + "&ds=" + "yt"
                + "&gl=" + Utils.encodeUrlUtf8(getExtractorContentCountry().getCountryCode())
                + "&q=" + Utils.encodeUrlUtf8(query)
                + "&xhr=t";

        final Map<String, List<String>> headers = new HashMap<>();
     headers.put("Origin", Collections.singletonList(StringObfuscator.decode(new int[]{
    0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,
    0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33
})));
headers.put("Referer", Collections.singletonList(StringObfuscator.decode(new int[]{
    0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,
    0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33
})));

        final Response response = NewPipe.getDownloader()
                .get(url, headers, getExtractorLocalization());

        final String contentTypeHeader = response.getHeader("Content-Type");
        if (isNullOrEmpty(contentTypeHeader) || !contentTypeHeader.contains("application/json")) {
            throw new ExtractionException("Invalid response type (got \"" + contentTypeHeader
                    + "\", excepted a JSON response) (response code "
                    + response.responseCode() + ")");
        }

        final String responseBody = response.responseBody();

        if (responseBody.isEmpty()) {
            throw new ExtractionException("Empty response received");
        }

        try {
            final JsonArray suggestions = JsonParser.array()
                    .from(responseBody)
                    .getArray(1); // 0: search query, 1: search suggestions, 2: tracking data?
            return suggestions.stream()
                    .filter(JsonArray.class::isInstance)
                    .map(JsonArray.class::cast)
                    .map(suggestion -> suggestion.getString(0)) // 0 is the search suggestion
                    .filter(suggestion -> !isBlank(suggestion)) // Filter blank suggestions
                    .collect(Collectors.toUnmodifiableList());
        } catch (final JsonParserException e) {
            throw new ParsingException("Could not parse JSON response", e);
        }
    }
}
