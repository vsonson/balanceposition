package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.ProgramStep;
import com.balpos.app.service.ProgramStepService;
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
 * REST controller for managing ProgramStep.
 */
@RestController
@RequestMapping("/api")
public class ProgramStepResource {

    private final Logger log = LoggerFactory.getLogger(ProgramStepResource.class);

    private static final String ENTITY_NAME = "programStep";

    private final ProgramStepService programStepService;

    public ProgramStepResource(ProgramStepService programStepService) {
        this.programStepService = programStepService;
    }

    /**
     * POST  /program-steps : Create a new programStep.
     *
     * @param programStep the programStep to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programStep, or with status 400 (Bad Request) if the programStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/program-steps")
    @Timed
    public ResponseEntity<ProgramStep> createProgramStep(@Valid @RequestBody ProgramStep programStep) throws URISyntaxException {
        log.debug("REST request to save ProgramStep : {}", programStep);
        if (programStep.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programStep cannot already have an ID")).body(null);
        }
        ProgramStep result = programStepService.save(programStep);
        return ResponseEntity.created(new URI("/api/program-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /program-steps : Updates an existing programStep.
     *
     * @param programStep the programStep to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programStep,
     * or with status 400 (Bad Request) if the programStep is not valid,
     * or with status 500 (Internal Server Error) if the programStep couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/program-steps")
    @Timed
    public ResponseEntity<ProgramStep> updateProgramStep(@Valid @RequestBody ProgramStep programStep) throws URISyntaxException {
        log.debug("REST request to update ProgramStep : {}", programStep);
        if (programStep.getId() == null) {
            return createProgramStep(programStep);
        }
        ProgramStep result = programStepService.save(programStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programStep.getId().toString()))
            .body(result);
    }

    /**
     * GET  /program-steps : get all the programSteps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of programSteps in body
     */
    @GetMapping("/program-steps")
    @Timed
    public ResponseEntity<List<ProgramStep>> getAllProgramSteps(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProgramSteps");
        Page<ProgramStep> page = programStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/program-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /program-steps/:id : get the "id" programStep.
     *
     * @param id the id of the programStep to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programStep, or with status 404 (Not Found)
     */
    @GetMapping("/program-steps/{id}")
    @Timed
    public ResponseEntity<ProgramStep> getProgramStep(@PathVariable Long id) {
        log.debug("REST request to get ProgramStep : {}", id);
        ProgramStep programStep = programStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programStep));
    }

    /**
     * DELETE  /program-steps/:id : delete the "id" programStep.
     *
     * @param id the id of the programStep to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/program-steps/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgramStep(@PathVariable Long id) {
        log.debug("REST request to delete ProgramStep : {}", id);
        programStepService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
