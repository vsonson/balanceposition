package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.Resources;
import com.balpos.app.service.ResourcesService;
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
 * REST controller for managing Resources.
 */
@RestController
@RequestMapping("/api")
public class ResourcesResource {

    private final Logger log = LoggerFactory.getLogger(ResourcesResource.class);

    private static final String ENTITY_NAME = "resources";

    private final ResourcesService resourcesService;

    public ResourcesResource(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    /**
     * POST  /resources : Create a new resources.
     *
     * @param resources the resources to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resources, or with status 400 (Bad Request) if the resources has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resources")
    @Timed
    public ResponseEntity<Resources> createResources(@Valid @RequestBody Resources resources) throws URISyntaxException {
        log.debug("REST request to save Resources : {}", resources);
        if (resources.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new resources cannot already have an ID")).body(null);
        }
        Resources result = resourcesService.save(resources);
        return ResponseEntity.created(new URI("/api/resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resources : Updates an existing resources.
     *
     * @param resources the resources to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resources,
     * or with status 400 (Bad Request) if the resources is not valid,
     * or with status 500 (Internal Server Error) if the resources couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resources")
    @Timed
    public ResponseEntity<Resources> updateResources(@Valid @RequestBody Resources resources) throws URISyntaxException {
        log.debug("REST request to update Resources : {}", resources);
        if (resources.getId() == null) {
            return createResources(resources);
        }
        Resources result = resourcesService.save(resources);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resources.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resources : get all the resources.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resources in body
     */
    @GetMapping("/resources")
    @Timed
    public ResponseEntity<List<Resources>> getAllResources(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Resources");
        Page<Resources> page = resourcesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resources/:id : get the "id" resources.
     *
     * @param id the id of the resources to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resources, or with status 404 (Not Found)
     */
    @GetMapping("/resources/{id}")
    @Timed
    public ResponseEntity<Resources> getResources(@PathVariable Long id) {
        log.debug("REST request to get Resources : {}", id);
        Resources resources = resourcesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resources));
    }

    /**
     * DELETE  /resources/:id : delete the "id" resources.
     *
     * @param id the id of the resources to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resources/{id}")
    @Timed
    public ResponseEntity<Void> deleteResources(@PathVariable Long id) {
        log.debug("REST request to delete Resources : {}", id);
        resourcesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
