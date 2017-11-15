package com.balpos.app.service;

import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.domain.User;
import com.balpos.app.domain.UserInfo;
import com.balpos.app.repository.QuoteOfTheDayRepository;
import com.balpos.app.repository.UserInfoRepository;
import com.balpos.app.repository.UserRepository;
import com.balpos.app.security.SecurityUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
        QuoteOfTheDay quote;
        if( SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
            quote = quoteOfTheDayRepository.save(quoteOfTheDay);
        }else{
            quote = quoteOfTheDay;
        }
        return quote;
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
        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
            quoteOfTheDayRepository.delete(id);
        }
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

    @PostConstruct
    @Profile("dev")
    private void loadQuotes() throws IOException {
        if( quoteOfTheDayRepository.count() == 0 ){

            URL url = this.getClass().getResource("/quote-of-the-day-list.txt");

            File file = new File(url.getFile());

            List<String> lines = FileUtils.readLines(file, "UTF-8");

            lines.forEach(line -> {

                int index = line.indexOf("\"");

                if( index > -1 ){

                    try{

                        int openQuote = line.indexOf("\"");
                        int closeQuote = line.lastIndexOf("\"");
                        String quoteText = line.substring(openQuote +1, closeQuote -1).trim();
                        int startAuthor = line.lastIndexOf("-");
                        String author = line.substring(startAuthor +1, line.length()).trim();

                        quoteOfTheDayRepository.save(new QuoteOfTheDay(
                            quoteText,
                            author
                        ));

                    }catch (StringIndexOutOfBoundsException e){
                        System.err.println(line);
                    }

                }

            });

        }
    }

}
