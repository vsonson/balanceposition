package com.balpos.app.web.rest;

import com.balpos.app.service.MetricDatumQueryService;
import com.balpos.app.service.MetricDatumService;
import com.balpos.app.service.dto.MetricDatumCriteria;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

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
    private final UserResourceUtil userResourceUtil;

    public MetricDatumResource(MetricDatumService metricDatumService, MetricDatumQueryService metricDatumQueryService, UserResourceUtil userResourceUtil) {
        this.metricDatumService = metricDatumService;
        this.metricDatumQueryService = metricDatumQueryService;
        this.userResourceUtil = userResourceUtil;
    }

    /**
     * POST  /metric-data : Create a new metricDatum for the logged in user
     *
     * @param metricDatumDTO the metricDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metricDatumDTO, or with status 400 (Bad Request) if the metricDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metric-data")
    @Timed
    public ResponseEntity<MetricDatumDTO> createMetricDatum(@Valid @RequestBody MetricDatumDTO metricDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save MetricDatum : {}", metricDatumDTO);

        MetricDatumDTO result = metricDatumService.save(metricDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.created(new URI("/api/metric-data/" ))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getDatumValue()))
            .body(result);
    }

    /**
     * GET  /metric-data : get all the metricData for the logged in user
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of metricData in body
     */
    @GetMapping("/metric-data")
    @Timed
    public ResponseEntity<List<MetricDatumDTO>> getAllMetricData(MetricDatumCriteria criteria, @ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get MetricData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<MetricDatumDTO> page = metricDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metric-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
