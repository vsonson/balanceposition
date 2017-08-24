package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.TrackMetric;
import com.balpos.app.service.TrackMetricService;
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
 * REST controller for managing TrackMetric.
 */
@RestController
@RequestMapping("/api")
public class TrackMetricResource {

    private final Logger log = LoggerFactory.getLogger(TrackMetricResource.class);

    private static final String ENTITY_NAME = "trackMetric";

    private final TrackMetricService trackMetricService;

    public TrackMetricResource(TrackMetricService trackMetricService) {
        this.trackMetricService = trackMetricService;
    }

    /**
     * POST  /track-metrics : Create a new trackMetric.
     *
     * @param trackMetric the trackMetric to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trackMetric, or with status 400 (Bad Request) if the trackMetric has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/track-metrics")
    @Timed
    public ResponseEntity<TrackMetric> createTrackMetric(@Valid @RequestBody TrackMetric trackMetric) throws URISyntaxException {
        log.debug("REST request to save TrackMetric : {}", trackMetric);
        if (trackMetric.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trackMetric cannot already have an ID")).body(null);
        }
        TrackMetric result = trackMetricService.save(trackMetric);
        return ResponseEntity.created(new URI("/api/track-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /track-metrics : Updates an existing trackMetric.
     *
     * @param trackMetric the trackMetric to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trackMetric,
     * or with status 400 (Bad Request) if the trackMetric is not valid,
     * or with status 500 (Internal Server Error) if the trackMetric couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/track-metrics")
    @Timed
    public ResponseEntity<TrackMetric> updateTrackMetric(@Valid @RequestBody TrackMetric trackMetric) throws URISyntaxException {
        log.debug("REST request to update TrackMetric : {}", trackMetric);
        if (trackMetric.getId() == null) {
            return createTrackMetric(trackMetric);
        }
        TrackMetric result = trackMetricService.save(trackMetric);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trackMetric.getId().toString()))
            .body(result);
    }

    /**
     * GET  /track-metrics : get all the trackMetrics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trackMetrics in body
     */
    @GetMapping("/track-metrics")
    @Timed
    public ResponseEntity<List<TrackMetric>> getAllTrackMetrics(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TrackMetrics");
        Page<TrackMetric> page = trackMetricService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/track-metrics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /track-metrics/:id : get the "id" trackMetric.
     *
     * @param id the id of the trackMetric to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trackMetric, or with status 404 (Not Found)
     */
    @GetMapping("/track-metrics/{id}")
    @Timed
    public ResponseEntity<TrackMetric> getTrackMetric(@PathVariable Long id) {
        log.debug("REST request to get TrackMetric : {}", id);
        TrackMetric trackMetric = trackMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trackMetric));
    }

    /**
     * DELETE  /track-metrics/:id : delete the "id" trackMetric.
     *
     * @param id the id of the trackMetric to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/track-metrics/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrackMetric(@PathVariable Long id) {
        log.debug("REST request to delete TrackMetric : {}", id);
        trackMetricService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
