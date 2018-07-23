package com.balpos.app.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(DatumType.APPETITE)
public class AppetiteDatum extends MetricDatum {
    private static final long serialVersionUID = 4334465963867989L;
}
