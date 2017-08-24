package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.Alert;
import com.balpos.app.service.AlertService;
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
 * REST controller for managing Alert.
 */
@RestController
@RequestMapping("/api")
public class AlertResource {

    private final Logger log = LoggerFactory.getLogger(AlertResource.class);

    private static final String ENTITY_NAME = "alert";

    private final AlertService alertService;

    public AlertResource(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * POST  /alerts : Create a new alert.
     *
     * @param alert the alert to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alert, or with status 400 (Bad Request) if the alert has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alerts")
    @Timed
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) throws URISyntaxException {
        log.debug("REST request to save Alert : {}", alert);
        if (alert.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new alert cannot already have an ID")).body(null);
        }
        Alert result = alertService.save(alert);
        return ResponseEntity.created(new URI("/api/alerts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alerts : Updates an existing alert.
     *
     * @param alert the alert to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alert,
     * or with status 400 (Bad Request) if the alert is not valid,
     * or with status 500 (Internal Server Error) if the alert couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alerts")
    @Timed
    public ResponseEntity<Alert> updateAlert(@RequestBody Alert alert) throws URISyntaxException {
        log.debug("REST request to update Alert : {}", alert);
        if (alert.getId() == null) {
            return createAlert(alert);
        }
        Alert result = alertService.save(alert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alert.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alerts : get all the alerts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of alerts in body
     */
    @GetMapping("/alerts")
    @Timed
    public ResponseEntity<List<Alert>> getAllAlerts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Alerts");
        Page<Alert> page = alertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alerts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /alerts/:id : get the "id" alert.
     *
     * @param id the id of the alert to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alert, or with status 404 (Not Found)
     */
    @GetMapping("/alerts/{id}")
    @Timed
    public ResponseEntity<Alert> getAlert(@PathVariable Long id) {
        log.debug("REST request to get Alert : {}", id);
        Alert alert = alertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alert));
    }

    /**
     * DELETE  /alerts/:id : delete the "id" alert.
     *
     * @param id the id of the alert to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alerts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        log.debug("REST request to delete Alert : {}", id);
        alertService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
