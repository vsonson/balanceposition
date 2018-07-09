package com.balpos.app.service.mapper;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.repository.DataPointRepository;
import com.balpos.app.service.dto.MetricDatumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for the entity MetricDatum and its DTO MetricDatumDTO.
 */
@Mapper(componentModel = "spring", uses = {DataPointMapper.class, UserMapper.class})
public abstract class MetricDatumMapper implements EntityMapper<MetricDatumDTO, MetricDatum> {
    private final Map<String, DataPoint> LOCALCACHE_DATAPOINT = new HashMap<>();

    @Autowired
    private DataPointRepository dataPointRepository;

    @PostConstruct
    private void postConstruct() {
        List<DataPoint> page = dataPointRepository.findAll(Specifications.where(null));
        page.forEach(dataPoint -> LOCALCACHE_DATAPOINT.put(dataPoint.getName(), dataPoint));
    }

    @Mapping(source = "dataPoint.name", target = "dataPointName")
    public abstract MetricDatumDTO toDto(MetricDatum metricDatum);

    @Mapping(source = "dataPointName", target = "dataPoint")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract MetricDatum toEntity(MetricDatumDTO metricDatumDTO);

    DataPoint fromName(String dataPointName) {
        return LOCALCACHE_DATAPOINT.get(dataPointName);
    }

    public MetricDatum fromId(Long id) {
        if (id == null) {
            return null;
        }
        MetricDatum metricDatum = new MetricDatum();
        metricDatum.setId(id);
        return metricDatum;
    }
}
