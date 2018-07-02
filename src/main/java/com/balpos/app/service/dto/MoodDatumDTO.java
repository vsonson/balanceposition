package com.balpos.app.service.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the MoodDatum entity.
 */
@Data
@Accessors(chain = true)
public class MoodDatumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String value;

    private ZonedDateTime timestamp;
}
