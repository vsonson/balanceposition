package com.balpos.app.web.rest;

import com.balpos.app.service.PerformanceDatumQueryService;
import com.balpos.app.service.PerformanceDatumService;
import com.balpos.app.service.dto.PerformanceDatumCriteria;
import com.balpos.app.service.dto.PerformanceDatumDTO;
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
 * REST controller for managing PerformanceDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class PerformanceDatumResource {

    private static final String CREATE_SUCCESS_MESSAGE = "Performance data saved successfully";

    private final PerformanceDatumService performanceDatumService;
    private final PerformanceDatumQueryService performanceDatumQueryService;
    private final UserResourceUtil userResourceUtil;

    public PerformanceDatumResource(PerformanceDatumService performanceDatumService, PerformanceDatumQueryService performanceDatumQueryService, UserResourceUtil userResourceUtil) {
        this.performanceDatumService = performanceDatumService;
        this.performanceDatumQueryService = performanceDatumQueryService;
        this.userResourceUtil = userResourceUtil;
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
    public ResponseEntity<PerformanceDatumDTO> createPerformanceDatum(@Valid @RequestBody PerformanceDatumDTO performanceDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save PerformanceDatum : {}", performanceDatumDTO);

        PerformanceDatumDTO result = performanceDatumService.save(performanceDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());

        return ResponseEntity.created(new URI("/api/performance-data/"))
            .headers(HeaderUtil.createAlert(CREATE_SUCCESS_MESSAGE, null))
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
    public ResponseEntity<List<PerformanceDatumDTO>> getAllPerformanceData(PerformanceDatumCriteria criteria, @ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get PerformanceData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<PerformanceDatumDTO> page = performanceDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/performance-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
