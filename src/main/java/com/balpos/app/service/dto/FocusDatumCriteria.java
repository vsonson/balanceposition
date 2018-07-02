package com.balpos.app.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the FocusDatum entity. This class is used in FocusDatumResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /focus-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FocusDatumCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter level;

    private ZonedDateTimeFilter timestamp;

    private LongFilter userId;

    public FocusDatumCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLevel() {
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public ZonedDateTimeFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTimeFilter timestamp) {
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
        return "FocusDatumCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
