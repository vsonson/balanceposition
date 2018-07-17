package com.balpos.app.service.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the MetricDatum entity.
 */
@Data
public class MetricDatumDTO implements Serializable {
    @NotNull
    @Size(min = 1)
    private String datumValue;

    @NotNull
    private LocalDateTime timestamp;

    private String dataPointName;
}
