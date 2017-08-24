package com.balpos.app.service;

import com.balpos.app.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Program.
 */
public interface ProgramService {

    /**
     * Save a program.
     *
     * @param program the entity to save
     * @return the persisted entity
     */
    Program save(Program program);

    /**
     *  Get all the programs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Program> findAll(Pageable pageable);

    /**
     *  Get the "id" program.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Program findOne(Long id);

    /**
     *  Delete the "id" program.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
