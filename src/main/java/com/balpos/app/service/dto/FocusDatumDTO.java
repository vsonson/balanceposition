package com.balpos.app.service.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the FocusDatum entity.
 */
@Data
public class FocusDatumDTO implements Serializable {

    private static final long serialVersionUID = 626504868951324740L;

    @NotNull
    @Size(min = 1)
    private String level;

    @NotNull
    private ZonedDateTime timestamp;
}
