package com.balpos.app.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the PerformanceDatum entity. This class is used in PerformanceDatumResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /performance-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PerformanceDatumCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter feel;

    private LocalDateFilter timestamp;

    private LongFilter userId;

    public PerformanceDatumCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFeel() {
        return feel;
    }

    public void setFeel(StringFilter feel) {
        this.feel = feel;
    }

    public LocalDateFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateFilter timestamp) {
        this.timestamp = timestamp;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PerformanceDatumCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (feel != null ? "feel=" + feel + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
