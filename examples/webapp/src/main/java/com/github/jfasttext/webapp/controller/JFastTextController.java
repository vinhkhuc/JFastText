package com.github.jfasttext.webapp.controller;

import com.github.jfasttext.webapp.service.JFastTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JFastTextController {

    @Autowired
    private JFastTextService jFastTextService;

    @GetMapping("/langDetect/{text}")
    public String detectLanguage(@PathVariable String text) {
        return jFastTextService.detectLanguage(text);
    }

}
