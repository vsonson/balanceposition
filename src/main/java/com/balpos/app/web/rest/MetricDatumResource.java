package com.balpos.app.web.rest;

import com.balpos.app.service.MetricDatumQueryService;
import com.balpos.app.service.MetricDatumService;
import com.balpos.app.service.dto.BodyDatumDTO;
import com.balpos.app.service.dto.MetricDatumCriteria;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.dto.SleepDatumDTO;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.mapper.FrontendViewModelMapper;
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
    private final FrontendViewModelMapper frontendViewModelMapper;

    public MetricDatumResource(MetricDatumService metricDatumService, MetricDatumQueryService metricDatumQueryService, UserResourceUtil userResourceUtil, FrontendViewModelMapper frontendViewModelMapper) {
        this.metricDatumService = metricDatumService;
        this.metricDatumQueryService = metricDatumQueryService;
        this.userResourceUtil = userResourceUtil;
        this.frontendViewModelMapper = frontendViewModelMapper;
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
    public ResponseEntity<? extends MetricDatumDTO> createMetricDatum(@Valid @RequestBody MetricDatumDTO metricDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save MetricDatum : {}", metricDatumDTO);

        //TODO filter out sleep and body datapoint types with friendly message to use other API

        MetricDatumDTO result = metricDatumService.save(metricDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.created(new URI("/api/metric-data/"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getDatumValue()))
            .body(result);
    }

    /**
     * POST  /sleep-data : Create a new metricDatum for the logged in user
     *
     * @param sleepDatumDTO the metricDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metricDatumDTO, or with status 400 (Bad Request) if the metricDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sleep-data")
    @Timed
    public ResponseEntity<SleepDatumDTO> createSleepDatum(@Valid @RequestBody SleepDatumDTO sleepDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save MetricDatum : {}", sleepDatumDTO);

        SleepDatumDTO result = metricDatumService.save(sleepDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.created(new URI("/api/sleep-data/"))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getDatumValue()))
            .body(result);
    }

    /**
     * POST  /body-data : Create a new metricDatum for the logged in user
     *
     * @param bodyDatumDTO the metricDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metricDatumDTO, or with status 400 (Bad Request) if the metricDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/body-data")
    @Timed
    public ResponseEntity<BodyDatumDTO> createBodyDatum(@Valid @RequestBody BodyDatumDTO bodyDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save MetricDatum : {}", bodyDatumDTO);

        BodyDatumDTO result = metricDatumService.save(bodyDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.created(new URI("/api/body-data/"))
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
    public ResponseEntity<List<MetricDatumDTO>> getAllMetricData(MetricDatumCriteria criteria,
                                                                 @ApiParam Pageable pageable,
                                                                 @ApiParam @RequestParam(required = false) boolean frontendViewModel,
                                                                 Principal principal) {

        log.debug("REST request to get MetricData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<MetricDatumDTO> page = metricDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metric-data");

        List<MetricDatumDTO> content = (frontendViewModel) ? frontendViewModelMapper.toVM(page.getContent()) : page.getContent();
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

}
