package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.ThoughtOfDay;
import com.balpos.app.service.ThoughtOfDayService;
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
 * REST controller for managing ThoughtOfDay.
 */
@RestController
@RequestMapping("/api")
public class ThoughtOfDayResource {

    private final Logger log = LoggerFactory.getLogger(ThoughtOfDayResource.class);

    private static final String ENTITY_NAME = "thoughtOfDay";

    private final ThoughtOfDayService thoughtOfDayService;

    public ThoughtOfDayResource(ThoughtOfDayService thoughtOfDayService) {
        this.thoughtOfDayService = thoughtOfDayService;
    }

    /**
     * POST  /thought-of-days : Create a new thoughtOfDay.
     *
     * @param thoughtOfDay the thoughtOfDay to create
     * @return the ResponseEntity with status 201 (Created) and with body the new thoughtOfDay, or with status 400 (Bad Request) if the thoughtOfDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/thought-of-days")
    @Timed
    public ResponseEntity<ThoughtOfDay> createThoughtOfDay(@RequestBody ThoughtOfDay thoughtOfDay) throws URISyntaxException {
        log.debug("REST request to save ThoughtOfDay : {}", thoughtOfDay);
        if (thoughtOfDay.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new thoughtOfDay cannot already have an ID")).body(null);
        }
        ThoughtOfDay result = thoughtOfDayService.save(thoughtOfDay);
        return ResponseEntity.created(new URI("/api/thought-of-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /thought-of-days : Updates an existing thoughtOfDay.
     *
     * @param thoughtOfDay the thoughtOfDay to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated thoughtOfDay,
     * or with status 400 (Bad Request) if the thoughtOfDay is not valid,
     * or with status 500 (Internal Server Error) if the thoughtOfDay couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/thought-of-days")
    @Timed
    public ResponseEntity<ThoughtOfDay> updateThoughtOfDay(@RequestBody ThoughtOfDay thoughtOfDay) throws URISyntaxException {
        log.debug("REST request to update ThoughtOfDay : {}", thoughtOfDay);
        if (thoughtOfDay.getId() == null) {
            return createThoughtOfDay(thoughtOfDay);
        }
        ThoughtOfDay result = thoughtOfDayService.save(thoughtOfDay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, thoughtOfDay.getId().toString()))
            .body(result);
    }

    /**
     * GET  /thought-of-days : get all the thoughtOfDays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of thoughtOfDays in body
     */
    @GetMapping("/thought-of-days")
    @Timed
    public ResponseEntity<List<ThoughtOfDay>> getAllThoughtOfDays(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ThoughtOfDays");
        Page<ThoughtOfDay> page = thoughtOfDayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/thought-of-days");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /thought-of-days/:id : get the "id" thoughtOfDay.
     *
     * @param id the id of the thoughtOfDay to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the thoughtOfDay, or with status 404 (Not Found)
     */
    @GetMapping("/thought-of-days/{id}")
    @Timed
    public ResponseEntity<ThoughtOfDay> getThoughtOfDay(@PathVariable Long id) {
        log.debug("REST request to get ThoughtOfDay : {}", id);
        ThoughtOfDay thoughtOfDay = thoughtOfDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(thoughtOfDay));
    }

    /**
     * DELETE  /thought-of-days/:id : delete the "id" thoughtOfDay.
     *
     * @param id the id of the thoughtOfDay to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/thought-of-days/{id}")
    @Timed
    public ResponseEntity<Void> deleteThoughtOfDay(@PathVariable Long id) {
        log.debug("REST request to delete ThoughtOfDay : {}", id);
        thoughtOfDayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
