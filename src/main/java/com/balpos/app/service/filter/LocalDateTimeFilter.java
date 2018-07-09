package com.balpos.app.service.filter;

import io.github.jhipster.service.filter.RangeFilter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class LocalDateTimeFilter extends RangeFilter<LocalDateTime> {

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setEquals(LocalDateTime equals) {
        super.setEquals(equals);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setGreaterThan(LocalDateTime equals) {
        super.setGreaterThan(equals);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setGreaterOrEqualThan(LocalDateTime equals) {
        super.setGreaterOrEqualThan(equals);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setLessThan(LocalDateTime equals) {
        super.setLessThan(equals);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setLessOrEqualThan(LocalDateTime equals) {
        super.setLessOrEqualThan(equals);
    }

    @Override
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setIn(List<LocalDateTime> in) {
        super.setIn(in);
    }
}
