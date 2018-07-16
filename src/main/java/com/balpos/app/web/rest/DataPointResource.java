package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.service.DataPointService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import com.balpos.app.service.dto.DataPointDTO;
import com.balpos.app.service.dto.DataPointCriteria;
import com.balpos.app.service.DataPointQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DataPoint.
 */
@RestController
@RequestMapping("/api")
public class DataPointResource {

    private final Logger log = LoggerFactory.getLogger(DataPointResource.class);

    private static final String ENTITY_NAME = "dataPoint";

    private final DataPointService dataPointService;
    private final DataPointQueryService dataPointQueryService;

    public DataPointResource(DataPointService dataPointService, DataPointQueryService dataPointQueryService) {
        this.dataPointService = dataPointService;
        this.dataPointQueryService = dataPointQueryService;
    }

    /**
     * GET  /data-points : get all the dataPoints.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of dataPoints in body
     */
    @GetMapping("/data-points")
    @Timed
    public ResponseEntity<List<DataPointDTO>> getAllDataPoints(DataPointCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get DataPoints by criteria: {}", criteria);
        Page<DataPointDTO> page = dataPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-points/:id : get the "id" dataPoint.
     *
     * @param id the id of the dataPointDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataPointDTO, or with status 404 (Not Found)
     */
    @GetMapping("/data-points/{id}")
    @Timed
    public ResponseEntity<DataPointDTO> getDataPoint(@PathVariable Long id) {
        log.debug("REST request to get DataPoint : {}", id);
        DataPointDTO dataPointDTO = dataPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataPointDTO));
    }
}
