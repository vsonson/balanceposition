package com.balpos.app.service.impl;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.MetricDatumRepository;
import com.balpos.app.service.MetricDatumService;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.mapper.MetricDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service Implementation for managing MetricDatum.
 */
@Service
@Transactional
@Slf4j
public class MetricDatumServiceImpl implements MetricDatumService {

    private final MetricDatumRepository metricDatumRepository;

    private final MetricDatumMapper metricDatumMapper;

    public MetricDatumServiceImpl(MetricDatumRepository metricDatumRepository, MetricDatumMapper metricDatumMapper) {
        this.metricDatumRepository = metricDatumRepository;
        this.metricDatumMapper = metricDatumMapper;
    }

    /**
     * Get all the metricData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MetricDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MetricData");
        return metricDatumRepository.findAll(pageable)
            .map(metricDatumMapper::toDto);
    }

    /**
     * Save a metricDatum.
     *
     * @param metricDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends MetricDatumDTO, S extends MetricDatum> T save(T metricDatumDTO, User user) {
        log.debug("Request to save MetricDatum : {}", metricDatumDTO);
        S metricDatum = metricDatumMapper.toEntity(metricDatumDTO);

        // if this should only insert one record per day then get the current record, if exists, and update it
        if (metricDatum.getDataPoint().getOnePerDay()) {
            S metricDatumUpdate = getDatumForDay(metricDatumDTO.getTimestamp().toLocalDate(), metricDatum.getDataPoint(), user);
            if (metricDatumUpdate != null) {
                metricDatum.setId(metricDatumUpdate.getId());
            }
        }

        metricDatum.setUser(user);
        metricDatum = metricDatumRepository.save(metricDatum);
        return (T) metricDatumMapper.toDto(metricDatum);
    }

    @SuppressWarnings("unchecked")
    private <S extends MetricDatum> S getDatumForDay(LocalDate day, DataPoint dataPoint, User user) {
        //check for a record from today and update it if it exists
        LocalDateTime firstMomentOfDay = day.atStartOfDay();
        List<? extends MetricDatum> metricDatumList = metricDatumRepository.findByUserAndDataPointAndTimestampBetween(user, dataPoint, firstMomentOfDay, firstMomentOfDay.plusDays(1));
        return (metricDatumList == null || metricDatumList.isEmpty()) ? null : (S) metricDatumList.get(0);
    }


}
