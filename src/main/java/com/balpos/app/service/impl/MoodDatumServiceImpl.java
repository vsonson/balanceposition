package com.balpos.app.service.impl;

import com.balpos.app.domain.User;
import com.balpos.app.service.MoodDatumService;
import com.balpos.app.domain.MoodDatum;
import com.balpos.app.repository.MoodDatumRepository;
import com.balpos.app.service.dto.MoodDatumDTO;
import com.balpos.app.service.mapper.MoodDatumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MoodDatum.
 */
@Service
@Transactional
public class MoodDatumServiceImpl implements MoodDatumService{

    private final Logger log = LoggerFactory.getLogger(MoodDatumServiceImpl.class);

    private final MoodDatumRepository moodDatumRepository;

    private final MoodDatumMapper moodDatumMapper;
    public MoodDatumServiceImpl(MoodDatumRepository moodDatumRepository, MoodDatumMapper moodDatumMapper) {
        this.moodDatumRepository = moodDatumRepository;
        this.moodDatumMapper = moodDatumMapper;
    }

    /**
     * Save a moodDatum.
     *
     * @param moodDatumDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MoodDatumDTO save(MoodDatumDTO moodDatumDTO, User user) {
        log.debug("Request to save MoodDatum : {}", moodDatumDTO);
        MoodDatum moodDatum = moodDatumMapper.toEntity(moodDatumDTO);
        moodDatum.setUser(user);
        moodDatum = moodDatumRepository.save(moodDatum);
        return moodDatumMapper.toDto(moodDatum);
    }

    /**
     *  Get all the moodData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MoodDatumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MoodData");
        return moodDatumRepository.findAll(pageable)
            .map(moodDatumMapper::toDto);
    }

    /**
     *  Get one moodDatum by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MoodDatumDTO findOne(Long id) {
        log.debug("Request to get MoodDatum : {}", id);
        MoodDatum moodDatum = moodDatumRepository.findOne(id);
        return moodDatumMapper.toDto(moodDatum);
    }

}
