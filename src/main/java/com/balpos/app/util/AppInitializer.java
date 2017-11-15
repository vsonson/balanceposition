package com.balpos.app.util;

import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.domain.User;
import com.balpos.app.domain.UserInfo;
import com.balpos.app.repository.QuoteOfTheDayRepository;
import com.balpos.app.repository.UserInfoRepository;
import com.balpos.app.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Configuration
public class AppInitializer {

    private final QuoteOfTheDayRepository quoteOfTheDayRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppInitializer(
        QuoteOfTheDayRepository quoteOfTheDayRepository,
        UserInfoRepository userInfoRepository,
        UserRepository userRepository
    ) {
        this.quoteOfTheDayRepository = quoteOfTheDayRepository;
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void loadData() throws IOException {

        loadUserInfo();

        loadQuotes();


    }

    private void loadUserInfo() {
        Optional<User> optional = userRepository.findOneByLogin("user");
        if( optional.isPresent()){
            User user = optional.get();
            UserInfo userInfo = userInfoRepository.findOneByUser(user);
            if( userInfo == null){
                userInfo = new UserInfo();
                userInfo.setUser(user);
                userInfoRepository.save(userInfo);
            }

            User admin = userRepository.findOneByLogin("admin").get();
            UserInfo adminInfo = userInfoRepository.findOneByUser(admin);
            if( adminInfo == null){
                adminInfo = new UserInfo();
                adminInfo.setUser(admin);
                userInfoRepository.save(adminInfo);
            }
        }
    }

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
