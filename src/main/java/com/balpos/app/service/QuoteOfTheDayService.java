package com.balpos.app.service;

import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.repository.QuoteOfTheDayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuoteOfTheDay.
 */
@Service
@Transactional
public class QuoteOfTheDayService {

    private final Logger log = LoggerFactory.getLogger(QuoteOfTheDayService.class);

    private final QuoteOfTheDayRepository quoteOfTheDayRepository;
    public QuoteOfTheDayService(QuoteOfTheDayRepository quoteOfTheDayRepository) {
        this.quoteOfTheDayRepository = quoteOfTheDayRepository;
    }

    /**
     * Save a quoteOfTheDay.
     *
     * @param quoteOfTheDay the entity to save
     * @return the persisted entity
     */
    public QuoteOfTheDay save(QuoteOfTheDay quoteOfTheDay) {
        log.debug("Request to save QuoteOfTheDay : {}", quoteOfTheDay);
        return quoteOfTheDayRepository.save(quoteOfTheDay);
    }

    /**
     *  Get all the quoteOfTheDays.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuoteOfTheDay> findAll(Pageable pageable) {
        log.debug("Request to get all QuoteOfTheDays");
        return quoteOfTheDayRepository.findAll(pageable);
    }

    /**
     *  Get one quoteOfTheDay by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public QuoteOfTheDay findOne(Long id) {
        log.debug("Request to get QuoteOfTheDay : {}", id);
        return quoteOfTheDayRepository.findOne(id);
    }

    /**
     *  Delete the  quoteOfTheDay by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QuoteOfTheDay : {}", id);
        quoteOfTheDayRepository.delete(id);
    }

    public QuoteOfTheDay getCurrent() {
        return null;
    }
}
