package com.nikolayr.playground.shared.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringUtils {
    public static String decodeBase64(String encoded) {
        return new String(Base64.getDecoder()
                .decode(encoded), StandardCharsets.UTF_8);
    }
}
