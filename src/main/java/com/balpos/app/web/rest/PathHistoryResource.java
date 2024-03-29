package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.PathHistory;
import com.balpos.app.service.PathHistoryService;
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
 * REST controller for managing PathHistory.
 */
@RestController
@RequestMapping("/api")
public class PathHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PathHistoryResource.class);

    private static final String ENTITY_NAME = "pathHistory";

    private final PathHistoryService pathHistoryService;

    public PathHistoryResource(PathHistoryService pathHistoryService) {
        this.pathHistoryService = pathHistoryService;
    }

    /**
     * POST  /path-histories : Create a new pathHistory.
     *
     * @param pathHistory the pathHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pathHistory, or with status 400 (Bad Request) if the pathHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/path-histories")
    @Timed
    public ResponseEntity<PathHistory> createPathHistory(@RequestBody PathHistory pathHistory) throws URISyntaxException {
        log.debug("REST request to save PathHistory : {}", pathHistory);
        if (pathHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pathHistory cannot already have an ID")).body(null);
        }
        PathHistory result = pathHistoryService.save(pathHistory);
        return ResponseEntity.created(new URI("/api/path-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /path-histories : Updates an existing pathHistory.
     *
     * @param pathHistory the pathHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pathHistory,
     * or with status 400 (Bad Request) if the pathHistory is not valid,
     * or with status 500 (Internal Server Error) if the pathHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/path-histories")
    @Timed
    public ResponseEntity<PathHistory> updatePathHistory(@RequestBody PathHistory pathHistory) throws URISyntaxException {
        log.debug("REST request to update PathHistory : {}", pathHistory);
        if (pathHistory.getId() == null) {
            return createPathHistory(pathHistory);
        }
        PathHistory result = pathHistoryService.save(pathHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pathHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /path-histories : get all the pathHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pathHistories in body
     */
    @GetMapping("/path-histories")
    @Timed
    public ResponseEntity<List<PathHistory>> getAllPathHistories(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PathHistories");
        Page<PathHistory> page = pathHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/path-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /path-histories/:id : get the "id" pathHistory.
     *
     * @param id the id of the pathHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pathHistory, or with status 404 (Not Found)
     */
    @GetMapping("/path-histories/{id}")
    @Timed
    public ResponseEntity<PathHistory> getPathHistory(@PathVariable Long id) {
        log.debug("REST request to get PathHistory : {}", id);
        PathHistory pathHistory = pathHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pathHistory));
    }

    /**
     * DELETE  /path-histories/:id : delete the "id" pathHistory.
     *
     * @param id the id of the pathHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/path-histories/{id}")
    @Timed
    public ResponseEntity<Void> deletePathHistory(@PathVariable Long id) {
        log.debug("REST request to delete PathHistory : {}", id);
        pathHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
