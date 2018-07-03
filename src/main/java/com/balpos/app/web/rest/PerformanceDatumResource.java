package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.service.PerformanceDatumService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import com.balpos.app.service.dto.PerformanceDatumDTO;
import com.balpos.app.service.dto.PerformanceDatumCriteria;
import com.balpos.app.service.PerformanceDatumQueryService;
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
 * REST controller for managing PerformanceDatum.
 */
@RestController
@RequestMapping("/api")
public class PerformanceDatumResource {

    private final Logger log = LoggerFactory.getLogger(PerformanceDatumResource.class);

    private static final String ENTITY_NAME = "performanceDatum";

    private final PerformanceDatumService performanceDatumService;
    private final PerformanceDatumQueryService performanceDatumQueryService;

    public PerformanceDatumResource(PerformanceDatumService performanceDatumService, PerformanceDatumQueryService performanceDatumQueryService) {
        this.performanceDatumService = performanceDatumService;
        this.performanceDatumQueryService = performanceDatumQueryService;
    }

    /**
     * POST  /performance-data : Create a new performanceDatum.
     *
     * @param performanceDatumDTO the performanceDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new performanceDatumDTO, or with status 400 (Bad Request) if the performanceDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/performance-data")
    @Timed
    public ResponseEntity<PerformanceDatumDTO> createPerformanceDatum(@Valid @RequestBody PerformanceDatumDTO performanceDatumDTO) throws URISyntaxException {
        log.debug("REST request to save PerformanceDatum : {}", performanceDatumDTO);
        if (performanceDatumDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new performanceDatum cannot already have an ID")).body(null);
        }
        PerformanceDatumDTO result = performanceDatumService.save(performanceDatumDTO);
        return ResponseEntity.created(new URI("/api/performance-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /performance-data : Updates an existing performanceDatum.
     *
     * @param performanceDatumDTO the performanceDatumDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated performanceDatumDTO,
     * or with status 400 (Bad Request) if the performanceDatumDTO is not valid,
     * or with status 500 (Internal Server Error) if the performanceDatumDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/performance-data")
    @Timed
    public ResponseEntity<PerformanceDatumDTO> updatePerformanceDatum(@Valid @RequestBody PerformanceDatumDTO performanceDatumDTO) throws URISyntaxException {
        log.debug("REST request to update PerformanceDatum : {}", performanceDatumDTO);
        if (performanceDatumDTO.getId() == null) {
            return createPerformanceDatum(performanceDatumDTO);
        }
        PerformanceDatumDTO result = performanceDatumService.save(performanceDatumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, performanceDatumDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /performance-data : get all the performanceData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of performanceData in body
     */
    @GetMapping("/performance-data")
    @Timed
    public ResponseEntity<List<PerformanceDatumDTO>> getAllPerformanceData(PerformanceDatumCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get PerformanceData by criteria: {}", criteria);
        Page<PerformanceDatumDTO> page = performanceDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/performance-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /performance-data/:id : get the "id" performanceDatum.
     *
     * @param id the id of the performanceDatumDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the performanceDatumDTO, or with status 404 (Not Found)
     */
    @GetMapping("/performance-data/{id}")
    @Timed
    public ResponseEntity<PerformanceDatumDTO> getPerformanceDatum(@PathVariable Long id) {
        log.debug("REST request to get PerformanceDatum : {}", id);
        PerformanceDatumDTO performanceDatumDTO = performanceDatumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(performanceDatumDTO));
    }

    /**
     * DELETE  /performance-data/:id : delete the "id" performanceDatum.
     *
     * @param id the id of the performanceDatumDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/performance-data/{id}")
    @Timed
    public ResponseEntity<Void> deletePerformanceDatum(@PathVariable Long id) {
        log.debug("REST request to delete PerformanceDatum : {}", id);
        performanceDatumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
