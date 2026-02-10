package com.dev.analysis.domain.accesslog;

import java.util.List;

public record ParseError(
        Long count,
        List<String> errorSamples
) {
    public static ParseError of(Long count, List<String> errorSamples) {
        return new ParseError(count, errorSamples);
    }
}
