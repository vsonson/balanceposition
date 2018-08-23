package com.balpos.app.service.dto;


import com.balpos.app.domain.Color;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the DataPoint entity.
 */
@Data
public class UserDataPointDTO implements Serializable {
    private static final long serialVersionUID = -461003081673481836L;

    private Boolean enabled;

    private Color color;

    private DataPointDTO dataPoint;

    private LocalDateTime lastupdate;

}
