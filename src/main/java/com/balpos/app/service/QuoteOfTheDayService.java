package com.balpos.app.service;

import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.domain.User;
import com.balpos.app.domain.UserInfo;
import com.balpos.app.repository.QuoteOfTheDayRepository;
import com.balpos.app.repository.UserInfoRepository;
import com.balpos.app.repository.UserRepository;
import com.balpos.app.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Service Implementation for managing QuoteOfTheDay.
 */
@Service
@Transactional
public class QuoteOfTheDayService {

    private final Logger log = LoggerFactory.getLogger(QuoteOfTheDayService.class);

    private final QuoteOfTheDayRepository quoteOfTheDayRepository;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public QuoteOfTheDayService(
        QuoteOfTheDayRepository quoteOfTheDayRepository,
        UserRepository userRepository,
        UserInfoRepository userInfoRepository
    ) {
        this.quoteOfTheDayRepository = quoteOfTheDayRepository;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
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

        String login = SecurityUtils.getCurrentUserLogin();

        User user = userRepository.findOneByLogin(login).get();

        UserInfo userInfo = userInfoRepository.findOneByUser(user);

        LocalDate lastQuoteDate = userInfo.getLastQuoteDate();
        QuoteOfTheDay quoteOfTheDay;
        // has 24 hours elapsed?
        if( lastQuoteDate != null && lastQuoteDate.plusDays(1).isBefore(LocalDate.now()) ){
            Set<QuoteOfTheDay> quoteOfTheDays = userInfo.getQuoteOfTheDays();
            List<Long> ids = new ArrayList<>();
            quoteOfTheDays.forEach(quoteOfTheDay1 -> ids.add(quoteOfTheDay1.getId()));
            List<QuoteOfTheDay> quoteList = quoteOfTheDayRepository.findByIdNotIn(ids);
            quoteOfTheDay = quoteList.get(0);
            userInfo.setLastQuoteDate(LocalDate.now());
            userInfo.setLastQuoteId(quoteOfTheDay.getId());
            userInfo.addQuoteOfTheDay(quoteOfTheDay);
        }else if( lastQuoteDate != null && lastQuoteDate.plusDays(1).isAfter(LocalDate.now()) ){
            Long lastQuoteId = userInfo.getLastQuoteId();
            quoteOfTheDay = quoteOfTheDayRepository.findOne(lastQuoteId);
        }else{
            quoteOfTheDay = quoteOfTheDayRepository.findAll().get(0);
            userInfo.setLastQuoteDate(LocalDate.now());
            userInfo.setLastQuoteId(quoteOfTheDay.getId());
            userInfo.addQuoteOfTheDay(quoteOfTheDay);
        }

        userInfoRepository.save(userInfo);
        quoteOfTheDay.addUserInfo(userInfo);
        quoteOfTheDayRepository.save(quoteOfTheDay);

        return quoteOfTheDay;
    }
}
