package com.balpos.app.service.dto;

import io.github.jhipster.service.filter.*;
import lombok.Data;


/**
 * Criteria class for the SleepDatum entity. This class is used in SleepDatumResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sleep-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
public class SleepDatumCriteria {

    private LongFilter id;
    private FloatFilter durationHours;
    private StringFilter feel;
    private LocalDateFilter timestamp;
    private LongFilter userId;
}
