package com.dev.analysis.enums;

import org.springframework.util.StringUtils;

public enum ValueType {
    UNKNOWN;

    public static String valueOrUnknown(String value) {
        return StringUtils.hasText(value) ? value : UNKNOWN.name();
    }
}
