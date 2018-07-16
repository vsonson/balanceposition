package com.balpos.app.service.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the DataPoint entity.
 */
@Data
public class UserDataPointDTO implements Serializable {
    private static final long serialVersionUID = -461003081673481836L;

    private Boolean enabled;

    private String color;

    private DataPointDTO dataPoint;

}
