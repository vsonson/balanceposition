package com.balpos.app.service.impl;

import com.balpos.app.domain.BodyDatum;
import com.balpos.app.domain.SleepDatum;
import com.balpos.app.domain.User;
import com.balpos.app.repository.BodyDatumRepository;
import com.balpos.app.service.BodyDatumService;
import com.balpos.app.service.dto.BodyDatumDTO;
import com.balpos.app.service.mapper.BodyDatumMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Service Implementation for managing BodyDatum.
 */
@Service
@Transactional
@Slf4j
public class BodyDatumServiceImpl implements BodyDatumService {


    private final BodyDatumRepository bodyDatumRepository;

    private final BodyDatumMapper bodyDatumMapper;

    public BodyDatumServiceImpl(BodyDatumRepository bodyDatumRepository, BodyDatumMapper bodyDatumMapper) {
        this.bodyDatumRepository = bodyDatumRepository;
        this.bodyDatumMapper = bodyDatumMapper;
    }

    /**
     * Save a bodyDatum.
     *
     * @param bodyDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BodyDatumDTO save(BodyDatumDTO bodyDatumDTO, User user) {
        log.debug("Request to save BodyDatum : {}", bodyDatumDTO);

        //check for a record from today and update it if it exists
        LocalDateTime firstMomentOfDay = bodyDatumDTO.getTimestamp().atStartOfDay();
        List<BodyDatum> bodyDatumList = bodyDatumRepository.findByUserAndTimestampBetween(user, firstMomentOfDay, firstMomentOfDay.plusDays(1));

        BodyDatum bodyDatum;
        if (bodyDatumList != null && !bodyDatumList.isEmpty()) {
            bodyDatum = bodyDatumList.get(0);
            bodyDatum.setTimestamp(bodyDatumDTO.getTimestamp());
            bodyDatum.setDigestive(bodyDatumDTO.getDigestive());
            bodyDatum.setHeadache(bodyDatumDTO.getHeadache());
        } else {
            bodyDatum = bodyDatumMapper.toEntity(bodyDatumDTO);
        }

        bodyDatum.setUser(user);
        bodyDatum = bodyDatumRepository.save(bodyDatum);
        return bodyDatumMapper.toDto(bodyDatum);
    }

    /**
     * Get all the bodyData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BodyDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BodyData");
        return bodyDatumRepository.findAll(pageable)
            .map(bodyDatumMapper::toDto);
    }
}
