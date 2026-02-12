package com.dev.analysis.support.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.springframework.util.StringUtils;

public class NonBlankConverter extends AbstractBeanField<String, String> {
    @Override
    protected String convert(String value) throws CsvDataTypeMismatchException {
        if (!StringUtils.hasText(value)) {
            throw new CsvDataTypeMismatchException();
        }
        return value;
    }
}
