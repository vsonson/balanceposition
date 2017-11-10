package com.balpos.app.service;

import com.balpos.app.BalancepositionApp;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BalancepositionApp.class)
public class QuoteOfTheDayLoaderTest {

    @Test
    public void testReadQuoteFile() throws IOException {

        URL url = this.getClass().getResource("/quote-of-the-day-list-test.txt");

        File file = new File(url.getFile());

        List<String> lines = FileUtils.readLines(file, "UTF-8");

        final int[] count = {0};
        lines.forEach(new Consumer<String>() {
            @Override
            public void accept(String line) {
                int index = line.indexOf("\"");
                if( index > -1 ){
                    String quote = line.substring(index, line.length());
                    System.out.println(quote);
                }

            }
        });

    }

}
