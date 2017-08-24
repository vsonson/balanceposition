package com.balpos.app.service.impl;

import com.balpos.app.service.ProgramLevelService;
import com.balpos.app.domain.ProgramLevel;
import com.balpos.app.repository.ProgramLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProgramLevel.
 */
@Service
@Transactional
public class ProgramLevelServiceImpl implements ProgramLevelService{

    private final Logger log = LoggerFactory.getLogger(ProgramLevelServiceImpl.class);

    private final ProgramLevelRepository programLevelRepository;
    public ProgramLevelServiceImpl(ProgramLevelRepository programLevelRepository) {
        this.programLevelRepository = programLevelRepository;
    }

    /**
     * Save a programLevel.
     *
     * @param programLevel the entity to save
     * @return the persisted entity
     */
    @Override
    public ProgramLevel save(ProgramLevel programLevel) {
        log.debug("Request to save ProgramLevel : {}", programLevel);
        return programLevelRepository.save(programLevel);
    }

    /**
     *  Get all the programLevels.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProgramLevel> findAll(Pageable pageable) {
        log.debug("Request to get all ProgramLevels");
        return programLevelRepository.findAll(pageable);
    }

    /**
     *  Get one programLevel by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProgramLevel findOne(Long id) {
        log.debug("Request to get ProgramLevel : {}", id);
        return programLevelRepository.findOne(id);
    }

    /**
     *  Delete the  programLevel by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProgramLevel : {}", id);
        programLevelRepository.delete(id);
    }
}
