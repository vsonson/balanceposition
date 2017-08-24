package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.PathWay;
import com.balpos.app.service.PathWayService;
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
 * REST controller for managing PathWay.
 */
@RestController
@RequestMapping("/api")
public class PathWayResource {

    private final Logger log = LoggerFactory.getLogger(PathWayResource.class);

    private static final String ENTITY_NAME = "pathWay";

    private final PathWayService pathWayService;

    public PathWayResource(PathWayService pathWayService) {
        this.pathWayService = pathWayService;
    }

    /**
     * POST  /path-ways : Create a new pathWay.
     *
     * @param pathWay the pathWay to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pathWay, or with status 400 (Bad Request) if the pathWay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/path-ways")
    @Timed
    public ResponseEntity<PathWay> createPathWay(@Valid @RequestBody PathWay pathWay) throws URISyntaxException {
        log.debug("REST request to save PathWay : {}", pathWay);
        if (pathWay.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pathWay cannot already have an ID")).body(null);
        }
        PathWay result = pathWayService.save(pathWay);
        return ResponseEntity.created(new URI("/api/path-ways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /path-ways : Updates an existing pathWay.
     *
     * @param pathWay the pathWay to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pathWay,
     * or with status 400 (Bad Request) if the pathWay is not valid,
     * or with status 500 (Internal Server Error) if the pathWay couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/path-ways")
    @Timed
    public ResponseEntity<PathWay> updatePathWay(@Valid @RequestBody PathWay pathWay) throws URISyntaxException {
        log.debug("REST request to update PathWay : {}", pathWay);
        if (pathWay.getId() == null) {
            return createPathWay(pathWay);
        }
        PathWay result = pathWayService.save(pathWay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pathWay.getId().toString()))
            .body(result);
    }

    /**
     * GET  /path-ways : get all the pathWays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pathWays in body
     */
    @GetMapping("/path-ways")
    @Timed
    public ResponseEntity<List<PathWay>> getAllPathWays(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PathWays");
        Page<PathWay> page = pathWayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/path-ways");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /path-ways/:id : get the "id" pathWay.
     *
     * @param id the id of the pathWay to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pathWay, or with status 404 (Not Found)
     */
    @GetMapping("/path-ways/{id}")
    @Timed
    public ResponseEntity<PathWay> getPathWay(@PathVariable Long id) {
        log.debug("REST request to get PathWay : {}", id);
        PathWay pathWay = pathWayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pathWay));
    }

    /**
     * DELETE  /path-ways/:id : delete the "id" pathWay.
     *
     * @param id the id of the pathWay to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/path-ways/{id}")
    @Timed
    public ResponseEntity<Void> deletePathWay(@PathVariable Long id) {
        log.debug("REST request to delete PathWay : {}", id);
        pathWayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
