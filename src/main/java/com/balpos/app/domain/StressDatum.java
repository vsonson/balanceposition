package com.balpos.app.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A StressDatum.
 */
@Entity
@DiscriminatorValue(DatumType.STRESS)
public class StressDatum extends MetricDatum {

    private static final long serialVersionUID = -6282586102195331L;
}
