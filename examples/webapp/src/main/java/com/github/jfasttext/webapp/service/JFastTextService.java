package com.github.jfasttext.webapp.service;

import com.github.jfasttext.JFastText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JFastTextService {
    Logger logger = LoggerFactory.getLogger(JFastTextService.class);
    private JFastText jft;

    @PostConstruct
    public void init() {
        logger.info("Loading JFastText model ...");
        jft = new JFastText();
        jft.loadModel("../../src/test/resources/models/supervised.model.bin");
    }

    public String detectLanguage(String text) {
        JFastText.ProbLabel probLabel = jft.predictProba(text);
        logger.info("jft: " + jft);
        logger.info("text: " + text);
        logger.info("probLabel: " + probLabel);
        return probLabel.label;
    }
}
