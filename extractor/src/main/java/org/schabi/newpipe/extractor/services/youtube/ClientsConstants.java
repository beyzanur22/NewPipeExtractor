package org.schabi.newpipe.extractor.services.youtube;

final class ClientsConstants {
    private ClientsConstants() {
    }

    static final String DESKTOP_CLIENT_PLATFORM = "DESKTOP";
    static final String MOBILE_CLIENT_PLATFORM = "MOBILE";
    static final String WATCH_CLIENT_SCREEN = "WATCH";
    static final String EMBED_CLIENT_SCREEN = "EMBED";

    static final String WEB_CLIENT_ID = "1";
    static final String WEB_CLIENT_NAME = "WEB";
    static final String WEB_HARDCODED_CLIENT_VERSION = "2.20260120.01.00";

    static final String WEB_REMIX_CLIENT_ID = "67";
    static final String WEB_REMIX_CLIENT_NAME = "WEB_REMIX";
    static final String WEB_REMIX_HARDCODED_CLIENT_VERSION = "1.20260121.03.00";

    static final String WEB_EMBEDDED_CLIENT_ID = "56";
    static final String WEB_EMBEDDED_CLIENT_NAME = "WEB_EMBEDDED_PLAYER";
    static final String WEB_EMBEDDED_CLIENT_VERSION = "1.20260122.01.00";

    static final String WEB_MUSIC_ANALYTICS_CLIENT_ID = "31";
    static final String WEB_MUSIC_ANALYTICS_CLIENT_NAME = "WEB_MUSIC_ANALYTICS";
    static final String WEB_MUSIC_ANALYTICS_CLIENT_VERSION = "2.0";

    static final String IOS_CLIENT_ID = "5";
    static final String IOS_CLIENT_NAME = "IOS";
    static final String IOS_CLIENT_VERSION = "21.03.2";
    static final String IOS_DEVICE_MODEL = "iPhone16,2";
    static final String IOS_OS_VERSION = "18.7.2.22H124";
    static final String IOS_USER_AGENT_VERSION = "18_7_2";

    static final String ANDROID_CLIENT_ID = "3";
    static final String ANDROID_CLIENT_NAME = "ANDROID";
    static final String ANDROID_CLIENT_VERSION = "21.03.36";

    static final String VISIONOS_CLIENT_ID = StringObfuscator.decode(new int[]{0x6F,0x6E,0x6F});
    static final String VISIONOS_CLIENT_NAME = StringObfuscator.decode(new int[]{0x08,0x17,0x0D,0x17,0x11,0x10,0x11,0x0D});
    static final String VISIONOS_CLIENT_VERSION = StringObfuscator.decode(new int[]{0x6F,0x70,0x6E,0x6C});
    static final String VISIONOS_DEVICE_MODEL = StringObfuscator.decode(new int[]{0x0C,0x3B,0x3F,0x32,0x37,0x2A,0x27,0x1A,0x3B,0x28,0x37,0x3D,0x3B,0x6F,0x6A,0x72,0x6F});
    static final String VISIONOS_VERSION = StringObfuscator.decode(new int[]{0x6C,0x6B,0x70,0x68,0x70,0x6E,0x70,0x6C,0x6D,0x11,0x6A,0x69,0x6F});
    static final String VISIONOS_USER_AGENT_VERSION = StringObfuscator.decode(new int[]{0x6C,0x6B,0x01,0x68,0x01,0x6E});
}
