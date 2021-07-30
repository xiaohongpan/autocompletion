package com.example.autocompletion.wordstore;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class WordStoreHandler {
    private static final int MAX_AUTO_COMPLETION_WORD = 10;

    private WordStore rootWordStore;

    public WordStoreHandler() {
        rootWordStore = new WordStore();
    }

    private void insert(String origWord, String word, WordStore wordStore, Map<String, Integer> countMap) {
        Character c = null;
        String nextWord = "";
        String storeWord = wordStore.getCompactWord();
        Map<Character, WordStore> storeMap = wordStore.getStoreMap();
        Set<String> autoCompletionWords = wordStore.getAutoCompletionWords();

        if (storeWord.isEmpty()) {
            if (storeMap.isEmpty() && !wordStore.isEndWord()) {
                wordStore.setCompactWord(word);
                wordStore.setEndWord(true);
                updateAutoCompletions(wordStore.getAutoCompletionWords(), origWord, countMap);
                return;
            }
            c = word.charAt(0); // First char
            nextWord = word.substring(1);
        } else {
            // Find the common prefix length
            int i = 0;
            while (i < Math.min(word.length(), storeWord.length()) && word.charAt(i) == storeWord.charAt(i)) {
                i++;
            }
            // If the current WordStore needs to be split, create a child WordStore
            if (i < storeWord.length()) {
                WordStore newWordStore = new WordStore();
                newWordStore.setCompactWord(storeWord.substring(i + 1));
                newWordStore.setStoreMap(storeMap);
                newWordStore.setAutoCompletionWords(new HashSet<>(autoCompletionWords));
                newWordStore.setEndWord(true);
                wordStore.setCompactWord(storeWord.substring(0, i));
                storeMap = new HashMap<>();
                wordStore.setStoreMap(storeMap);
                storeMap.put(storeWord.charAt(i), newWordStore);
                // Add the newWordStore's word to current storeWord as auto-completion
                String newWord = origWord.substring(0, origWord.length() - word.length()) + storeWord;
                if (countMap.containsKey(newWord)) {
                    updateAutoCompletions(autoCompletionWords, newWord, countMap);
                }
                wordStore.setEndWord(false);
            }
            if (i < word.length()) {
                c = word.charAt(i);
                nextWord = word.substring(i + 1);
            } else {
                wordStore.setEndWord(true);
            }
        }
        if (c != null) {
            updateAutoCompletions(autoCompletionWords, origWord, countMap);
            storeMap.putIfAbsent(c, new WordStore());
            insert(origWord, nextWord, storeMap.get(c), countMap);
        }
    }

    private void updateAutoCompletions(Set<String> autoCompletionWords, String newWord, Map<String, Integer> countMap) {
        autoCompletionWords.add(newWord);
        if (autoCompletionWords.size() > MAX_AUTO_COMPLETION_WORD) {
            Queue<String> queue = new PriorityQueue<String>((a, b) -> countMap.get(a) - countMap.get(b));
            queue.add(newWord);
            for (String autoCompletionWord : autoCompletionWords) {
                queue.add(autoCompletionWord);
            }
            String word = queue.poll();
            autoCompletionWords.remove(word);
        }
    }
    // Sorted by word length, same length sort by alphabetic
    public synchronized void buildCompactTrie(Map<String, Integer> countMap) {
        Map<String, Integer> tmpCountMap = new TreeMap<String, Integer>(new Comparator<String>() {
            public int compare(String a, String b) {
                int cmp = a.length() - b.length();
                if (cmp == 0) {
                    cmp = a.compareTo(b);
                }
                return cmp;
            }
        });
        tmpCountMap.putAll(countMap);
        for (Map.Entry<String, Integer> tmpCountEntry : tmpCountMap.entrySet()) {
            String word = tmpCountEntry.getKey();
            insert(word, word, rootWordStore, countMap);
        }
    }

    private List<String> search(String word, WordStore wordStore) {
        String compactWord = wordStore.getCompactWord();
        int i = 0;
        while (i < Math.min(word.length(), compactWord.length()) && word.charAt(i) == compactWord.charAt(i)) {
            i++;
        }
        if (i >= word.length()) {
            return new ArrayList<>(wordStore.getAutoCompletionWords());
        }
        char c = word.charAt(i);
        String nextWord = word.substring(i + 1);
        Map<Character, WordStore> storeMap = wordStore.getStoreMap();
        if (storeMap.containsKey(c)) {
            return search(nextWord, storeMap.get(c));
        }
        return Collections.emptyList();
    }

    public synchronized List<String> getAutoCompletionWords(String word) {
        return search(word, rootWordStore);
    }
}
