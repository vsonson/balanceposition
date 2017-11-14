package com.balpos.app.service;

import com.balpos.app.domain.QuoteOfTheDayHistory;
import com.balpos.app.repository.QuoteOfTheDayHistoryRepository;
import com.balpos.app.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuoteOfTheDayHistory.
 */
@Service
@Transactional
public class QuoteOfTheDayHistoryService {

    private final Logger log = LoggerFactory.getLogger(QuoteOfTheDayHistoryService.class);

    private final QuoteOfTheDayHistoryRepository quoteOfTheDayHistoryRepository;
    public QuoteOfTheDayHistoryService(QuoteOfTheDayHistoryRepository quoteOfTheDayHistoryRepository) {
        this.quoteOfTheDayHistoryRepository = quoteOfTheDayHistoryRepository;
    }

    /**
     * Save a quoteOfTheDayHistory.
     *
     * @param quoteOfTheDayHistory the entity to save
     * @return the persisted entity
     */
    public QuoteOfTheDayHistory save(QuoteOfTheDayHistory quoteOfTheDayHistory) {
        log.debug("Request to save QuoteOfTheDayHistory : {}", quoteOfTheDayHistory);
        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
            return quoteOfTheDayHistoryRepository.save(quoteOfTheDayHistory);
        }else{
            return quoteOfTheDayHistoryRepository.findOne(1L);
        }
    }

    /**
     *  Get all the quoteOfTheDayHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuoteOfTheDayHistory> findAll(Pageable pageable) {
        log.debug("Request to get all QuoteOfTheDayHistories");
        return quoteOfTheDayHistoryRepository.findAll(pageable);
    }

    /**
     *  Get one quoteOfTheDayHistory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public QuoteOfTheDayHistory findOne(Long id) {
        log.debug("Request to get QuoteOfTheDayHistory : {}", id);
        return quoteOfTheDayHistoryRepository.findOne(id);
    }

    /**
     *  Delete the  quoteOfTheDayHistory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QuoteOfTheDayHistory : {}", id);
        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
            quoteOfTheDayHistoryRepository.delete(id);
        }
    }
}
