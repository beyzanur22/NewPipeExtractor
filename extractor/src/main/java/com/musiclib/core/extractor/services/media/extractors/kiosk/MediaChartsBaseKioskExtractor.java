package com.musiclib.core.extractor.services.media.extractors.kiosk;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonWriter;
import com.musiclib.core.extractor.Image;
import com.musiclib.core.extractor.Page;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.downloader.Downloader;
import com.musiclib.core.extractor.exceptions.ExtractionException;
import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.kiosk.KioskExtractor;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;
import com.musiclib.core.extractor.localization.ContentCountry;
import com.musiclib.core.extractor.localization.DateWrapper;
import com.musiclib.core.extractor.localization.Localization;
import com.musiclib.core.extractor.services.media.InnertubeClientRequestInfo;
import com.musiclib.core.extractor.services.media.StringObfuscator;
import com.musiclib.core.extractor.services.media.linkHandler.MediaChannelLinkHandlerFactory;
import com.musiclib.core.extractor.services.media.linkHandler.MediaStreamLinkHandlerFactory;
import com.musiclib.core.extractor.stream.StreamInfoItem;
import com.musiclib.core.extractor.stream.StreamInfoItemExtractor;
import com.musiclib.core.extractor.stream.StreamInfoItemsCollector;
import com.musiclib.core.extractor.stream.StreamType;
import com.musiclib.core.extractor.utils.JsonUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.musiclib.core.extractor.services.media.MediaParsingHelper.DISABLE_PRETTY_PRINT_PARAMETER;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getClientHeaders;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getOriginReferrerHeaders;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getThumbnailsFromInfoItem;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.getValidJsonResponseBody;
import static com.musiclib.core.extractor.services.media.MediaParsingHelper.prepareJsonBuilder;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

/**
 * Base class parsing responses from Media Charts for all trending video charts.
 *
 * <p>
 * Note: Media Charts isn't officially supported in all Media supported countries (there are
 * fewer countries in the {@code LAUNCHED_CHART_COUNTRIES} array of Media Charts' HTML responses
 * than in the Media country selector).
 * </p>
 *
 * <p>
 * For some trends, some videos are still returned in unsupported countries, even if there are
 * fewer than in a supported country, for others an HTTP 400 error is returned saying
 * {@code Request contains an invalid argument.}.
 * </p>
 */
abstract class MediaChartsBaseKioskExtractor extends KioskExtractor<StreamInfoItem> {

    // Extracted from Media Charts' HTML, in the array named LAUNCHED_CHART_COUNTRIES
    protected static final Set<String> YT_CHARTS_SUPPORTED_COUNTRY_CODES = Set.of(
            "AE", "AR", "AT", "AU", "BE", "BO", "BR", "CA", "CH", "CL", "CO", "CR", "CZ", "DE",
            "DK", "DO", "EC", "EE", "EG", "ES", "FI", "FR", "GB", "GT", "HN", "HU", "ID", "IE",
            "IL", "IN", "IS", "IT", "JP", "KE", "KR", "LU", "MX", "NG", "NI", "NL", "NO", "NZ",
            "PA", "PE", "PL", "PT", "PY", "RO", "RS", "RU", "SA", "SE", "SV", "TR", "TZ", "UA",
            "UG", "US", "UY", "ZA", "ZW");

    protected static final String YT_CHARTS_ENDPOINT =
            StringObfuscator.decode(new int[]{54, 42, 42, 46, 45, 100, 113, 113, 61, 54, 63, 44, 42, 45, 112, 39, 49, 43, 42, 43, 60, 59, 112, 61, 49, 51, 113, 39, 49, 43, 42, 43, 60, 59, 55, 113, 40, 111, 113, 60, 44, 49, 41, 45, 59, 97, 63, 50, 42, 99, 52, 45, 49, 48, 120})
                    + DISABLE_PRETTY_PRINT_PARAMETER;

    protected final String chartType;
    protected JsonObject browseResponse;

    protected MediaChartsBaseKioskExtractor(final StreamingService streamingService,
                                              final ListLinkHandler linkHandler,
                                              final String kioskId,
                                              final String chartType) {
        super(streamingService, linkHandler, kioskId);
        this.chartType = chartType;
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        final Localization localization = getExtractorLocalization();
        final ContentCountry contentCountry = getExtractorContentCountry();

        final InnertubeClientRequestInfo innertubeClientRequestInfo =
                InnertubeClientRequestInfo.ofWebMusicAnalyticsChartsClient();

        final byte[] body = JsonWriter.string(prepareJsonBuilder(getExtractorLocalization(),
                contentCountry, innertubeClientRequestInfo, null)
                .value("browseId", "FEmusic_analytics_charts_home")
                .value("query", "perspective=CHART_DETAILS&chart_params_country_code="
                        + contentCountry.getCountryCode() + "&chart_params_chart_type="
                        + chartType)
                .done())
                .getBytes(StandardCharsets.UTF_8);

        final var headers = new HashMap<>(getOriginReferrerHeaders(StringObfuscator.decode(new int[]{54, 42, 42, 46, 45, 100, 113, 113, 61, 54, 63, 44, 42, 45, 112, 39, 49, 43, 42, 43, 60, 59, 112, 61, 49, 51})));
        headers.putAll(getClientHeaders(innertubeClientRequestInfo.clientInfo.clientId,
                innertubeClientRequestInfo.clientInfo.clientVersion));

        browseResponse = JsonUtils.toJsonObject(getValidJsonResponseBody(
                getDownloader().postWithContentTypeJson(
                        YT_CHARTS_ENDPOINT, headers, body, localization)));
    }

    @Nonnull
    @Override
    public abstract String getName() throws ParsingException;

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        final JsonArray videos = browseResponse.getObject("contents")
                .getObject("sectionListRenderer")
                .getArray("contents")
                .getObject(0)
                .getObject("musicAnalyticsSectionRenderer")
                .getObject("content")
                .getArray("videos")
                .getObject(0)
                .getArray("videoViews");

        final StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());

        videos.streamAsJsonObjects()
                .forEachOrdered(video -> collector.commit(
                        new MediaChartsVideoInfoItemExtractor(video)));

        return new InfoItemsPage<>(collector, null);
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page) {
        // There is no continuation in charts
        return InfoItemsPage.emptyPage();
    }

    static final class MediaChartsVideoInfoItemExtractor
            implements StreamInfoItemExtractor {

        @Nonnull
        private final JsonObject videoObject;

        MediaChartsVideoInfoItemExtractor(@Nonnull final JsonObject videoObject) {
            this.videoObject = videoObject;
        }

        @Override
        public StreamType getStreamType() {
            // There are only video streams in Media Charts, at least for now
            return StreamType.VIDEO_STREAM;
        }

        @Override
        public boolean isAd() {
            return false;
        }

        @Override
        public long getDuration() {
            return videoObject.getInt("videoDuration", -1);
        }

        @Override
        public long getViewCount() {
            // View counts aren't returned, at least for now
            return -1;
        }

        @Override
        public String getUploaderName() {
            return videoObject.getString("channelName");
        }

        @Override
        public String getUploaderUrl() throws ParsingException {
            final String channelId = videoObject.getString("externalChannelId");

            if (isNullOrEmpty(channelId)) {
                throw new ParsingException("Could not get channel ID");
            }

            return MediaChannelLinkHandlerFactory.getInstance().getUrl("channel/" + channelId);
        }

        @Override
        public boolean isUploaderVerified() {
            // We don't have any info on this, at least for now
            return false;
        }

        @Nullable
        @Override
        public String getTextualUploadDate() {
            return null;
        }

        @Nonnull
        @Override
        public DateWrapper getUploadDate() {
            final var releaseDate = videoObject.getObject("releaseDate");
            final var localDate = LocalDate.of(releaseDate.getInt("year"),
                    releaseDate.getInt("month"), releaseDate.getInt("day"));
            // We request that times should be returned with 0 offset to UTC timezone in
            // the JSON body, but Media charts does it only in its HTTP headers
            final var instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();

            // We don't have more info than the release day, hence isApproximate=true
            return new DateWrapper(instant, true);
        }

        @Override
        public String getName() {
            return videoObject.getString("title");
        }

        @Override
        public String getUrl() throws ParsingException {
            return MediaStreamLinkHandlerFactory.getInstance().getUrl(
                    videoObject.getString("id"));
        }

        @Nonnull
        @Override
        public List<Image> getThumbnails() throws ParsingException {
            return getThumbnailsFromInfoItem(videoObject);
        }
    }
}
