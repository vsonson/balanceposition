package com.balpos.app.service.impl;

import com.balpos.app.service.ProgramService;
import com.balpos.app.domain.Program;
import com.balpos.app.repository.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Program.
 */
@Service
@Transactional
public class ProgramServiceImpl implements ProgramService{

    private final Logger log = LoggerFactory.getLogger(ProgramServiceImpl.class);

    private final ProgramRepository programRepository;
    public ProgramServiceImpl(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * Save a program.
     *
     * @param program the entity to save
     * @return the persisted entity
     */
    @Override
    public Program save(Program program) {
        log.debug("Request to save Program : {}", program);
        return programRepository.save(program);
    }

    /**
     *  Get all the programs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Program> findAll(Pageable pageable) {
        log.debug("Request to get all Programs");
        return programRepository.findAll(pageable);
    }

    /**
     *  Get one program by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Program findOne(Long id) {
        log.debug("Request to get Program : {}", id);
        return programRepository.findOne(id);
    }

    /**
     *  Delete the  program by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Program : {}", id);
        programRepository.delete(id);
    }
}
