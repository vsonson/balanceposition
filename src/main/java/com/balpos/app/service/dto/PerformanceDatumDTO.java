package com.balpos.app.service.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the PerformanceDatum entity.
 */
@Data
@Accessors(chain = true)
public class PerformanceDatumDTO implements Serializable {

    private static final long serialVersionUID = 2045907276723583076L;

    @NotNull
    @Size(min = 1)
    private String feel;

    @NotNull
    private LocalDate timestamp;
}
