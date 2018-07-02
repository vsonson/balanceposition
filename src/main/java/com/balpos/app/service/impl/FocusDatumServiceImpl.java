package com.balpos.app.service.impl;

import com.balpos.app.domain.FocusDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.FocusDatumRepository;
import com.balpos.app.service.FocusDatumService;
import com.balpos.app.service.dto.FocusDatumDTO;
import com.balpos.app.service.mapper.FocusDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Service Implementation for managing FocusDatum.
 */
@Service
@Transactional
@Slf4j
public class FocusDatumServiceImpl implements FocusDatumService {

    private final FocusDatumRepository focusDatumRepository;

    private final FocusDatumMapper focusDatumMapper;

    public FocusDatumServiceImpl(FocusDatumRepository focusDatumRepository, FocusDatumMapper focusDatumMapper) {
        this.focusDatumRepository = focusDatumRepository;
        this.focusDatumMapper = focusDatumMapper;
    }

    /**
     * Save a focusDatum.
     *
     * @param focusDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FocusDatumDTO save(FocusDatumDTO focusDatumDTO, User user) {
        log.debug("Request to save FocusDatum : {}", focusDatumDTO);

        //check for a record from today and update it if it exists
        ZonedDateTime firstMomentOfDay = focusDatumDTO.getTimestamp().withHour(0).withMinute(0).withSecond(0);
        List<FocusDatum> focusDatumList = focusDatumRepository.findByUserAndTimestampBetween(user, firstMomentOfDay, firstMomentOfDay.plusDays(1));


        FocusDatum focusDatum ;
        if( focusDatumList!=null && focusDatumList.isEmpty()){
            focusDatum =focusDatumMapper.toEntity(focusDatumDTO);
        } else {
            focusDatum = focusDatumList.get(0);
            focusDatum.setLevel(focusDatumDTO.getLevel());
            focusDatum.setTimestamp(focusDatumDTO.getTimestamp());
        }
        focusDatum.setUser(user);
        focusDatum = focusDatumRepository.save(focusDatum);
        return focusDatumMapper.toDto(focusDatum);
    }

    /**
     * Get all the focusData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FocusDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FocusData");
        return focusDatumRepository.findAll(pageable)
            .map(focusDatumMapper::toDto);
    }

    /**
     * Get one focusDatum by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FocusDatumDTO findOne(Long id) {
        log.debug("Request to get FocusDatum : {}", id);
        FocusDatum focusDatum = focusDatumRepository.findOne(id);
        return focusDatumMapper.toDto(focusDatum);
    }

}
