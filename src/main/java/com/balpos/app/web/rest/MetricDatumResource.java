package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.service.MetricDatumService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.dto.MetricDatumCriteria;
import com.balpos.app.service.MetricDatumQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
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
 * REST controller for managing MetricDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class MetricDatumResource {

    private static final String ENTITY_NAME = "metricDatum";

    private final MetricDatumService metricDatumService;
    private final MetricDatumQueryService metricDatumQueryService;

    public MetricDatumResource(MetricDatumService metricDatumService, MetricDatumQueryService metricDatumQueryService) {
        this.metricDatumService = metricDatumService;
        this.metricDatumQueryService = metricDatumQueryService;
    }

    /**
     * POST  /metric-data : Create a new metricDatum.
     *
     * @param metricDatumDTO the metricDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metricDatumDTO, or with status 400 (Bad Request) if the metricDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metric-data")
    @Timed
    public ResponseEntity<MetricDatumDTO> createMetricDatum(@Valid @RequestBody MetricDatumDTO metricDatumDTO) throws URISyntaxException {
        log.debug("REST request to save MetricDatum : {}", metricDatumDTO);
        if (metricDatumDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new metricDatum cannot already have an ID")).body(null);
        }
        MetricDatumDTO result = metricDatumService.save(metricDatumDTO);
        return ResponseEntity.created(new URI("/api/metric-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metric-data : Updates an existing metricDatum.
     *
     * @param metricDatumDTO the metricDatumDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metricDatumDTO,
     * or with status 400 (Bad Request) if the metricDatumDTO is not valid,
     * or with status 500 (Internal Server Error) if the metricDatumDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metric-data")
    @Timed
    public ResponseEntity<MetricDatumDTO> updateMetricDatum(@Valid @RequestBody MetricDatumDTO metricDatumDTO) throws URISyntaxException {
        log.debug("REST request to update MetricDatum : {}", metricDatumDTO);
        if (metricDatumDTO.getId() == null) {
            return createMetricDatum(metricDatumDTO);
        }
        MetricDatumDTO result = metricDatumService.save(metricDatumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metricDatumDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metric-data : get all the metricData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of metricData in body
     */
    @GetMapping("/metric-data")
    @Timed
    public ResponseEntity<List<MetricDatumDTO>> getAllMetricData(MetricDatumCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get MetricData by criteria: {}", criteria);
        Page<MetricDatumDTO> page = metricDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metric-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /metric-data/:id : get the "id" metricDatum.
     *
     * @param id the id of the metricDatumDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metricDatumDTO, or with status 404 (Not Found)
     */
    @GetMapping("/metric-data/{id}")
    @Timed
    public ResponseEntity<MetricDatumDTO> getMetricDatum(@PathVariable Long id) {
        log.debug("REST request to get MetricDatum : {}", id);
        MetricDatumDTO metricDatumDTO = metricDatumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metricDatumDTO));
    }

    /**
     * DELETE  /metric-data/:id : delete the "id" metricDatum.
     *
     * @param id the id of the metricDatumDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metric-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetricDatum(@PathVariable Long id) {
        log.debug("REST request to delete MetricDatum : {}", id);
        metricDatumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
