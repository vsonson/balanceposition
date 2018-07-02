package com.balpos.app.web.rest;

import com.balpos.app.service.FocusDatumQueryService;
import com.balpos.app.service.FocusDatumService;
import com.balpos.app.service.dto.FocusDatumCriteria;
import com.balpos.app.service.dto.FocusDatumDTO;
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
 * REST controller for managing FocusDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class FocusDatumResource {

    private static final String CREATE_SUCCESS_MESSAGE = "Focus data saved successfully";

    private final FocusDatumService focusDatumService;
    private final FocusDatumQueryService focusDatumQueryService;
    private final UserResourceUtil userResourceUtil;

    public FocusDatumResource(FocusDatumService focusDatumService, FocusDatumQueryService focusDatumQueryService, UserResourceUtil userResourceUtil) {
        this.focusDatumService = focusDatumService;
        this.focusDatumQueryService = focusDatumQueryService;
        this.userResourceUtil = userResourceUtil;
    }

    /**
     * POST  /focus-data : Create a new focusDatum.
     *
     * @param focusDatumDTO the focusDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new focusDatumDTO, or with status 400 (Bad Request) if the focusDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/focus-data")
    @Timed
    public ResponseEntity<FocusDatumDTO> createFocusDatum(@Valid @RequestBody FocusDatumDTO focusDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save FocusDatum : {}", focusDatumDTO);

        FocusDatumDTO result = focusDatumService.save(focusDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());

        return ResponseEntity.created(new URI("/api/focus-data/"))
            .headers(HeaderUtil.createAlert(CREATE_SUCCESS_MESSAGE, null))
            .body(result);
    }

    /**
     * GET  /focus-data : get all the focusData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of focusData in body
     */
    @GetMapping("/focus-data")
    @Timed
    public ResponseEntity<List<FocusDatumDTO>> getAllFocusData(FocusDatumCriteria criteria, @ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get FocusData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<FocusDatumDTO> page = focusDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/focus-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
