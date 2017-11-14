package com.balpos.app.util;

import com.balpos.app.domain.QuoteOfTheDay;
import com.balpos.app.repository.QuoteOfTheDayRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Configuration
public class AppInitializer {

    private final QuoteOfTheDayRepository quoteOfTheDayRepository;

    @Autowired
    public AppInitializer(QuoteOfTheDayRepository quoteOfTheDayRepository) {
        this.quoteOfTheDayRepository = quoteOfTheDayRepository;
    }

    @PostConstruct
    public void loadQuoteFile() throws IOException {

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
