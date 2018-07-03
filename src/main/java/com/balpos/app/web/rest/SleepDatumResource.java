package com.balpos.app.web.rest;

import com.balpos.app.service.SleepDatumQueryService;
import com.balpos.app.service.SleepDatumService;
import com.balpos.app.service.dto.SleepDatumCriteria;
import com.balpos.app.service.dto.SleepDatumDTO;
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
 * REST controller for managing SleepDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class SleepDatumResource {

    private static final String CREATE_SUCCESS_MESSAGE = "Sleep data saved successfully";

    private final SleepDatumService sleepDatumService;
    private final SleepDatumQueryService sleepDatumQueryService;
    private final UserResourceUtil userResourceUtil;

    public SleepDatumResource(SleepDatumService sleepDatumService, SleepDatumQueryService sleepDatumQueryService, UserResourceUtil userResourceUtil) {
        this.sleepDatumService = sleepDatumService;
        this.sleepDatumQueryService = sleepDatumQueryService;
        this.userResourceUtil = userResourceUtil;
    }

    /**
     * POST  /sleep-data : Create a new sleepDatum.
     *
     * @param sleepDatumDTO the sleepDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sleepDatumDTO, or with status 400 (Bad Request) if the sleepDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sleep-data")
    @Timed
    public ResponseEntity<SleepDatumDTO> createSleepDatum(@Valid @RequestBody SleepDatumDTO sleepDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save SleepDatum : {}", sleepDatumDTO);

        SleepDatumDTO result = sleepDatumService.save(sleepDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());

        return ResponseEntity.created(new URI("/api/sleep-data/"))
            .headers(HeaderUtil.createAlert(CREATE_SUCCESS_MESSAGE, null))
            .body(result);
    }

    /**
     * GET  /sleep-data : get all the sleepData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sleepData in body
     */
    @GetMapping("/sleep-data")
    @Timed
    public ResponseEntity<List<SleepDatumDTO>> getAllSleepData(SleepDatumCriteria criteria, @ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get SleepData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<SleepDatumDTO> page = sleepDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sleep-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
