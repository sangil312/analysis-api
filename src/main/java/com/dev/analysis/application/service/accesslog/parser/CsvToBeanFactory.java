package com.dev.analysis.application.service.accesslog.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Reader;

@Component
@RequiredArgsConstructor
public class CsvToBeanFactory {
    private final CsvToBeanFilter nonBlankLineFilter;

    public <T> CsvToBean<T> create(Class<T> type, Reader reader, CsvExceptionHandler handler) {
        return new CsvToBeanBuilder<T>(reader)
            .withType(type)
            .withSeparator(',')
            .withQuoteChar('"')
            .withFilter(nonBlankLineFilter)
            .withExceptionHandler(handler)
            .build();
    }


}
