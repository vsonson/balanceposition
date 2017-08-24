package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.NotifcationTrigger;
import com.balpos.app.service.NotifcationTriggerService;
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
 * REST controller for managing NotifcationTrigger.
 */
@RestController
@RequestMapping("/api")
public class NotifcationTriggerResource {

    private final Logger log = LoggerFactory.getLogger(NotifcationTriggerResource.class);

    private static final String ENTITY_NAME = "notifcationTrigger";

    private final NotifcationTriggerService notifcationTriggerService;

    public NotifcationTriggerResource(NotifcationTriggerService notifcationTriggerService) {
        this.notifcationTriggerService = notifcationTriggerService;
    }

    /**
     * POST  /notifcation-triggers : Create a new notifcationTrigger.
     *
     * @param notifcationTrigger the notifcationTrigger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notifcationTrigger, or with status 400 (Bad Request) if the notifcationTrigger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notifcation-triggers")
    @Timed
    public ResponseEntity<NotifcationTrigger> createNotifcationTrigger(@RequestBody NotifcationTrigger notifcationTrigger) throws URISyntaxException {
        log.debug("REST request to save NotifcationTrigger : {}", notifcationTrigger);
        if (notifcationTrigger.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new notifcationTrigger cannot already have an ID")).body(null);
        }
        NotifcationTrigger result = notifcationTriggerService.save(notifcationTrigger);
        return ResponseEntity.created(new URI("/api/notifcation-triggers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifcation-triggers : Updates an existing notifcationTrigger.
     *
     * @param notifcationTrigger the notifcationTrigger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notifcationTrigger,
     * or with status 400 (Bad Request) if the notifcationTrigger is not valid,
     * or with status 500 (Internal Server Error) if the notifcationTrigger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notifcation-triggers")
    @Timed
    public ResponseEntity<NotifcationTrigger> updateNotifcationTrigger(@RequestBody NotifcationTrigger notifcationTrigger) throws URISyntaxException {
        log.debug("REST request to update NotifcationTrigger : {}", notifcationTrigger);
        if (notifcationTrigger.getId() == null) {
            return createNotifcationTrigger(notifcationTrigger);
        }
        NotifcationTrigger result = notifcationTriggerService.save(notifcationTrigger);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notifcationTrigger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notifcation-triggers : get all the notifcationTriggers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notifcationTriggers in body
     */
    @GetMapping("/notifcation-triggers")
    @Timed
    public ResponseEntity<List<NotifcationTrigger>> getAllNotifcationTriggers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of NotifcationTriggers");
        Page<NotifcationTrigger> page = notifcationTriggerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notifcation-triggers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notifcation-triggers/:id : get the "id" notifcationTrigger.
     *
     * @param id the id of the notifcationTrigger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notifcationTrigger, or with status 404 (Not Found)
     */
    @GetMapping("/notifcation-triggers/{id}")
    @Timed
    public ResponseEntity<NotifcationTrigger> getNotifcationTrigger(@PathVariable Long id) {
        log.debug("REST request to get NotifcationTrigger : {}", id);
        NotifcationTrigger notifcationTrigger = notifcationTriggerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notifcationTrigger));
    }

    /**
     * DELETE  /notifcation-triggers/:id : delete the "id" notifcationTrigger.
     *
     * @param id the id of the notifcationTrigger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifcation-triggers/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotifcationTrigger(@PathVariable Long id) {
        log.debug("REST request to delete NotifcationTrigger : {}", id);
        notifcationTriggerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
