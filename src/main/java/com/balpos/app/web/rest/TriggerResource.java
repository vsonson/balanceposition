package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.Trigger;
import com.balpos.app.service.TriggerService;
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
 * REST controller for managing Trigger.
 */
@RestController
@RequestMapping("/api")
public class TriggerResource {

    private final Logger log = LoggerFactory.getLogger(TriggerResource.class);

    private static final String ENTITY_NAME = "trigger";

    private final TriggerService triggerService;

    public TriggerResource(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

    /**
     * POST  /triggers : Create a new trigger.
     *
     * @param trigger the trigger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trigger, or with status 400 (Bad Request) if the trigger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/triggers")
    @Timed
    public ResponseEntity<Trigger> createTrigger(@Valid @RequestBody Trigger trigger) throws URISyntaxException {
        log.debug("REST request to save Trigger : {}", trigger);
        if (trigger.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trigger cannot already have an ID")).body(null);
        }
        Trigger result = triggerService.save(trigger);
        return ResponseEntity.created(new URI("/api/triggers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /triggers : Updates an existing trigger.
     *
     * @param trigger the trigger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trigger,
     * or with status 400 (Bad Request) if the trigger is not valid,
     * or with status 500 (Internal Server Error) if the trigger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/triggers")
    @Timed
    public ResponseEntity<Trigger> updateTrigger(@Valid @RequestBody Trigger trigger) throws URISyntaxException {
        log.debug("REST request to update Trigger : {}", trigger);
        if (trigger.getId() == null) {
            return createTrigger(trigger);
        }
        Trigger result = triggerService.save(trigger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trigger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /triggers : get all the triggers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of triggers in body
     */
    @GetMapping("/triggers")
    @Timed
    public ResponseEntity<List<Trigger>> getAllTriggers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Triggers");
        Page<Trigger> page = triggerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/triggers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /triggers/:id : get the "id" trigger.
     *
     * @param id the id of the trigger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trigger, or with status 404 (Not Found)
     */
    @GetMapping("/triggers/{id}")
    @Timed
    public ResponseEntity<Trigger> getTrigger(@PathVariable Long id) {
        log.debug("REST request to get Trigger : {}", id);
        Trigger trigger = triggerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trigger));
    }

    /**
     * DELETE  /triggers/:id : delete the "id" trigger.
     *
     * @param id the id of the trigger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/triggers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrigger(@PathVariable Long id) {
        log.debug("REST request to delete Trigger : {}", id);
        triggerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
