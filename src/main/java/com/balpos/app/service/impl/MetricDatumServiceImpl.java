package com.balpos.app.service.impl;

import com.balpos.app.domain.MetricDatum;
import com.balpos.app.repository.MetricDatumRepository;
import com.balpos.app.service.MetricDatumService;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.mapper.MetricDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
     * Save a metricDatum.
     *
     * @param metricDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MetricDatumDTO save(MetricDatumDTO metricDatumDTO) {
        log.debug("Request to save MetricDatum : {}", metricDatumDTO);
        MetricDatum metricDatum = metricDatumMapper.toEntity(metricDatumDTO);
        metricDatum = metricDatumRepository.save(metricDatum);
        return metricDatumMapper.toDto(metricDatum);
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
     * Get one metricDatum by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MetricDatumDTO findOne(Long id) {
        log.debug("Request to get MetricDatum : {}", id);
        MetricDatum metricDatum = metricDatumRepository.findOne(id);
        return metricDatumMapper.toDto(metricDatum);
    }

    /**
     * Delete the  metricDatum by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MetricDatum : {}", id);
        metricDatumRepository.delete(id);
    }
}
