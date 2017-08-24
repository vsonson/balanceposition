package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.IncentiveHistory;
import com.balpos.app.service.IncentiveHistoryService;
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
 * REST controller for managing IncentiveHistory.
 */
@RestController
@RequestMapping("/api")
public class IncentiveHistoryResource {

    private final Logger log = LoggerFactory.getLogger(IncentiveHistoryResource.class);

    private static final String ENTITY_NAME = "incentiveHistory";

    private final IncentiveHistoryService incentiveHistoryService;

    public IncentiveHistoryResource(IncentiveHistoryService incentiveHistoryService) {
        this.incentiveHistoryService = incentiveHistoryService;
    }

    /**
     * POST  /incentive-histories : Create a new incentiveHistory.
     *
     * @param incentiveHistory the incentiveHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new incentiveHistory, or with status 400 (Bad Request) if the incentiveHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/incentive-histories")
    @Timed
    public ResponseEntity<IncentiveHistory> createIncentiveHistory(@RequestBody IncentiveHistory incentiveHistory) throws URISyntaxException {
        log.debug("REST request to save IncentiveHistory : {}", incentiveHistory);
        if (incentiveHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new incentiveHistory cannot already have an ID")).body(null);
        }
        IncentiveHistory result = incentiveHistoryService.save(incentiveHistory);
        return ResponseEntity.created(new URI("/api/incentive-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /incentive-histories : Updates an existing incentiveHistory.
     *
     * @param incentiveHistory the incentiveHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated incentiveHistory,
     * or with status 400 (Bad Request) if the incentiveHistory is not valid,
     * or with status 500 (Internal Server Error) if the incentiveHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/incentive-histories")
    @Timed
    public ResponseEntity<IncentiveHistory> updateIncentiveHistory(@RequestBody IncentiveHistory incentiveHistory) throws URISyntaxException {
        log.debug("REST request to update IncentiveHistory : {}", incentiveHistory);
        if (incentiveHistory.getId() == null) {
            return createIncentiveHistory(incentiveHistory);
        }
        IncentiveHistory result = incentiveHistoryService.save(incentiveHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, incentiveHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /incentive-histories : get all the incentiveHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of incentiveHistories in body
     */
    @GetMapping("/incentive-histories")
    @Timed
    public ResponseEntity<List<IncentiveHistory>> getAllIncentiveHistories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of IncentiveHistories");
        Page<IncentiveHistory> page = incentiveHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/incentive-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /incentive-histories/:id : get the "id" incentiveHistory.
     *
     * @param id the id of the incentiveHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the incentiveHistory, or with status 404 (Not Found)
     */
    @GetMapping("/incentive-histories/{id}")
    @Timed
    public ResponseEntity<IncentiveHistory> getIncentiveHistory(@PathVariable Long id) {
        log.debug("REST request to get IncentiveHistory : {}", id);
        IncentiveHistory incentiveHistory = incentiveHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(incentiveHistory));
    }

    /**
     * DELETE  /incentive-histories/:id : delete the "id" incentiveHistory.
     *
     * @param id the id of the incentiveHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/incentive-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteIncentiveHistory(@PathVariable Long id) {
        log.debug("REST request to delete IncentiveHistory : {}", id);
        incentiveHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
