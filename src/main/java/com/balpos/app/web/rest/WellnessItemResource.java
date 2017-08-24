package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.WellnessItem;
import com.balpos.app.service.WellnessItemService;
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
 * REST controller for managing WellnessItem.
 */
@RestController
@RequestMapping("/api")
public class WellnessItemResource {

    private final Logger log = LoggerFactory.getLogger(WellnessItemResource.class);

    private static final String ENTITY_NAME = "wellnessItem";

    private final WellnessItemService wellnessItemService;

    public WellnessItemResource(WellnessItemService wellnessItemService) {
        this.wellnessItemService = wellnessItemService;
    }

    /**
     * POST  /wellness-items : Create a new wellnessItem.
     *
     * @param wellnessItem the wellnessItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wellnessItem, or with status 400 (Bad Request) if the wellnessItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wellness-items")
    @Timed
    public ResponseEntity<WellnessItem> createWellnessItem(@RequestBody WellnessItem wellnessItem) throws URISyntaxException {
        log.debug("REST request to save WellnessItem : {}", wellnessItem);
        if (wellnessItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wellnessItem cannot already have an ID")).body(null);
        }
        WellnessItem result = wellnessItemService.save(wellnessItem);
        return ResponseEntity.created(new URI("/api/wellness-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wellness-items : Updates an existing wellnessItem.
     *
     * @param wellnessItem the wellnessItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wellnessItem,
     * or with status 400 (Bad Request) if the wellnessItem is not valid,
     * or with status 500 (Internal Server Error) if the wellnessItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wellness-items")
    @Timed
    public ResponseEntity<WellnessItem> updateWellnessItem(@RequestBody WellnessItem wellnessItem) throws URISyntaxException {
        log.debug("REST request to update WellnessItem : {}", wellnessItem);
        if (wellnessItem.getId() == null) {
            return createWellnessItem(wellnessItem);
        }
        WellnessItem result = wellnessItemService.save(wellnessItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wellnessItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wellness-items : get all the wellnessItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wellnessItems in body
     */
    @GetMapping("/wellness-items")
    @Timed
    public ResponseEntity<List<WellnessItem>> getAllWellnessItems(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WellnessItems");
        Page<WellnessItem> page = wellnessItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wellness-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wellness-items/:id : get the "id" wellnessItem.
     *
     * @param id the id of the wellnessItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wellnessItem, or with status 404 (Not Found)
     */
    @GetMapping("/wellness-items/{id}")
    @Timed
    public ResponseEntity<WellnessItem> getWellnessItem(@PathVariable Long id) {
        log.debug("REST request to get WellnessItem : {}", id);
        WellnessItem wellnessItem = wellnessItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wellnessItem));
    }

    /**
     * DELETE  /wellness-items/:id : delete the "id" wellnessItem.
     *
     * @param id the id of the wellnessItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wellness-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteWellnessItem(@PathVariable Long id) {
        log.debug("REST request to delete WellnessItem : {}", id);
        wellnessItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
