package com.balpos.app.web.rest.mapper;

import com.balpos.app.domain.LookupValue;
import com.balpos.app.security.FrontendViewModelLookupService;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.mapper.MetricDatumMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class FrontendViewModelMapper {

    @Autowired
    private MetricDatumMapper metricDatumMapper;
    @Autowired
    private FrontendViewModelLookupService frontendViewModelLookupService;

    public <S extends MetricDatumDTO> S toVM(S metricDatumDTO) {
        // map the value
        Optional<LookupValue> lookupValue = frontendViewModelLookupService.findByDatapointNameAndSubclassNameAndSourceValue(
            metricDatumDTO.getDataPointName(),
            "datum_value",
            metricDatumDTO.getDatumValue());
        lookupValue.ifPresent(luv -> metricDatumDTO.setDatumValue(luv.getMappedValue().toString()));

        // map the extra values
        metricDatumDTO.mapChildFields(metricDatumDTO);

        return metricDatumDTO;
    }

    public abstract List<MetricDatumDTO> toVM(List<? extends MetricDatumDTO> metricDatumDTOList);
}
