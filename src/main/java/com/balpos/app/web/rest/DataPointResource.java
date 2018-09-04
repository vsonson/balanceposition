package com.balpos.app.web.rest;

import com.balpos.app.domain.Color;
import com.balpos.app.service.UserDataPointService;
import com.balpos.app.service.dto.UserDataPointDTO;
import com.balpos.app.service.mapper.UserDataPointMapper;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.stat.StatConstant;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.vm.DataPointVM;
import com.balpos.app.web.rest.vm.PostDataPointVM;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDateTime;
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
    public ResponseEntity<List<DataPointVM>> getAllDataPoints(Principal principal, @RequestParam Boolean ignoreStale) {
        log.debug("REST request to get DataPoints");
        List<UserDataPointDTO> userDataPoints = userDataPointService.findByUser(userResourceUtil.getUserFromLogin(principal.getName()).get());
        // set stale data point colors to GRAY
        if (!ignoreStale) {
            for (UserDataPointDTO udp : userDataPoints) {
                if (udp.getLastupdate() == null
                    || udp.getLastupdate().plusDays(StatConstant.NL_THRESH).isBefore(LocalDateTime.now())) {
                    udp.setColor(Color.GRAY);
                }
            }
        }
        List<DataPointVM> result = userDataPointMapper.toDpVM(userDataPoints);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * POST  /data-points
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new metricDatumDTO, or with status 400 (Bad Request) if the metricDatum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-points")
    @Timed
    public ResponseEntity<? extends DataPointVM> createUserDataPoint(@Valid @RequestBody PostDataPointVM postDataPointVM, Principal principal) throws URISyntaxException {
        log.debug("REST request to save UserDataPoints : {}", postDataPointVM);
        UserDataPointDTO userDataPointDTO = userDataPointMapper.toDto(postDataPointVM);
        UserDataPointDTO result = userDataPointService.save(userDataPointDTO, userResourceUtil.getUserFromLogin(principal.getName()).get());
        DataPointVM mappedResult = userDataPointMapper.toDpVM(result);
        return ResponseEntity.created(new URI("/data-points/"))
            .headers(HeaderUtil.createEntityCreationAlert("DataPoint", mappedResult.getName()))
            .body(mappedResult);
    }

}
