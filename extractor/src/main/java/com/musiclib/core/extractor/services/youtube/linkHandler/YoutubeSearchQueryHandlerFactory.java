package com.musiclib.core.extractor.services.youtube.linkHandler;

import static com.musiclib.core.extractor.utils.Utils.encodeUrlUtf8;
import static com.musiclib.core.extractor.utils.Utils.isNullOrEmpty;

import com.musiclib.core.extractor.exceptions.ParsingException;
import com.musiclib.core.extractor.linkhandler.SearchQueryHandlerFactory;

import java.util.List;

import javax.annotation.Nonnull;
import com.musiclib.core.extractor.services.youtube.StringObfuscator;

public final class YoutubeSearchQueryHandlerFactory extends SearchQueryHandlerFactory {

    private static final YoutubeSearchQueryHandlerFactory INSTANCE =
            new YoutubeSearchQueryHandlerFactory();

    public static final String ALL = "all";
    public static final String VIDEOS = "videos";
    public static final String CHANNELS = "channels";
    public static final String PLAYLISTS = "playlists";

    public static final String MUSIC_SONGS = "music_songs";
    public static final String MUSIC_VIDEOS = "music_videos";
    public static final String MUSIC_ALBUMS = "music_albums";
    public static final String MUSIC_PLAYLISTS = "music_playlists";
    public static final String MUSIC_ARTISTS = "music_artists";

   private static final String SEARCH_URL = StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x29,0x29,0x29,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x2C,0x3B,0x2D,0x2B,0x32,0x2A,0x2D,0x61,0x2D,0x3B,0x3F,0x2C,0x3D,0x36,0x01,0x2F,0x2B,0x3B,0x2C,0x27,0x63});
// = "https://www.youtube.com/results?search_query="
   private static final String MUSIC_SEARCH_URL = StringObfuscator.decode(new int[]{0x36,0x2A,0x2A,0x2E,0x2D,0x64,0x71,0x71,0x33,0x2B,0x2D,0x37,0x3D,0x70,0x27,0x31,0x2B,0x2A,0x2B,0x3C,0x3B,0x70,0x3D,0x31,0x33,0x71,0x2D,0x3B,0x3F,0x2C,0x3D,0x36,0x61,0x2F,0x63});
// = "https://music.youtube.com/search?q="

    @Nonnull
    public static YoutubeSearchQueryHandlerFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public String getUrl(final String searchString,
                         @Nonnull final List<String> contentFilters,
                         final String sortFilter)
            throws ParsingException, UnsupportedOperationException {
        final String contentFilter = !contentFilters.isEmpty() ? contentFilters.get(0) : "";
        switch (contentFilter) {
            case VIDEOS:
                return SEARCH_URL + encodeUrlUtf8(searchString) + "&sp=EgIQAfABAQ%253D%253D";
            case CHANNELS:
                return SEARCH_URL + encodeUrlUtf8(searchString) + "&sp=EgIQAvABAQ%253D%253D";
            case PLAYLISTS:
                return SEARCH_URL + encodeUrlUtf8(searchString) + "&sp=EgIQA_ABAQ%253D%253D";
            case MUSIC_SONGS:
            case MUSIC_VIDEOS:
            case MUSIC_ALBUMS:
            case MUSIC_PLAYLISTS:
            case MUSIC_ARTISTS:
                return MUSIC_SEARCH_URL + encodeUrlUtf8(searchString);
            default:
                return SEARCH_URL + encodeUrlUtf8(searchString) + "&sp=8AEB";
        }
    }

    @Override
    public String[] getAvailableContentFilter() {
        return new String[]{
                ALL,
                VIDEOS,
                CHANNELS,
                PLAYLISTS,
                MUSIC_SONGS,
                MUSIC_VIDEOS,
                MUSIC_ALBUMS,
                MUSIC_PLAYLISTS
                // MUSIC_ARTISTS
        };
    }

    @Nonnull
    public static String getSearchParameter(final String contentFilter) {
        if (isNullOrEmpty(contentFilter)) {
            return "8AEB";
        }

        switch (contentFilter) {
                case VIDEOS:
                    return "EgIQAfABAQ%3D%3D";
                case CHANNELS:
                    return "EgIQAvABAQ%3D%3D";
                case PLAYLISTS:
                    return "EgIQA_ABAQ%3D%3D";
                case MUSIC_SONGS:
                case MUSIC_VIDEOS:
                case MUSIC_ALBUMS:
                case MUSIC_PLAYLISTS:
                case MUSIC_ARTISTS:
                    return "";
                default:
                    return "8AEB";
        }
    }
}
