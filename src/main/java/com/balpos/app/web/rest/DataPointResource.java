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
     * POST  /data-points : Create a new dataPoint.
     *
     * @param dataPointDTO the dataPointDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataPointDTO, or with status 400 (Bad Request) if the dataPoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-points")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DataPointDTO> createDataPoint(@RequestBody DataPointDTO dataPointDTO) throws URISyntaxException {
        log.debug("REST request to save DataPoint : {}", dataPointDTO);
        if (dataPointDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataPoint cannot already have an ID")).body(null);
        }
        DataPointDTO result = dataPointService.save(dataPointDTO);
        return ResponseEntity.created(new URI("/api/data-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-points : Updates an existing dataPoint.
     *
     * @param dataPointDTO the dataPointDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataPointDTO,
     * or with status 400 (Bad Request) if the dataPointDTO is not valid,
     * or with status 500 (Internal Server Error) if the dataPointDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-points")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DataPointDTO> updateDataPoint(@RequestBody DataPointDTO dataPointDTO) throws URISyntaxException {
        log.debug("REST request to update DataPoint : {}", dataPointDTO);
        if (dataPointDTO.getId() == null) {
            return createDataPoint(dataPointDTO);
        }
        DataPointDTO result = dataPointService.save(dataPointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataPointDTO.getId().toString()))
            .body(result);
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

    /**
     * DELETE  /data-points/:id : delete the "id" dataPoint.
     *
     * @param id the id of the dataPointDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-points/{id}")
    @Timed
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteDataPoint(@PathVariable Long id) {
        log.debug("REST request to delete DataPoint : {}", id);
        dataPointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
