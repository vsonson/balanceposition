package com.balpos.app.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A MoodDatum.
 */
@Entity
@DiscriminatorValue(DatumType.MOOD)
public class MoodDatum extends MetricDatum {

    private static final long serialVersionUID = -8747084459455291792L;
}
