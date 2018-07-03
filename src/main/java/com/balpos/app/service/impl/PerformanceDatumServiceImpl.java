package com.balpos.app.service.impl;

import com.balpos.app.domain.PerformanceDatum;
import com.balpos.app.domain.SleepDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.PerformanceDatumRepository;
import com.balpos.app.service.PerformanceDatumService;
import com.balpos.app.service.dto.PerformanceDatumDTO;
import com.balpos.app.service.mapper.PerformanceDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Service Implementation for managing PerformanceDatum.
 */
@Service
@Transactional
@Slf4j
public class PerformanceDatumServiceImpl implements PerformanceDatumService {

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
    public PerformanceDatumDTO save(PerformanceDatumDTO performanceDatumDTO, User user) {
        log.debug("Request to save PerformanceDatum : {}", performanceDatumDTO);

        //check for a record from today and update it if it exists
        LocalDateTime firstMomentOfDay = performanceDatumDTO.getTimestamp().atStartOfDay();
        List<PerformanceDatum> performanceDatumList = performanceDatumRepository.findByUserAndTimestampBetween(user, firstMomentOfDay, firstMomentOfDay.plusDays(1));

        PerformanceDatum performanceDatum;
        if (performanceDatumList != null && !performanceDatumList.isEmpty()) {
            performanceDatum = performanceDatumList.get(0);
            performanceDatum.setFeel(performanceDatumDTO.getFeel());
            performanceDatum.setTimestamp(performanceDatumDTO.getTimestamp());

        } else {
            performanceDatum = performanceDatumMapper.toEntity(performanceDatumDTO);
        }

        performanceDatum.setUser(user);
        performanceDatum = performanceDatumRepository.save(performanceDatum);
        return performanceDatumMapper.toDto(performanceDatum);
    }

    /**
     * Get all the performanceData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PerformanceDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PerformanceData");
        return performanceDatumRepository.findAll(pageable)
            .map(performanceDatumMapper::toDto);
    }

}
