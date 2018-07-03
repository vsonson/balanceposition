package com.balpos.app.service.dto;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import lombok.Data;


/**
 * Criteria class for the MoodDatum entity. This class is used in MoodDatumResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /mood-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
public class MoodDatumCriteria {

    private LongFilter id;

    private StringFilter value;

    private ZonedDateTimeFilter timestamp;

    private LongFilter userId;

}
