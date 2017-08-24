package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.IncentiveAction;
import com.balpos.app.service.IncentiveActionService;
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
 * REST controller for managing IncentiveAction.
 */
@RestController
@RequestMapping("/api")
public class IncentiveActionResource {

    private final Logger log = LoggerFactory.getLogger(IncentiveActionResource.class);

    private static final String ENTITY_NAME = "incentiveAction";

    private final IncentiveActionService incentiveActionService;

    public IncentiveActionResource(IncentiveActionService incentiveActionService) {
        this.incentiveActionService = incentiveActionService;
    }

    /**
     * POST  /incentive-actions : Create a new incentiveAction.
     *
     * @param incentiveAction the incentiveAction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new incentiveAction, or with status 400 (Bad Request) if the incentiveAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/incentive-actions")
    @Timed
    public ResponseEntity<IncentiveAction> createIncentiveAction(@Valid @RequestBody IncentiveAction incentiveAction) throws URISyntaxException {
        log.debug("REST request to save IncentiveAction : {}", incentiveAction);
        if (incentiveAction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new incentiveAction cannot already have an ID")).body(null);
        }
        IncentiveAction result = incentiveActionService.save(incentiveAction);
        return ResponseEntity.created(new URI("/api/incentive-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /incentive-actions : Updates an existing incentiveAction.
     *
     * @param incentiveAction the incentiveAction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated incentiveAction,
     * or with status 400 (Bad Request) if the incentiveAction is not valid,
     * or with status 500 (Internal Server Error) if the incentiveAction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/incentive-actions")
    @Timed
    public ResponseEntity<IncentiveAction> updateIncentiveAction(@Valid @RequestBody IncentiveAction incentiveAction) throws URISyntaxException {
        log.debug("REST request to update IncentiveAction : {}", incentiveAction);
        if (incentiveAction.getId() == null) {
            return createIncentiveAction(incentiveAction);
        }
        IncentiveAction result = incentiveActionService.save(incentiveAction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, incentiveAction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /incentive-actions : get all the incentiveActions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of incentiveActions in body
     */
    @GetMapping("/incentive-actions")
    @Timed
    public ResponseEntity<List<IncentiveAction>> getAllIncentiveActions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of IncentiveActions");
        Page<IncentiveAction> page = incentiveActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/incentive-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /incentive-actions/:id : get the "id" incentiveAction.
     *
     * @param id the id of the incentiveAction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the incentiveAction, or with status 404 (Not Found)
     */
    @GetMapping("/incentive-actions/{id}")
    @Timed
    public ResponseEntity<IncentiveAction> getIncentiveAction(@PathVariable Long id) {
        log.debug("REST request to get IncentiveAction : {}", id);
        IncentiveAction incentiveAction = incentiveActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(incentiveAction));
    }

    /**
     * DELETE  /incentive-actions/:id : delete the "id" incentiveAction.
     *
     * @param id the id of the incentiveAction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/incentive-actions/{id}")
    @Timed
    public ResponseEntity<Void> deleteIncentiveAction(@PathVariable Long id) {
        log.debug("REST request to delete IncentiveAction : {}", id);
        incentiveActionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
