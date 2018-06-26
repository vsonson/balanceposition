package com.balpos.app.service.dto;


import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the StressDatum entity.
 */
@Data
public class StressDatumDTO implements Serializable {

    private static final long serialVersionUID = 345816876990376200L;

    @NotNull
    @Size(min = 1)
    private String value;

    @NotNull
    private ZonedDateTime timestamp;
}
