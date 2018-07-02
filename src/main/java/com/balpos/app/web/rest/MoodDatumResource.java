package com.balpos.app.web.rest;

import com.balpos.app.service.util.UserResourceUtil;
import com.codahale.metrics.annotation.Timed;
import com.balpos.app.service.MoodDatumService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import com.balpos.app.service.dto.MoodDatumDTO;
import com.balpos.app.service.dto.MoodDatumCriteria;
import com.balpos.app.service.MoodDatumQueryService;
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
 * REST controller for managing MoodDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class MoodDatumResource {

    private static final String ENTITY_NAME = "moodDatum";

    private final MoodDatumService moodDatumService;
    private final MoodDatumQueryService moodDatumQueryService;
    private final UserResourceUtil userResourceUtil;

    public MoodDatumResource(MoodDatumService moodDatumService, MoodDatumQueryService moodDatumQueryService, UserResourceUtil userResourceUtil) {
        this.moodDatumService = moodDatumService;
        this.moodDatumQueryService = moodDatumQueryService;
        this.userResourceUtil = userResourceUtil;
    }

    /**
     * POST  /mood-data : Create a new moodDatum.
     *
     * @param moodDatumDTO the moodDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moodDatumDTO, or with status 400 (Bad Request) if the moodDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mood-data")
    @Timed
    public ResponseEntity<MoodDatumDTO> createMoodDatum(@Valid @RequestBody MoodDatumDTO moodDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save MoodDatum : {}", moodDatumDTO);
        if (moodDatumDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new moodDatum cannot already have an ID")).body(null);
        }
        MoodDatumDTO result = moodDatumService.save(moodDatumDTO,  userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.created(new URI("/api/mood-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mood-data : Updates an existing moodDatum.
     *
     * @param moodDatumDTO the moodDatumDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moodDatumDTO,
     * or with status 400 (Bad Request) if the moodDatumDTO is not valid,
     * or with status 500 (Internal Server Error) if the moodDatumDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mood-data")
    @Timed
    public ResponseEntity<MoodDatumDTO> updateMoodDatum(@Valid @RequestBody MoodDatumDTO moodDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to update MoodDatum : {}", moodDatumDTO);
        if (moodDatumDTO.getId() == null) {
            return createMoodDatum(moodDatumDTO, principal);
        }
        MoodDatumDTO result = moodDatumService.save(moodDatumDTO,  userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, moodDatumDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mood-data : get all the moodData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of moodData in body
     */
    @GetMapping("/mood-data")
    @Timed
    public ResponseEntity<List<MoodDatumDTO>> getAllMoodData(MoodDatumCriteria criteria,@ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get MoodData by criteria: {}", criteria);
        criteria.setUserId(userResourceUtil.createUserFilterFromLogin(principal.getName()));

        Page<MoodDatumDTO> page = moodDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mood-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
