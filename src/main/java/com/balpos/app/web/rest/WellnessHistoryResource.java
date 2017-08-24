package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.WellnessHistory;
import com.balpos.app.service.WellnessHistoryService;
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
 * REST controller for managing WellnessHistory.
 */
@RestController
@RequestMapping("/api")
public class WellnessHistoryResource {

    private final Logger log = LoggerFactory.getLogger(WellnessHistoryResource.class);

    private static final String ENTITY_NAME = "wellnessHistory";

    private final WellnessHistoryService wellnessHistoryService;

    public WellnessHistoryResource(WellnessHistoryService wellnessHistoryService) {
        this.wellnessHistoryService = wellnessHistoryService;
    }

    /**
     * POST  /wellness-histories : Create a new wellnessHistory.
     *
     * @param wellnessHistory the wellnessHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wellnessHistory, or with status 400 (Bad Request) if the wellnessHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wellness-histories")
    @Timed
    public ResponseEntity<WellnessHistory> createWellnessHistory(@RequestBody WellnessHistory wellnessHistory) throws URISyntaxException {
        log.debug("REST request to save WellnessHistory : {}", wellnessHistory);
        if (wellnessHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wellnessHistory cannot already have an ID")).body(null);
        }
        WellnessHistory result = wellnessHistoryService.save(wellnessHistory);
        return ResponseEntity.created(new URI("/api/wellness-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wellness-histories : Updates an existing wellnessHistory.
     *
     * @param wellnessHistory the wellnessHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wellnessHistory,
     * or with status 400 (Bad Request) if the wellnessHistory is not valid,
     * or with status 500 (Internal Server Error) if the wellnessHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wellness-histories")
    @Timed
    public ResponseEntity<WellnessHistory> updateWellnessHistory(@RequestBody WellnessHistory wellnessHistory) throws URISyntaxException {
        log.debug("REST request to update WellnessHistory : {}", wellnessHistory);
        if (wellnessHistory.getId() == null) {
            return createWellnessHistory(wellnessHistory);
        }
        WellnessHistory result = wellnessHistoryService.save(wellnessHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wellnessHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wellness-histories : get all the wellnessHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wellnessHistories in body
     */
    @GetMapping("/wellness-histories")
    @Timed
    public ResponseEntity<List<WellnessHistory>> getAllWellnessHistories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WellnessHistories");
        Page<WellnessHistory> page = wellnessHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wellness-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wellness-histories/:id : get the "id" wellnessHistory.
     *
     * @param id the id of the wellnessHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wellnessHistory, or with status 404 (Not Found)
     */
    @GetMapping("/wellness-histories/{id}")
    @Timed
    public ResponseEntity<WellnessHistory> getWellnessHistory(@PathVariable Long id) {
        log.debug("REST request to get WellnessHistory : {}", id);
        WellnessHistory wellnessHistory = wellnessHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wellnessHistory));
    }

    /**
     * DELETE  /wellness-histories/:id : delete the "id" wellnessHistory.
     *
     * @param id the id of the wellnessHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wellness-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteWellnessHistory(@PathVariable Long id) {
        log.debug("REST request to delete WellnessHistory : {}", id);
        wellnessHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
