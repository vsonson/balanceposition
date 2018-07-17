package com.balpos.app.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the SleepDatum entity.
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SleepDatumDTO extends MetricDatumDTO {

    private static final long serialVersionUID = -5792625896653164268L;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    private Float durationHours;
}

