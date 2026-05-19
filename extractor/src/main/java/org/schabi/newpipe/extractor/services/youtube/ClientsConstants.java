package org.schabi.newpipe.extractor.services.youtube;

final class ClientsConstants {
    private ClientsConstants() {
    }

    // Common client fields

    static final String DESKTOP_CLIENT_PLATFORM = "DESKTOP";
    static final String MOBILE_CLIENT_PLATFORM = "MOBILE";
    static final String WATCH_CLIENT_SCREEN = "WATCH";
    static final String EMBED_CLIENT_SCREEN = "EMBED";

    // WEB (YouTube desktop) client fields

    static final String WEB_CLIENT_ID = "1";
   static final String WEB_CLIENT_NAME = StringObfuscator.decode(new int[]{0x09,0x13,0x18});  // ="WEB"
    /**
     * The client version for InnerTube requests with the {@code WEB} client, used as the last
     * fallback if the extraction of the real one failed.
     */
static final String WEB_HARDCODED_CLIENT_VERSION = StringObfuscator.decode(new int[]{0x6C,0x70,0x6C,0x6E,0x6C,0x6A,0x6E,0x6F,0x6C,0x6E,0x70,0x6E,0x6F,0x70,0x6E,0x6E});
// = "2.20260120.01.00"
    // WEB_REMIX (YouTube Music) client fields

    static final String WEB_REMIX_CLIENT_ID = "67";
    static final String WEB_REMIX_CLIENT_NAME = StringObfuscator.decode(new int[]{0x09,0x13,0x18,0x72,0x0C,0x13,0x17,0x17,0x02});
// = "WEB_REMIX"
    static final String WEB_REMIX_HARDCODED_CLIENT_VERSION = StringObfuscator.decode(new int[]{0x6F,0x70,0x6C,0x6E,0x6C,0x6A,0x6E,0x6F,0x6C,0x6F,0x70,0x6E,0x6B,0x70,0x6E,0x6E});
// = "1.20260121.03.00"

    // WEB_EMBEDDED_PLAYER (YouTube embeds)

    static final String WEB_EMBEDDED_CLIENT_ID = "56";
   static final String WEB_EMBEDDED_CLIENT_NAME = StringObfuscator.decode(new int[]{0x09,0x13,0x18,0x72,0x13,0x17,0x18,0x13,0x12,0x12,0x13,0x12,0x72,0x0E,0x16,0x1F,0x03,0x13,0x0C});
// = "WEB_EMBEDDED_PLAYER"
 static final String WEB_EMBEDDED_CLIENT_VERSION = StringObfuscator.decode(new int[]{0x6F,0x70,0x6C,0x6E,0x6C,0x6A,0x6E,0x6F,0x6C,0x6C,0x70,0x6E,0x6F,0x70,0x6E,0x6E});
// = "1.20260122.01.00"

    // WEB_MUSIC_ANALYTICS (YouTube charts)

    static final String WEB_MUSIC_ANALYTICS_CLIENT_ID = "31";
   static final String WEB_MUSIC_ANALYTICS_CLIENT_NAME = StringObfuscator.decode(new int[]{0x09,0x13,0x18,0x72,0x17,0x01,0x0D,0x17,0x19,0x72,0x1F,0x18,0x1F,0x16,0x03,0x0E,0x17,0x19,0x0D});
// = "WEB_MUSIC_ANALYTICS"
    static final String WEB_MUSIC_ANALYTICS_CLIENT_VERSION = "2.0";

    // IOS (iOS YouTube app) client fields

    static final String IOS_CLIENT_ID = "5";
   static final String IOS_CLIENT_NAME = StringObfuscator.decode(new int[]{0x17,0x11,0x0D});
// = "IOS"

    /**
     * The hardcoded client version of the iOS app used for InnerTube requests with this client.
     *
     * <p>
     * It can be extracted by getting the latest release version of the app on
     * <a href="https://apps.apple.com/us/app/youtube/id544007664">the App
     * Store page of the YouTube app</a>, in the {@code What’s New} section.
     * </p>
     */
    static final String IOS_CLIENT_VERSION = StringObfuscator.decode(new int[]{0x6C,0x6F,0x70,0x6E,0x6B,0x70,0x6C});
// = "21.03.2"

    /**
     * The device machine id for the iPhone 15 Pro Max, used to get 60fps with the {@code iOS}
     * client.
     *
     * <p>
     * See <a href="https://gist.github.com/adamawolf/3048717">this GitHub Gist</a> for more
     * information.
     * </p>
     */
    static final String IOS_DEVICE_MODEL = "iPhone16,2";

    /**
     * The iOS version to be used in JSON POST requests, the one of an iPhone 15 Pro Max running
     * iOS 18.2.1 with the hardcoded version of the iOS app (for the {@code "osVersion"} field).
     *
     * <p>
     * The value of this field seems to use the following structure:
     * "iOS major version.minor version.patch version.build version", where
     * "patch version" is equal to 0 if it isn't set
     * The build version corresponding to the iOS version used can be found on
     * <a href="https://theapplewiki.com/wiki/Firmware/iPhone/18.x#iPhone_15_Pro_Max">
     *     https://theapplewiki.com/wiki/Firmware/iPhone/18.x#iPhone_15_Pro_Max</a>
     * </p>
     *
     * @see #IOS_USER_AGENT_VERSION
     */
    static final String IOS_OS_VERSION = "18.7.2.22H124";

    /**
     * The iOS version to be used in the HTTP user agent for requests.
     *
     * <p>
     * This should be the same of as {@link #IOS_OS_VERSION}.
     * </p>
     *
     * @see #IOS_OS_VERSION
     */
    static final String IOS_USER_AGENT_VERSION = "18_7_2";

    // ANDROID (Android YouTube app) client fields

    static final String ANDROID_CLIENT_ID = "3";
   static final String ANDROID_CLIENT_NAME = StringObfuscator.decode(new int[]{0x1F,0x18,0x12,0x0C,0x11,0x17,0x12});
// = "ANDROID"

    /**
     * The hardcoded client version of the Android app used for InnerTube requests with this
     * client.
     *
     * <p>
     * It can be extracted by getting the latest release version of the app in an APK repository
     * such as <a href="https://www.apkmirror.com/apk/google-inc/youtube/">APKMirror</a>.
     * </p>
     */
    static final String ANDROID_CLIENT_VERSION = StringObfuscator.decode(new int[]{0x6C,0x6F,0x70,0x6E,0x6B,0x70,0x6B,0x6A});
// = "21.03.36"
}
