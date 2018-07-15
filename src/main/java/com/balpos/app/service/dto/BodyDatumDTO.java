package com.balpos.app.service.dto;


import com.balpos.app.domain.BodyDatum;
import com.balpos.app.domain.LookupValue;
import com.balpos.app.domain.MetricDatum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Optional;

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

    @Override
    public <S extends MetricDatum> void mapChildFields(S entity) {
        setHeadache(((BodyDatum) entity).getHeadache());
    }

    @Override
    public <S extends MetricDatumDTO> void mapChildFields(S metricDatumDTO) {
        Optional<LookupValue> lookupValue = frontendViewModelLookupService.findByDatapointNameAndSubclassNameAndSourceValue(
            metricDatumDTO.getDataPointName(),
            "headache",
            ((BodyDatumDTO) metricDatumDTO).getHeadache());
        lookupValue.ifPresent(luv -> setHeadache(luv.getMappedValue().toString()));
    }

}
