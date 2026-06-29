package com.musiclib.core.extractor.exceptions;

import com.musiclib.core.extractor.services.youtube.StringObfuscator;

public class YoutubeMusicPremiumContentException extends ContentNotAvailableException {
    private static final int[] MSG = {10, 54, 55, 45, 126, 40, 55, 58, 59, 49, 126, 55, 45, 126, 63, 126, 7, 49, 43, 10, 43, 60, 59, 126, 19, 43, 45, 55, 61, 126, 14, 44, 59, 51, 55, 43, 51, 126, 40, 55, 58, 59, 49};

    public YoutubeMusicPremiumContentException() {
        super(StringObfuscator.decode(MSG));
    }

    public YoutubeMusicPremiumContentException(final Throwable cause) {
        super(StringObfuscator.decode(MSG), cause);
    }
}
