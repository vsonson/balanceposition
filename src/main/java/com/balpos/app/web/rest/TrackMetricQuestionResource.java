package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.TrackMetricQuestion;
import com.balpos.app.service.TrackMetricQuestionService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TrackMetricQuestion.
 */
@RestController
@RequestMapping("/api")
public class TrackMetricQuestionResource {

    private final Logger log = LoggerFactory.getLogger(TrackMetricQuestionResource.class);

    private static final String ENTITY_NAME = "trackMetricQuestion";

    private final TrackMetricQuestionService trackMetricQuestionService;

    public TrackMetricQuestionResource(TrackMetricQuestionService trackMetricQuestionService) {
        this.trackMetricQuestionService = trackMetricQuestionService;
    }

    /**
     * POST  /track-metric-questions : Create a new trackMetricQuestion.
     *
     * @param trackMetricQuestion the trackMetricQuestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trackMetricQuestion, or with status 400 (Bad Request) if the trackMetricQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/track-metric-questions")
    @Timed
    public ResponseEntity<TrackMetricQuestion> createTrackMetricQuestion(@Valid @RequestBody TrackMetricQuestion trackMetricQuestion) throws URISyntaxException {
        log.debug("REST request to save TrackMetricQuestion : {}", trackMetricQuestion);
        if (trackMetricQuestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trackMetricQuestion cannot already have an ID")).body(null);
        }
        TrackMetricQuestion result = trackMetricQuestionService.save(trackMetricQuestion);
        return ResponseEntity.created(new URI("/api/track-metric-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /track-metric-questions : Updates an existing trackMetricQuestion.
     *
     * @param trackMetricQuestion the trackMetricQuestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trackMetricQuestion,
     * or with status 400 (Bad Request) if the trackMetricQuestion is not valid,
     * or with status 500 (Internal Server Error) if the trackMetricQuestion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/track-metric-questions")
    @Timed
    public ResponseEntity<TrackMetricQuestion> updateTrackMetricQuestion(@Valid @RequestBody TrackMetricQuestion trackMetricQuestion) throws URISyntaxException {
        log.debug("REST request to update TrackMetricQuestion : {}", trackMetricQuestion);
        if (trackMetricQuestion.getId() == null) {
            return createTrackMetricQuestion(trackMetricQuestion);
        }
        TrackMetricQuestion result = trackMetricQuestionService.save(trackMetricQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trackMetricQuestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /track-metric-questions : get all the trackMetricQuestions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trackMetricQuestions in body
     */
    @GetMapping("/track-metric-questions")
    @Timed
    public ResponseEntity<List<TrackMetricQuestion>> getAllTrackMetricQuestions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TrackMetricQuestions");
        Page<TrackMetricQuestion> page = trackMetricQuestionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/track-metric-questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /track-metric-questions/:id : get the "id" trackMetricQuestion.
     *
     * @param id the id of the trackMetricQuestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trackMetricQuestion, or with status 404 (Not Found)
     */
    @GetMapping("/track-metric-questions/{id}")
    @Timed
    public ResponseEntity<TrackMetricQuestion> getTrackMetricQuestion(@PathVariable Long id) {
        log.debug("REST request to get TrackMetricQuestion : {}", id);
        TrackMetricQuestion trackMetricQuestion = trackMetricQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trackMetricQuestion));
    }

    /**
     * DELETE  /track-metric-questions/:id : delete the "id" trackMetricQuestion.
     *
     * @param id the id of the trackMetricQuestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/track-metric-questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrackMetricQuestion(@PathVariable Long id) {
        log.debug("REST request to delete TrackMetricQuestion : {}", id);
        trackMetricQuestionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
