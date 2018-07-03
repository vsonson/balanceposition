package com.balpos.app.service.impl;

import com.balpos.app.domain.StressDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.StressDatumRepository;
import com.balpos.app.service.StressDatumService;
import com.balpos.app.service.dto.StressDatumDTO;
import com.balpos.app.service.mapper.StressDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Service Implementation for managing StressDatum.
 */
@Service
@Transactional
@Slf4j
public class StressDatumServiceImpl implements StressDatumService {

    private final StressDatumRepository stressDatumRepository;

    private final StressDatumMapper stressDatumMapper;

    public StressDatumServiceImpl(StressDatumRepository stressDatumRepository, StressDatumMapper stressDatumMapper) {
        this.stressDatumRepository = stressDatumRepository;
        this.stressDatumMapper = stressDatumMapper;
    }

    /**
     * Save a stressDatum.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public StressDatumDTO save(StressDatumDTO stressDatumDTO, User user) {
        log.debug("Request to save StressDatum : {}", stressDatumDTO);

        //check for a record from today and update it if it exists
        ZonedDateTime firstMomentOfDay = stressDatumDTO.getTimestamp().withHour(0).withMinute(0).withSecond(0);
        List<StressDatum> stressDatumList = stressDatumRepository.findByUserAndTimestampBetween(user, firstMomentOfDay, firstMomentOfDay.plusDays(1));

        StressDatum stressDatum;
        if (stressDatumList != null && !stressDatumList.isEmpty()) {
            stressDatum = stressDatumList.get(0);
            stressDatum.setTimestamp(stressDatumDTO.getTimestamp());
            stressDatum.setValue(stressDatumDTO.getValue());
        } else {
            stressDatum = stressDatumMapper.toEntity(stressDatumDTO);
        }
        stressDatum.setUser(user);

        stressDatum = stressDatumRepository.save(stressDatum);
        return stressDatumMapper.toDto(stressDatum);
    }

    /**
     * Get all the stressData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StressDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StressData");
        return stressDatumRepository.findAll(pageable)
            .map(stressDatumMapper::toDto);
    }

    /**
     * Get one stressDatum by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StressDatumDTO findOne(Long id) {
        log.debug("Request to get StressDatum : {}", id);
        StressDatum stressDatum = stressDatumRepository.findOne(id);
        return stressDatumMapper.toDto(stressDatum);
    }
}
