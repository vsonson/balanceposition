package com.balpos.app.service.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the SleepDatum entity.
 */
@Data
@Accessors(chain = true)
public class SleepDatumDTO implements Serializable {

    private static final long serialVersionUID = -5792625896653164268L;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    private Float durationHours;

    @NotNull
    private String feel;

    @NotNull
    private LocalDate timestamp;

}
