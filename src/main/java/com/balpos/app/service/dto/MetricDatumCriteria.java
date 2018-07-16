package com.balpos.app.service.dto;

import com.balpos.app.service.filter.LocalDateTimeFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import lombok.Data;


/**
 * Criteria class for the MetricDatum entity. This class is used in MetricDatumResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /metric-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
public class MetricDatumCriteria {

    private LongFilter id;

    private StringFilter dataPointName;

    private LocalDateTimeFilter timestamp;

    private StringFilter datumValue;

    private LongFilter userId;
}
