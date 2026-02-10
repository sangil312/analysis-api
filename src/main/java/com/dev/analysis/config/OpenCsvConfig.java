package com.dev.analysis.config;

import com.opencsv.bean.CsvToBeanFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class OpenCsvConfig {

    @Bean
    public CsvToBeanFilter nonBlankLineFilter() {
        return line -> {
            for (String value : line) {
                if (StringUtils.hasText(value)) {
                    return true;
                }
            }
            return false;
        };
    }
}
