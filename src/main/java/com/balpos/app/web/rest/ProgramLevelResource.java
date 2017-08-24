package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.ProgramLevel;
import com.balpos.app.service.ProgramLevelService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProgramLevel.
 */
@RestController
@RequestMapping("/api")
public class ProgramLevelResource {

    private final Logger log = LoggerFactory.getLogger(ProgramLevelResource.class);

    private static final String ENTITY_NAME = "programLevel";

    private final ProgramLevelService programLevelService;

    public ProgramLevelResource(ProgramLevelService programLevelService) {
        this.programLevelService = programLevelService;
    }

    /**
     * POST  /program-levels : Create a new programLevel.
     *
     * @param programLevel the programLevel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programLevel, or with status 400 (Bad Request) if the programLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/program-levels")
    @Timed
    public ResponseEntity<ProgramLevel> createProgramLevel(@Valid @RequestBody ProgramLevel programLevel) throws URISyntaxException {
        log.debug("REST request to save ProgramLevel : {}", programLevel);
        if (programLevel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programLevel cannot already have an ID")).body(null);
        }
        ProgramLevel result = programLevelService.save(programLevel);
        return ResponseEntity.created(new URI("/api/program-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /program-levels : Updates an existing programLevel.
     *
     * @param programLevel the programLevel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programLevel,
     * or with status 400 (Bad Request) if the programLevel is not valid,
     * or with status 500 (Internal Server Error) if the programLevel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/program-levels")
    @Timed
    public ResponseEntity<ProgramLevel> updateProgramLevel(@Valid @RequestBody ProgramLevel programLevel) throws URISyntaxException {
        log.debug("REST request to update ProgramLevel : {}", programLevel);
        if (programLevel.getId() == null) {
            return createProgramLevel(programLevel);
        }
        ProgramLevel result = programLevelService.save(programLevel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programLevel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /program-levels : get all the programLevels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of programLevels in body
     */
    @GetMapping("/program-levels")
    @Timed
    public ResponseEntity<List<ProgramLevel>> getAllProgramLevels(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProgramLevels");
        Page<ProgramLevel> page = programLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/program-levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /program-levels/:id : get the "id" programLevel.
     *
     * @param id the id of the programLevel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programLevel, or with status 404 (Not Found)
     */
    @GetMapping("/program-levels/{id}")
    @Timed
    public ResponseEntity<ProgramLevel> getProgramLevel(@PathVariable Long id) {
        log.debug("REST request to get ProgramLevel : {}", id);
        ProgramLevel programLevel = programLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programLevel));
    }

    /**
     * DELETE  /program-levels/:id : delete the "id" programLevel.
     *
     * @param id the id of the programLevel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/program-levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgramLevel(@PathVariable Long id) {
        log.debug("REST request to delete ProgramLevel : {}", id);
        programLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
