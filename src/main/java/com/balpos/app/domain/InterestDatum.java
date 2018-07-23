package com.balpos.app.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A PerformanceDatum.
 */
@Entity
@DiscriminatorValue(DatumType.INTEREST)
public class InterestDatum extends MetricDatum {

    private static final long serialVersionUID = 2774205864624040040L;
}
