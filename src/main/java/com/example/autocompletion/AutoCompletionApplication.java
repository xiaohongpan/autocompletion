package com.example.autocompletion;

import com.example.autocompletion.filesystem.StringFileReader;
import com.example.autocompletion.wordstore.WordStoreHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class AutoCompletionApplication {
    private WordStoreHandler wordStoreHandler;
    private StringFileReader stringFileReader;

    @Autowired
    public AutoCompletionApplication(WordStoreHandler wordStoreHandler, StringFileReader stringFileReader) {
        this.wordStoreHandler = wordStoreHandler;
        this.stringFileReader = stringFileReader;
    }

    public static void main(String[] args) {
        SpringApplication.run(AutoCompletionApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            List<String> words = new ArrayList<>();
            File file = ResourceUtils.getFile("classpath:test.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s = bufferedReader.readLine();
            while (s != null) {
                s = s.trim();
                words.add(s);
                s = bufferedReader.readLine();
            }
            stringFileReader.buildCountMap(words);
            Map<String, Integer> countMap = stringFileReader.getCountMap();
            wordStoreHandler.buildCompactTrie(countMap);
        };
    }
}
