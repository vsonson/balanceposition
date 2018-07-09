package com.balpos.app.service.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the MetricDatum entity.
 */
@Data
public class MetricDatumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String metricValue;

    @NotNull
    private Instant timestamp;

    private String dataPointName;

    private String userLogin;
}
