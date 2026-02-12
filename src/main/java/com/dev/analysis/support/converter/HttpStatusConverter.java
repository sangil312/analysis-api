package com.dev.analysis.support.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class HttpStatusConverter extends AbstractBeanField<Integer, String> {
    @Override
    protected Integer convert(String value) throws CsvDataTypeMismatchException {
        if (!StringUtils.hasText(value)) {
            throw new CsvDataTypeMismatchException();
        }
        try {
            HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(value));
            return httpStatus.value();
        } catch (IllegalArgumentException ex) {
            throw new CsvDataTypeMismatchException();
        }
    }
}
