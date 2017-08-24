package com.balpos.app.service.impl;

import com.balpos.app.service.ThoughtOfDayService;
import com.balpos.app.domain.ThoughtOfDay;
import com.balpos.app.repository.ThoughtOfDayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ThoughtOfDay.
 */
@Service
@Transactional
public class ThoughtOfDayServiceImpl implements ThoughtOfDayService{

    private final Logger log = LoggerFactory.getLogger(ThoughtOfDayServiceImpl.class);

    private final ThoughtOfDayRepository thoughtOfDayRepository;
    public ThoughtOfDayServiceImpl(ThoughtOfDayRepository thoughtOfDayRepository) {
        this.thoughtOfDayRepository = thoughtOfDayRepository;
    }

    /**
     * Save a thoughtOfDay.
     *
     * @param thoughtOfDay the entity to save
     * @return the persisted entity
     */
    @Override
    public ThoughtOfDay save(ThoughtOfDay thoughtOfDay) {
        log.debug("Request to save ThoughtOfDay : {}", thoughtOfDay);
        return thoughtOfDayRepository.save(thoughtOfDay);
    }

    /**
     *  Get all the thoughtOfDays.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ThoughtOfDay> findAll(Pageable pageable) {
        log.debug("Request to get all ThoughtOfDays");
        return thoughtOfDayRepository.findAll(pageable);
    }

    /**
     *  Get one thoughtOfDay by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ThoughtOfDay findOne(Long id) {
        log.debug("Request to get ThoughtOfDay : {}", id);
        return thoughtOfDayRepository.findOne(id);
    }

    /**
     *  Delete the  thoughtOfDay by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ThoughtOfDay : {}", id);
        thoughtOfDayRepository.delete(id);
    }
}
