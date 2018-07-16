package com.balpos.app.service.mapper;

import com.balpos.app.domain.*;
import com.balpos.app.service.dto.BodyDatumDTO;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.dto.SleepDatumDTO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Mapper for the entity MetricDatum and its DTO MetricDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {DataPointMapper.class, UserMapper.class})
@Slf4j
public abstract class MetricDatumMapper {

    @Autowired
    protected DataPointMapper dataPointMapper;

    public static Class<? extends MetricDatum> classForDatum(DataPoint datapoint) {
        // TODO this duplicates information already declared in annotations
        // TODO determine the type based on the discriminatorvalue annotation
        switch (datapoint.getName()) {
            case "Stress":
                return StressDatum.class;
            case "Mood":
                return MoodDatum.class;
            case "Interest":
                return InterestDatum.class;
            case "Focus":
                return FocusDatum.class;
            case "Performance":
                return PerformanceDatum.class;
            case "Sleep":
                return SleepDatum.class;
            case "Body":
                return BodyDatum.class;
        }
        throw new IllegalArgumentException("Unrecognized data point class");
    }

    public static Class<? extends MetricDatumDTO> classForDto(DataPoint datapoint) {
        // TODO this duplicates information already declared in annotations
        // TODO determine the type based on the secondarytable annotation
        switch (datapoint.getName()) {
            case "Sleep":
                return SleepDatumDTO.class;
            case "Body":
                return BodyDatumDTO.class;
            default:
                return MetricDatumDTO.class;
        }
    }

    public <S extends MetricDatum, T extends MetricDatumDTO> S toEntity(T dto) {
        if (dto == null) {
            return null;
        }

        try {
            S metricDatum = getNewDatumForDto(dto);
            metricDatum.setDatumValue(dto.getDatumValue());
            metricDatum.setTimestamp(dto.getTimestamp());
            metricDatum.mapChildFields(dto);
            return metricDatum;

        } catch (IllegalAccessException | InstantiationException e) {
            log.error("Exception during mapping", e);
            return null;
        }
    }

    public <S extends MetricDatum, T extends MetricDatumDTO> T toDto(S entity) {
        if (entity == null) {
            return null;
        }

        T metricDTO = getNewDtoForDatum(entity);
        metricDTO.setDatumValue(entity.getDatumValue());
        metricDTO.setTimestamp(entity.getTimestamp());
        metricDTO.mapChildFields(entity);

        return metricDTO;
    }

    public abstract List<MetricDatumDTO> toDto(List<? extends MetricDatum> entityList);

    @SuppressWarnings("unchecked")
    @NonNull
    private <S extends MetricDatum, T extends MetricDatumDTO> S getNewDatumForDto(T dto) throws IllegalAccessException, InstantiationException {
        DataPoint datapoint = dataPointMapper.fromName(dto.getDataPointName());
        if (datapoint == null || StringUtils.isEmpty(datapoint.getName())) {
            throw new IllegalArgumentException("Unrecognized data point name");
        }

        Class<? extends MetricDatum> clazz = classForDatum(datapoint);
        S result = (S) clazz.newInstance();
        result.setDataPoint(datapoint);
        return result;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <S extends MetricDatum, T extends MetricDatumDTO> T getNewDtoForDatum(S entity) {
        DataPoint datapoint = entity.getDataPoint();
        if (datapoint == null || StringUtils.isEmpty(datapoint.getName())) {
            throw new IllegalArgumentException("Unrecognized data point");
        }

        Class<? extends MetricDatumDTO> clazz = classForDto(datapoint);
        T result;
        try {
            result = (T) clazz.newInstance();
        } catch (Exception e) {
            log.error("Error during mapping, mapping to default DTO", e);
            result = (T) new MetricDatumDTO();
        }
        result.setDataPointName(datapoint.getName());
        return result;
    }


}