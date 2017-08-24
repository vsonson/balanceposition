package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.PathStep;
import com.balpos.app.service.PathStepService;
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
 * REST controller for managing PathStep.
 */
@RestController
@RequestMapping("/api")
public class PathStepResource {

    private final Logger log = LoggerFactory.getLogger(PathStepResource.class);

    private static final String ENTITY_NAME = "pathStep";

    private final PathStepService pathStepService;

    public PathStepResource(PathStepService pathStepService) {
        this.pathStepService = pathStepService;
    }

    /**
     * POST  /path-steps : Create a new pathStep.
     *
     * @param pathStep the pathStep to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pathStep, or with status 400 (Bad Request) if the pathStep has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/path-steps")
    @Timed
    public ResponseEntity<PathStep> createPathStep(@Valid @RequestBody PathStep pathStep) throws URISyntaxException {
        log.debug("REST request to save PathStep : {}", pathStep);
        if (pathStep.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pathStep cannot already have an ID")).body(null);
        }
        PathStep result = pathStepService.save(pathStep);
        return ResponseEntity.created(new URI("/api/path-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /path-steps : Updates an existing pathStep.
     *
     * @param pathStep the pathStep to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pathStep,
     * or with status 400 (Bad Request) if the pathStep is not valid,
     * or with status 500 (Internal Server Error) if the pathStep couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/path-steps")
    @Timed
    public ResponseEntity<PathStep> updatePathStep(@Valid @RequestBody PathStep pathStep) throws URISyntaxException {
        log.debug("REST request to update PathStep : {}", pathStep);
        if (pathStep.getId() == null) {
            return createPathStep(pathStep);
        }
        PathStep result = pathStepService.save(pathStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pathStep.getId().toString()))
            .body(result);
    }

    /**
     * GET  /path-steps : get all the pathSteps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pathSteps in body
     */
    @GetMapping("/path-steps")
    @Timed
    public ResponseEntity<List<PathStep>> getAllPathSteps(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PathSteps");
        Page<PathStep> page = pathStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/path-steps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /path-steps/:id : get the "id" pathStep.
     *
     * @param id the id of the pathStep to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pathStep, or with status 404 (Not Found)
     */
    @GetMapping("/path-steps/{id}")
    @Timed
    public ResponseEntity<PathStep> getPathStep(@PathVariable Long id) {
        log.debug("REST request to get PathStep : {}", id);
        PathStep pathStep = pathStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pathStep));
    }

    /**
     * DELETE  /path-steps/:id : delete the "id" pathStep.
     *
     * @param id the id of the pathStep to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/path-steps/{id}")
    @Timed
    public ResponseEntity<Void> deletePathStep(@PathVariable Long id) {
        log.debug("REST request to delete PathStep : {}", id);
        pathStepService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
