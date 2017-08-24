package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.Incentive;
import com.balpos.app.service.IncentiveService;
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
 * REST controller for managing Incentive.
 */
@RestController
@RequestMapping("/api")
public class IncentiveResource {

    private final Logger log = LoggerFactory.getLogger(IncentiveResource.class);

    private static final String ENTITY_NAME = "incentive";

    private final IncentiveService incentiveService;

    public IncentiveResource(IncentiveService incentiveService) {
        this.incentiveService = incentiveService;
    }

    /**
     * POST  /incentives : Create a new incentive.
     *
     * @param incentive the incentive to create
     * @return the ResponseEntity with status 201 (Created) and with body the new incentive, or with status 400 (Bad Request) if the incentive has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/incentives")
    @Timed
    public ResponseEntity<Incentive> createIncentive(@Valid @RequestBody Incentive incentive) throws URISyntaxException {
        log.debug("REST request to save Incentive : {}", incentive);
        if (incentive.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new incentive cannot already have an ID")).body(null);
        }
        Incentive result = incentiveService.save(incentive);
        return ResponseEntity.created(new URI("/api/incentives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /incentives : Updates an existing incentive.
     *
     * @param incentive the incentive to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated incentive,
     * or with status 400 (Bad Request) if the incentive is not valid,
     * or with status 500 (Internal Server Error) if the incentive couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/incentives")
    @Timed
    public ResponseEntity<Incentive> updateIncentive(@Valid @RequestBody Incentive incentive) throws URISyntaxException {
        log.debug("REST request to update Incentive : {}", incentive);
        if (incentive.getId() == null) {
            return createIncentive(incentive);
        }
        Incentive result = incentiveService.save(incentive);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, incentive.getId().toString()))
            .body(result);
    }

    /**
     * GET  /incentives : get all the incentives.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of incentives in body
     */
    @GetMapping("/incentives")
    @Timed
    public ResponseEntity<List<Incentive>> getAllIncentives(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Incentives");
        Page<Incentive> page = incentiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/incentives");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /incentives/:id : get the "id" incentive.
     *
     * @param id the id of the incentive to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the incentive, or with status 404 (Not Found)
     */
    @GetMapping("/incentives/{id}")
    @Timed
    public ResponseEntity<Incentive> getIncentive(@PathVariable Long id) {
        log.debug("REST request to get Incentive : {}", id);
        Incentive incentive = incentiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(incentive));
    }

    /**
     * DELETE  /incentives/:id : delete the "id" incentive.
     *
     * @param id the id of the incentive to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/incentives/{id}")
    @Timed
    public ResponseEntity<Void> deleteIncentive(@PathVariable Long id) {
        log.debug("REST request to delete Incentive : {}", id);
        incentiveService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
