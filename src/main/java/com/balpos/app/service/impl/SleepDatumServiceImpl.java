package com.balpos.app.service.impl;

import com.balpos.app.domain.User;
import com.balpos.app.service.SleepDatumService;
import com.balpos.app.domain.SleepDatum;
import com.balpos.app.repository.SleepDatumRepository;
import com.balpos.app.service.dto.SleepDatumDTO;
import com.balpos.app.service.mapper.SleepDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


/**
 * Service Implementation for managing SleepDatum.
 */
@Service
@Transactional
@Slf4j
public class SleepDatumServiceImpl implements SleepDatumService{

    private final SleepDatumRepository sleepDatumRepository;

    private final SleepDatumMapper sleepDatumMapper;
    public SleepDatumServiceImpl(SleepDatumRepository sleepDatumRepository, SleepDatumMapper sleepDatumMapper) {
        this.sleepDatumRepository = sleepDatumRepository;
        this.sleepDatumMapper = sleepDatumMapper;
    }

    /**
     * Save a sleepDatum.
     *
     * @param sleepDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SleepDatumDTO save(SleepDatumDTO sleepDatumDTO, User user) {
        log.debug("Request to save SleepDatum : {}", sleepDatumDTO);

        //check for a record from today and update it if it exists
        LocalDateTime firstMomentOfDay = sleepDatumDTO.getTimestamp().atStartOfDay();
        List<SleepDatum> sleepDatumList = sleepDatumRepository.findByUserAndTimestampBetween(user, firstMomentOfDay, firstMomentOfDay.plusDays(1));

        SleepDatum sleepDatum;
        if (sleepDatumList != null && !sleepDatumList.isEmpty()) {
            sleepDatum = sleepDatumList.get(0);
            sleepDatum.setDurationHours(sleepDatumDTO.getDurationHours());
            sleepDatum.setTimestamp(sleepDatumDTO.getTimestamp());
            sleepDatum.setFeel(sleepDatumDTO.getFeel());

        } else {
            sleepDatum = sleepDatumMapper.toEntity(sleepDatumDTO);
        }

        sleepDatum.setUser(user);
        sleepDatum = sleepDatumRepository.save(sleepDatum);
        return sleepDatumMapper.toDto(sleepDatum);
    }

    /**
     *  Get all the sleepData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SleepDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SleepData");
        return sleepDatumRepository.findAll(pageable)
            .map(sleepDatumMapper::toDto);
    }

    /**
     *  Get one sleepDatum by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SleepDatumDTO findOne(Long id) {
        log.debug("Request to get SleepDatum : {}", id);
        SleepDatum sleepDatum = sleepDatumRepository.findOne(id);
        return sleepDatumMapper.toDto(sleepDatum);
    }
}
