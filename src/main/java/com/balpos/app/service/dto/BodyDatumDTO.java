package com.balpos.app.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the BodyDatum entity.
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BodyDatumDTO extends MetricDatumDTO {

    private static final long serialVersionUID = 3976816274359297234L;

    @NotNull
    private String headache;

}
