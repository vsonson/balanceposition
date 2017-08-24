package com.balpos.app.service.impl;

import com.balpos.app.service.ProgramStepService;
import com.balpos.app.domain.ProgramStep;
import com.balpos.app.repository.ProgramStepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProgramStep.
 */
@Service
@Transactional
public class ProgramStepServiceImpl implements ProgramStepService{

    private final Logger log = LoggerFactory.getLogger(ProgramStepServiceImpl.class);

    private final ProgramStepRepository programStepRepository;
    public ProgramStepServiceImpl(ProgramStepRepository programStepRepository) {
        this.programStepRepository = programStepRepository;
    }

    /**
     * Save a programStep.
     *
     * @param programStep the entity to save
     * @return the persisted entity
     */
    @Override
    public ProgramStep save(ProgramStep programStep) {
        log.debug("Request to save ProgramStep : {}", programStep);
        return programStepRepository.save(programStep);
    }

    /**
     *  Get all the programSteps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProgramStep> findAll(Pageable pageable) {
        log.debug("Request to get all ProgramSteps");
        return programStepRepository.findAll(pageable);
    }

    /**
     *  Get one programStep by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProgramStep findOne(Long id) {
        log.debug("Request to get ProgramStep : {}", id);
        return programStepRepository.findOne(id);
    }

    /**
     *  Delete the  programStep by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProgramStep : {}", id);
        programStepRepository.delete(id);
    }
}
