package com.balpos.app.web.rest;

import com.balpos.app.service.BodyDatumQueryService;
import com.balpos.app.service.BodyDatumService;
import com.balpos.app.service.dto.BodyDatumCriteria;
import com.balpos.app.service.dto.BodyDatumDTO;
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
 * REST controller for managing BodyDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class BodyDatumResource {

    private static final String CREATE_SUCCESS_MESSAGE = "Body data saved successfully";

    private final BodyDatumService bodyDatumService;
    private final BodyDatumQueryService bodyDatumQueryService;
    private final UserResourceUtil userResourceUtil;

    public BodyDatumResource(BodyDatumService bodyDatumService, BodyDatumQueryService bodyDatumQueryService, UserResourceUtil userResourceUtil) {
        this.bodyDatumService = bodyDatumService;
        this.bodyDatumQueryService = bodyDatumQueryService;
        this.userResourceUtil = userResourceUtil;
    }

    /**
     * POST  /body-data : Create a new bodyDatum.
     *
     * @param bodyDatumDTO the bodyDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bodyDatumDTO, or with status 400 (Bad Request) if the bodyDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/body-data")
    @Timed
    public ResponseEntity<BodyDatumDTO> createBodyDatum(@Valid @RequestBody BodyDatumDTO bodyDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save BodyDatum : {}", bodyDatumDTO);

        BodyDatumDTO result = bodyDatumService.save(bodyDatumDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());

        return ResponseEntity.created(new URI("/api/body-data/"))
            .headers(HeaderUtil.createAlert(CREATE_SUCCESS_MESSAGE, null))
            .body(result);
    }

    /**
     * GET  /body-data : get all the bodyData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of bodyData in body
     */
    @GetMapping("/body-data")
    @Timed
    public ResponseEntity<List<BodyDatumDTO>> getAllBodyData(BodyDatumCriteria criteria, @ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get BodyData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<BodyDatumDTO> page = bodyDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/body-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
