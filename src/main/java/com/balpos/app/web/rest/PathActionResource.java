package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.PathAction;
import com.balpos.app.service.PathActionService;
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
 * REST controller for managing PathAction.
 */
@RestController
@RequestMapping("/api")
public class PathActionResource {

    private final Logger log = LoggerFactory.getLogger(PathActionResource.class);

    private static final String ENTITY_NAME = "pathAction";

    private final PathActionService pathActionService;

    public PathActionResource(PathActionService pathActionService) {
        this.pathActionService = pathActionService;
    }

    /**
     * POST  /path-actions : Create a new pathAction.
     *
     * @param pathAction the pathAction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pathAction, or with status 400 (Bad Request) if the pathAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/path-actions")
    @Timed
    public ResponseEntity<PathAction> createPathAction(@Valid @RequestBody PathAction pathAction) throws URISyntaxException {
        log.debug("REST request to save PathAction : {}", pathAction);
        if (pathAction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pathAction cannot already have an ID")).body(null);
        }
        PathAction result = pathActionService.save(pathAction);
        return ResponseEntity.created(new URI("/api/path-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /path-actions : Updates an existing pathAction.
     *
     * @param pathAction the pathAction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pathAction,
     * or with status 400 (Bad Request) if the pathAction is not valid,
     * or with status 500 (Internal Server Error) if the pathAction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/path-actions")
    @Timed
    public ResponseEntity<PathAction> updatePathAction(@Valid @RequestBody PathAction pathAction) throws URISyntaxException {
        log.debug("REST request to update PathAction : {}", pathAction);
        if (pathAction.getId() == null) {
            return createPathAction(pathAction);
        }
        PathAction result = pathActionService.save(pathAction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pathAction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /path-actions : get all the pathActions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pathActions in body
     */
    @GetMapping("/path-actions")
    @Timed
    public ResponseEntity<List<PathAction>> getAllPathActions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PathActions");
        Page<PathAction> page = pathActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/path-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /path-actions/:id : get the "id" pathAction.
     *
     * @param id the id of the pathAction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pathAction, or with status 404 (Not Found)
     */
    @GetMapping("/path-actions/{id}")
    @Timed
    public ResponseEntity<PathAction> getPathAction(@PathVariable Long id) {
        log.debug("REST request to get PathAction : {}", id);
        PathAction pathAction = pathActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pathAction));
    }

    /**
     * DELETE  /path-actions/:id : delete the "id" pathAction.
     *
     * @param id the id of the pathAction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/path-actions/{id}")
    @Timed
    public ResponseEntity<Void> deletePathAction(@PathVariable Long id) {
        log.debug("REST request to delete PathAction : {}", id);
        pathActionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
