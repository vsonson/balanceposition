package com.balpos.app.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A PerformanceDatum.
 */
@Entity
@DiscriminatorValue(DatumType.PERFORMANCE)
public class PerformanceDatum extends MetricDatum {

    private static final long serialVersionUID = 5563439836065280892L;


}
