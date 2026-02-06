package com.dev.analysis.parser;

import com.dev.analysis.support.converter.HttpStatusConverter;
import com.dev.analysis.support.converter.NonBlankConverter;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Getter;

@Getter
public class AccessLogCsvRow {
    @CsvCustomBindByName(column = "ClientIp", converter = NonBlankConverter.class, required = true)
    private String clientIp;

    @CsvCustomBindByName(column = "RequestUri", converter = NonBlankConverter.class, required = true)
    private String requestUri;

    @CsvCustomBindByName(column = "HttpStatus", converter = HttpStatusConverter.class, required = true)
    private Integer httpStatus;
}
