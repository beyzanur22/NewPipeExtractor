package org.schabi.newpipe.extractor.services.youtube;

public final class StringObfuscator {
    private static final int KEY = 0x5E;

    private StringObfuscator() {
    }

    public static String decode(final int[] encoded) {
        final char[] chars = new char[encoded.length];
        for (int i = 0; i < encoded.length; i++) {
            chars[i] = (char) (encoded[i] ^ KEY);
        }
        return new String(chars);
    }

    public static int[] encode(final String s) {
        final int[] result = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = s.charAt(i) ^ KEY;
        }
        return result;
    }
}
