package com.balpos.app.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A FocusDatum.
 */
@Entity
@DiscriminatorValue("Focus")
public class FocusDatum extends MetricDatum {

    private static final long serialVersionUID = 499780691035089767L;
}
