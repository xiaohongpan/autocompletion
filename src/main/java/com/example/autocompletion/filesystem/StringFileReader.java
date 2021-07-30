package com.example.autocompletion.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StringFileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StringFileReader.class);

    private Map<String, Integer> countMap = new ConcurrentHashMap<>();

    public synchronized Map<String, Integer> getCountMap() {
        return new HashMap<>(countMap);
    }
    public synchronized void buildCountMap(List<String> words) {
        for (String word : words) {
            countMap.put(word, countMap.getOrDefault(word, 0) + 1);
        }
    }
}
