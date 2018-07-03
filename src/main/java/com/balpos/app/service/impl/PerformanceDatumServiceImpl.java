package com.balpos.app.service.impl;

import com.balpos.app.service.PerformanceDatumService;
import com.balpos.app.domain.PerformanceDatum;
import com.balpos.app.repository.PerformanceDatumRepository;
import com.balpos.app.service.dto.PerformanceDatumDTO;
import com.balpos.app.service.mapper.PerformanceDatumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PerformanceDatum.
 */
@Service
@Transactional
public class PerformanceDatumServiceImpl implements PerformanceDatumService{

    private final Logger log = LoggerFactory.getLogger(PerformanceDatumServiceImpl.class);

    private final PerformanceDatumRepository performanceDatumRepository;

    private final PerformanceDatumMapper performanceDatumMapper;
    public PerformanceDatumServiceImpl(PerformanceDatumRepository performanceDatumRepository, PerformanceDatumMapper performanceDatumMapper) {
        this.performanceDatumRepository = performanceDatumRepository;
        this.performanceDatumMapper = performanceDatumMapper;
    }

    /**
     * Save a performanceDatum.
     *
     * @param performanceDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PerformanceDatumDTO save(PerformanceDatumDTO performanceDatumDTO) {
        log.debug("Request to save PerformanceDatum : {}", performanceDatumDTO);
        PerformanceDatum performanceDatum = performanceDatumMapper.toEntity(performanceDatumDTO);
        performanceDatum = performanceDatumRepository.save(performanceDatum);
        return performanceDatumMapper.toDto(performanceDatum);
    }

    /**
     *  Get all the performanceData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PerformanceDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PerformanceData");
        return performanceDatumRepository.findAll(pageable)
            .map(performanceDatumMapper::toDto);
    }

    /**
     *  Get one performanceDatum by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PerformanceDatumDTO findOne(Long id) {
        log.debug("Request to get PerformanceDatum : {}", id);
        PerformanceDatum performanceDatum = performanceDatumRepository.findOne(id);
        return performanceDatumMapper.toDto(performanceDatum);
    }

    /**
     *  Delete the  performanceDatum by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PerformanceDatum : {}", id);
        performanceDatumRepository.delete(id);
    }
}
