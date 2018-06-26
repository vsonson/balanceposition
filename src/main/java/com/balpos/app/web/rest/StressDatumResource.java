package com.balpos.app.web.rest;

import com.balpos.app.domain.User;
import com.balpos.app.repository.StressDatumRepository;
import com.balpos.app.service.StressDatumQueryService;
import com.balpos.app.service.StressDatumService;
import com.balpos.app.service.UserService;
import com.balpos.app.service.dto.StressDatumCriteria;
import com.balpos.app.service.dto.StressDatumDTO;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
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
 * REST controller for managing StressDatum.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class StressDatumResource {

    private static final String CREATE_SUCCESS_MESSAGE = "Stress data saved successfully";

    private final StressDatumService stressDatumService;
    private final StressDatumQueryService stressDatumQueryService;
    private final UserService userService;

    public StressDatumResource(StressDatumService stressDatumService, StressDatumQueryService stressDatumQueryService, UserService userService) {
        this.stressDatumService = stressDatumService;
        this.stressDatumQueryService = stressDatumQueryService;
        this.userService = userService;
    }

    /**
     * POST  /stress-data : Create a new stressDatum.
     *
     * @param stressDatumDTO the stressDatumDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stressDatumDTO, or with status 400 (Bad Request) if the stressDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stress-data")
    @Timed
    public ResponseEntity<StressDatumDTO> createStressDatum(@Valid @RequestBody StressDatumDTO stressDatumDTO, Principal principal) throws URISyntaxException {
        log.debug("REST request to save StressDatum : {}", stressDatumDTO);

        StressDatumDTO result = stressDatumService.save(stressDatumDTO, userService.getUserByLogin(principal.getName()).get());

        return ResponseEntity.created(new URI("/api/stress-data/"))
            .headers(HeaderUtil.createAlert(CREATE_SUCCESS_MESSAGE, null))
            .body(result);
    }

    /**
     * GET  /stress-data : get all the stressData.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stressData in body
     */
    @GetMapping("/stress-data")
    @Timed
    public ResponseEntity<List<StressDatumDTO>> getAllStressData(StressDatumCriteria criteria, @ApiParam Pageable pageable, Principal principal) {
        log.debug("REST request to get StressData by criteria: {}", criteria);
        User user = userService.getUserByLogin(principal.getName()).get();
        LongFilter userFilter = new LongFilter();
        userFilter.setEquals(user.getId());
        criteria.setUserId(userFilter);

        Page<StressDatumDTO> page = stressDatumQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stress-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
