package com.balpos.app.web.rest;

import com.balpos.app.service.UserDataPointService;
import com.balpos.app.service.dto.UserDataPointDTO;
import com.balpos.app.service.mapper.UserDataPointMapper;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.vm.UserDataPointVM;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DataPoint.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class DataPointResource {

    private final UserDataPointService userDataPointService;
    private final UserResourceUtil userResourceUtil;
    private final UserDataPointMapper userDataPointMapper;

    public DataPointResource(UserDataPointService userDataPointService, UserResourceUtil userResourceUtil, UserDataPointMapper userDataPointMapper) {
        this.userDataPointService = userDataPointService;
        this.userResourceUtil = userResourceUtil;
        this.userDataPointMapper = userDataPointMapper;
    }

    /**
     * GET  /data-points : get all the dataPoints.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataPoints in body
     */
    @GetMapping("/data-points")
    @Timed
    public ResponseEntity<List<UserDataPointDTO>> getAllDataPoints(Principal principal) {
        log.debug("REST request to get DataPoints");
        List<UserDataPointDTO> userDataPoints = userDataPointService.findByUser(userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userDataPoints));
    }

    /**
     * POST  /data-points
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new metricDatumDTO, or with status 400 (Bad Request) if the metricDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-points")
    @Timed
    public ResponseEntity<? extends UserDataPointDTO> createUserDataPoint(@Valid @RequestBody UserDataPointVM userDataPointVM, Principal principal) throws URISyntaxException {
        log.debug("REST request to save UserDataPoints : {}", userDataPointVM);
        UserDataPointDTO userDataPointDTO = userDataPointMapper.toDto(userDataPointVM);
        UserDataPointDTO result = userDataPointService.save(userDataPointDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());
        return ResponseEntity.created(new URI("/data-points/"))
            .headers(HeaderUtil.createEntityCreationAlert("UserDataPoint", result.getDataPoint().getName()))
            .body(result);
    }

}
