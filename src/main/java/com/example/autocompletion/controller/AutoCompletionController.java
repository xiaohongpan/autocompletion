package com.example.autocompletion.controller;

import com.example.autocompletion.wordstore.WordStoreHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class AutoCompletionController {

    private WordStoreHandler wordStoreHandler;

    @Autowired
    public AutoCompletionController(WordStoreHandler wordStoreHandler) {
        this.wordStoreHandler = wordStoreHandler;
    }

    @GetMapping
    public List<String> searchForAutoCompletions(@RequestParam("word") String word) {
        return wordStoreHandler.getAutoCompletionWords(word);
    }
}
