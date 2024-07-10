package com.example.blackfriday.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RedisKeyUtils {
    public static final String EVENT_PRODUCT_KEY_PREFIX = "eventProduct.request.eventProductId:%s";

    public static String getEventProductRequestKey(Long eventProductId) {
        return EVENT_PRODUCT_KEY_PREFIX.formatted(eventProductId);
    }
}
