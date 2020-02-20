package com.github.jfasttext.service;

import com.github.jfasttext.JFastText;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JFastTextService {
    private JFastText jft;

    @PostConstruct
    public void init() {
        System.out.print("Loading JFastText model ...");
        jft = new JFastText();
        jft.loadModel("../../src/test/resources/models/supervised.model.bin");
    }

    public String detectLanguage(String text) {
        JFastText.ProbLabel probLabel = jft.predictProba(text);
        return probLabel.label;
    }
}
