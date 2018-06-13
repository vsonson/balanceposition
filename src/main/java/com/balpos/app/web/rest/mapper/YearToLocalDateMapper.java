package com.balpos.app.web.rest.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class YearToLocalDateMapper {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy");

    public String dateToYear(LocalDate dateTime) {
        return dateTime != null ?
            dateFormatter.format(dateTime) : null;
    }

    public LocalDate yearToDate(String year) {
        return !StringUtils.isEmpty(year) ?
            LocalDate.of(Integer.parseInt(year), 1, 1) : null;
    }

}
