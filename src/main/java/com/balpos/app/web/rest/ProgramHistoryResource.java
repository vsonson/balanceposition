package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.ProgramHistory;
import com.balpos.app.service.ProgramHistoryService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProgramHistory.
 */
@RestController
@RequestMapping("/api")
public class ProgramHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ProgramHistoryResource.class);

    private static final String ENTITY_NAME = "programHistory";

    private final ProgramHistoryService programHistoryService;

    public ProgramHistoryResource(ProgramHistoryService programHistoryService) {
        this.programHistoryService = programHistoryService;
    }

    /**
     * POST  /program-histories : Create a new programHistory.
     *
     * @param programHistory the programHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programHistory, or with status 400 (Bad Request) if the programHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/program-histories")
    @Timed
    public ResponseEntity<ProgramHistory> createProgramHistory(@RequestBody ProgramHistory programHistory) throws URISyntaxException {
        log.debug("REST request to save ProgramHistory : {}", programHistory);
        if (programHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programHistory cannot already have an ID")).body(null);
        }
        ProgramHistory result = programHistoryService.save(programHistory);
        return ResponseEntity.created(new URI("/api/program-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /program-histories : Updates an existing programHistory.
     *
     * @param programHistory the programHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programHistory,
     * or with status 400 (Bad Request) if the programHistory is not valid,
     * or with status 500 (Internal Server Error) if the programHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/program-histories")
    @Timed
    public ResponseEntity<ProgramHistory> updateProgramHistory(@RequestBody ProgramHistory programHistory) throws URISyntaxException {
        log.debug("REST request to update ProgramHistory : {}", programHistory);
        if (programHistory.getId() == null) {
            return createProgramHistory(programHistory);
        }
        ProgramHistory result = programHistoryService.save(programHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /program-histories : get all the programHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of programHistories in body
     */
    @GetMapping("/program-histories")
    @Timed
    public ResponseEntity<List<ProgramHistory>> getAllProgramHistories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProgramHistories");
        Page<ProgramHistory> page = programHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/program-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /program-histories/:id : get the "id" programHistory.
     *
     * @param id the id of the programHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programHistory, or with status 404 (Not Found)
     */
    @GetMapping("/program-histories/{id}")
    @Timed
    public ResponseEntity<ProgramHistory> getProgramHistory(@PathVariable Long id) {
        log.debug("REST request to get ProgramHistory : {}", id);
        ProgramHistory programHistory = programHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programHistory));
    }

    /**
     * DELETE  /program-histories/:id : delete the "id" programHistory.
     *
     * @param id the id of the programHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/program-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgramHistory(@PathVariable Long id) {
        log.debug("REST request to delete ProgramHistory : {}", id);
        programHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
