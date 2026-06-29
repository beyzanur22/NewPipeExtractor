package com.musiclib.core.extractor.services.peertube;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import com.musiclib.core.extractor.NewPipe;
import com.musiclib.core.extractor.downloader.Response;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.exceptions.ReCaptchaException;
import com.musiclib.core.extractor.utils.JsonUtils;
import com.musiclib.core.extractor.utils.Utils;

import java.io.IOException;

public class PeertubeInstance {

    private final String url;
    private String name;
    public static final PeertubeInstance DEFAULT_INSTANCE
            = new PeertubeInstance("https://framatube.org", "FramaTube");

    public PeertubeInstance(final String url) {
        this.url = url;
        this.name = "PeerTube";
    }

    public PeertubeInstance(final String url, final String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void fetchInstanceMetaData() throws Exception {
        final Response response;
        try {
            response = NewPipe.getDownloader().get(url + "/api/v1/config");
        } catch (ReCaptchaException | IOException e) {
            throw new Exception("unable to configure instance " + url, e);
        }

        if (response == null || Utils.isBlank(response.responseBody())) {
            throw new Exception("unable to configure instance " + url);
        }

        try {
            final JsonObject json = JsonParser.object().from(response.responseBody());
            this.name = JsonUtils.getString(json, "instance.name");
        } catch (JsonParserException | ParsingException e) {
            throw new Exception("unable to parse instance config", e);
        }
    }

    public String getName() {
        return name;
    }

}
