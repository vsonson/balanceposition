package com.balpos.app.service.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the DataPoint entity.
 */
@Data
public class DataPointDTO implements Serializable {

    private static final long serialVersionUID = -8967554606502281954L;

    private Long id;

    private String name;

    private String type;

    private Boolean onePerDay;

}
