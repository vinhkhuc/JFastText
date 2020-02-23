package com.github.jfasttext.webapp.controller;

import com.github.jfasttext.webapp.service.JFastTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class JFastTextController {

    @Autowired
    private JFastTextService jFastTextService;

    @RequestMapping(value="langDetect", method = RequestMethod.GET)
    public @ResponseBody
    String detectLanguage(@RequestParam("text") String text) {
        return jFastTextService.detectLanguage(text);
    }

}
