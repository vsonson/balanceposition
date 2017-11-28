package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.service.QuoteOfTheDayService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuoteOfTheDay.
 */
@RestController
@RequestMapping("/api")
public class QuoteOfTheDayResource {

    private final Logger log = LoggerFactory.getLogger(QuoteOfTheDayResource.class);

    private static final String ENTITY_NAME = "quoteOfTheDay";

    private final QuoteOfTheDayService quoteOfTheDayService;

    public QuoteOfTheDayResource(QuoteOfTheDayService quoteOfTheDayService) {
        this.quoteOfTheDayService = quoteOfTheDayService;
    }

    /**
     * POST  /quote-of-the-days : Create a new quoteOfTheDay.
     *
     * @param quoteOfTheDay the quoteOfTheDay to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quoteOfTheDay, or with status 400 (Bad Request) if the quoteOfTheDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quote-of-the-days")
    @Timed
    public ResponseEntity<QuoteOfTheDay> createQuoteOfTheDay(@RequestBody QuoteOfTheDay quoteOfTheDay) throws URISyntaxException {
        log.debug("REST request to save QuoteOfTheDay : {}", quoteOfTheDay);
        if (quoteOfTheDay.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new quoteOfTheDay cannot already have an ID")).body(null);
        }
        QuoteOfTheDay result = quoteOfTheDayService.save(quoteOfTheDay);
        return ResponseEntity.created(new URI("/api/quote-of-the-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quote-of-the-days : Updates an existing quoteOfTheDay.
     *
     * @param quoteOfTheDay the quoteOfTheDay to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quoteOfTheDay,
     * or with status 400 (Bad Request) if the quoteOfTheDay is not valid,
     * or with status 500 (Internal Server Error) if the quoteOfTheDay couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quote-of-the-days")
    @Timed
    public ResponseEntity<QuoteOfTheDay> updateQuoteOfTheDay(@RequestBody QuoteOfTheDay quoteOfTheDay) throws URISyntaxException {
        log.debug("REST request to update QuoteOfTheDay : {}", quoteOfTheDay);
        if (quoteOfTheDay.getId() == null) {
            return createQuoteOfTheDay(quoteOfTheDay);
        }
        QuoteOfTheDay result = quoteOfTheDayService.save(quoteOfTheDay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, quoteOfTheDay.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quote-of-the-days : get all the quoteOfTheDays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of quoteOfTheDays in body
     */
    @GetMapping("/quote-of-the-days")
    @Timed
    public ResponseEntity<List<QuoteOfTheDay>> getAllQuoteOfTheDays(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of QuoteOfTheDays");
        Page<QuoteOfTheDay> page = quoteOfTheDayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quote-of-the-days");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quote-of-the-days/:id : get the "id" quoteOfTheDay.
     *
     * @param id the id of the quoteOfTheDay to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quoteOfTheDay, or with status 404 (Not Found)
     */
    @GetMapping("/quote-of-the-days/{id}")
    @Timed
    public ResponseEntity<QuoteOfTheDay> getQuoteOfTheDay(@PathVariable Long id) {
        log.debug("REST request to get QuoteOfTheDay : {}", id);
        QuoteOfTheDay quoteOfTheDay = quoteOfTheDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quoteOfTheDay));
    }

    /**
     * GET  /quote-of-the-days/current : get the current quoteOfTheDay.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the quoteOfTheDay, or with status 404 (Not Found)
     */
    @GetMapping("/quote-of-the-days/current")
    @Timed
    public ResponseEntity<QuoteOfTheDay> getCurrentQuoteOfTheDay() {
        log.debug("REST request to get QuoteOfTheDay : {}");
        QuoteOfTheDay quoteOfTheDay = quoteOfTheDayService.getCurrent();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quoteOfTheDay));
    }

    @GetMapping("/quote-of-the-days/new")
    @Timed
    public Boolean newQuote() {
        log.debug("REST request to get QuoteOfTheDay : {}");
        return quoteOfTheDayService.newQuote();
    }

    /**
     * DELETE  /quote-of-the-days/:id : delete the "id" quoteOfTheDay.
     *
     * @param id the id of the quoteOfTheDay to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quote-of-the-days/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuoteOfTheDay(@PathVariable Long id) {
        log.debug("REST request to delete QuoteOfTheDay : {}", id);
        quoteOfTheDayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
