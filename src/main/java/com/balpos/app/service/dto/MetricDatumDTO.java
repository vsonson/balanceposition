package com.balpos.app.service.dto;


import com.balpos.app.domain.MetricDatum;
import com.balpos.app.security.FrontendViewModelLookupService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the MetricDatum entity.
 */
@Data
public class MetricDatumDTO implements Serializable {

    //TODO fix code smell
    @Autowired
    @Transient
    protected FrontendViewModelLookupService frontendViewModelLookupService;

    @NotNull
    @Size(min = 1)
    private String datumValue;

    @NotNull
    private LocalDateTime timestamp;

    private String dataPointName;

    //TODO fix code smell
    public <S extends MetricDatum> void mapChildFields(S entity) {
    }

    //TODO fix code smell
    public <S extends MetricDatumDTO> void mapChildFields(S metricDatumDTO) {
    }
}
