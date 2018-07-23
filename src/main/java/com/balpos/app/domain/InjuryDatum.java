package com.balpos.app.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(DatumType.INJURY)
public class InjuryDatum extends MetricDatum {
    private static final long serialVersionUID = 2485182979057166007L;
}
