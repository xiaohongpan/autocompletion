package com.example.autocompletion.wordstore;

import lombok.Data;

import java.util.*;

@Data
public class WordStore {
    private String compactWord;
    private Map<Character, WordStore> storeMap;
    private Set<String> autoCompletionWords;
    private boolean endWord;

    public WordStore() {
        compactWord = "";
        storeMap = new HashMap<>();
        autoCompletionWords = new HashSet<>();
        endWord = false;
    }
}
