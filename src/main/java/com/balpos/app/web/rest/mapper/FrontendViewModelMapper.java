package com.balpos.app.web.rest.mapper;

import com.balpos.app.domain.LookupValue;
import com.balpos.app.security.MetricValueMappingLookupService;
import com.balpos.app.service.dto.BodyDatumDTO;
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
    private MetricValueMappingLookupService metricValueMappingLookupService;

    public <S extends MetricDatumDTO> S toVM(S metricDatumDTO) {
        // map the value
        Optional<LookupValue> lookupValue = metricValueMappingLookupService.findByDatapointNameAndSubclassNameAndSourceValue(
            metricDatumDTO.getDataPointName(),
            "datum_value",
            metricDatumDTO.getDatumValue());
        lookupValue.ifPresent(luv -> metricDatumDTO.setDatumValue(luv.getMappedValue().toString()));

        // map the extra values
        mapChildFields(metricDatumDTO);

        return metricDatumDTO;
    }

    public <S extends MetricDatumDTO> void mapChildFields(S metricDatumDTO) {
        String datapointName = metricDatumDTO.getDataPointName();
        switch (datapointName) {
            case "Body":
                Optional<LookupValue> lookupValue = metricValueMappingLookupService.findByDatapointNameAndSubclassNameAndSourceValue(
                    metricDatumDTO.getDataPointName(),
                    "headache",
                    ((BodyDatumDTO) metricDatumDTO).getHeadache());
                lookupValue.ifPresent(luv -> ((BodyDatumDTO) metricDatumDTO).setHeadache(luv.getMappedValue().toString()));
                break;
        }

    }

    public abstract List<MetricDatumDTO> toVM(List<? extends MetricDatumDTO> metricDatumDTOList);
}
