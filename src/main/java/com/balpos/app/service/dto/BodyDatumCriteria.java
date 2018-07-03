package com.balpos.app.service.dto;

import com.balpos.app.domain.enumeration.DigestiveLevel;
import com.balpos.app.domain.enumeration.HeadacheLevel;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import lombok.Data;


/**
 * Criteria class for the BodyDatum entity. This class is used in BodyDatumResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /body-data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
public class BodyDatumCriteria {

    private LongFilter id;
    private HeadacheLevelFilter headache;
    private DigestiveLevelFilter digestive;
    private LocalDateFilter timestamp;
    private LongFilter userId;

    /**
     * Class for filtering HeadacheLevel
     */
    public static class HeadacheLevelFilter extends Filter<HeadacheLevel> {
    }

    /**
     * Class for filtering DigestiveLevel
     */
    public static class DigestiveLevelFilter extends Filter<DigestiveLevel> {
    }
}
