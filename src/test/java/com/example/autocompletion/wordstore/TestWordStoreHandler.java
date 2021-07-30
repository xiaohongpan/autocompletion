package com.example.autocompletion.wordstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class TestWordStoreHandler {
    @Autowired
    private WordStoreHandler wordStoreHandler;

    @Test
    public void basicTest() {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("apple", 30);
        countMap.put("app", 100);
        countMap.put("bank", 300);
        countMap.put("bat", 5000);
        countMap.put("ban", 100);
        countMap.put("corn", 400);
        countMap.put("cornel", 200);
        countMap.put("his", 2300);
        countMap.put("her", 300);
        countMap.put("history", 1200);
        countMap.put("ray", 1340);
        wordStoreHandler.buildCompactTrie(countMap);

        Map<String, Set<String>> expectAutoCompletionWords = getExpectAutoCompletionWords();
        for (Map.Entry<String, Set<String>> entry : expectAutoCompletionWords.entrySet()) {
            String word = entry.getKey();
            Set<String> expectAutoCompletions = entry.getValue();
            List<String> autoCompletions = wordStoreHandler.getAutoCompletionWords(word);
            Assertions.assertEquals(expectAutoCompletions, new HashSet<>(autoCompletions));
        }
    }

    private Map<String, Set<String>> getExpectAutoCompletionWords() {
        Map<String, Set<String>> expectAutoCompletionWords = new HashMap<>();
        expectAutoCompletionWords.put("", new HashSet<String>() {{ add("app"); add("bat"); add("ban"); add("bank"); add("corn"); add("cornel"); add("his"); add("her"); add("history"); add("ray");}});
        expectAutoCompletionWords.put("a", new HashSet<String>() {{add("apple"); add("app");}});
        expectAutoCompletionWords.put("ap", new HashSet<String>() {{add("apple"); add("app");}});
        expectAutoCompletionWords.put("app", new HashSet<String>() {{add("apple"); add("app");}});
        expectAutoCompletionWords.put("appl", new HashSet<String>() {{add("apple");}});
        expectAutoCompletionWords.put("apple", new HashSet<String>() {{add("apple");}});
        expectAutoCompletionWords.put("b", new HashSet<String>() {{add("bank"); add("bat"); add("ban");}});
        expectAutoCompletionWords.put("ba", new HashSet<String>() {{add("bank"); add("bat"); add("ban");}});
        expectAutoCompletionWords.put("ban", new HashSet<String>() {{add("bank"); add("ban");}});
        expectAutoCompletionWords.put("bank", new HashSet<String>() {{add("bank"); }});
        expectAutoCompletionWords.put("bat", new HashSet<String>() {{add("bat");}});
        expectAutoCompletionWords.put("c", new HashSet<String>() {{add("corn"); add("cornel");}});
        expectAutoCompletionWords.put("co", new HashSet<String>() {{add("corn"); add("cornel");}});
        expectAutoCompletionWords.put("cor", new HashSet<String>() {{add("corn"); add("cornel");}});
        expectAutoCompletionWords.put("corn", new HashSet<String>() {{add("corn"); add("cornel");}});
        expectAutoCompletionWords.put("corne", new HashSet<String>() {{add("cornel");}});
        expectAutoCompletionWords.put("cornel", new HashSet<String>() {{add("cornel");}});
        expectAutoCompletionWords.put("h", new HashSet<String>() {{add("his"); add("her"); add("history");}});
        expectAutoCompletionWords.put("he", new HashSet<String>() {{add("her"); }});
        expectAutoCompletionWords.put("her", new HashSet<String>() {{add("her"); }});
        expectAutoCompletionWords.put("hi", new HashSet<String>() {{add("his"); add("history");}});
        expectAutoCompletionWords.put("his", new HashSet<String>() {{add("his"); add("history");}});
        expectAutoCompletionWords.put("hist", new HashSet<String>() {{add("history");}});
        expectAutoCompletionWords.put("histo", new HashSet<String>() {{add("history");}});
        expectAutoCompletionWords.put("histor", new HashSet<String>() {{add("history");}});
        expectAutoCompletionWords.put("history", new HashSet<String>() {{add("history");}});
        expectAutoCompletionWords.put("r", new HashSet<String>() {{add("ray");}});
        expectAutoCompletionWords.put("ra", new HashSet<String>() {{add("ray");}});
        expectAutoCompletionWords.put("ray", new HashSet<String>() {{add("ray");}});

        return expectAutoCompletionWords;
    }

}
