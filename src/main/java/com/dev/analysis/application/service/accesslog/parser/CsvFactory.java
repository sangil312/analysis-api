package com.dev.analysis.application.service.accesslog.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Reader;

@Component
@RequiredArgsConstructor
public class CsvFactory {
    private final CsvToBeanFilter nonBlankLineFilter;

    public <T> CsvToBean<T> create(Reader reader, Class<T> type) {
        return new CsvToBeanBuilder<T>(reader)
            .withType(type)
            .withSeparator(',')
            .withQuoteChar('"')
            .withFilter(nonBlankLineFilter)
            .withThrowExceptions(false)
            .build();
    }
}
