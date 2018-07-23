package com.balpos.app.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(DatumType.ENERGY)
public class EnergyDatum extends MetricDatum {
    private static final long serialVersionUID = -8034035540584510261L;
}
