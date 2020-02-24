package com.github.jfasttext.webapp.service;

import com.github.jfasttext.JFastText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JFastTextService {
    Logger logger = LoggerFactory.getLogger(JFastTextService.class);

    private static final String FALLBACK = "__unknown__";

    @Value("${fastTextModel}")
    private String fastTextModel;

    private JFastText jft;

    @PostConstruct
    public void init() throws Exception {
        logger.info("Loading JFastText model ...");
        jft = new JFastText();
        jft.loadModel(fastTextModel);
    }

    public String detectLanguage(String text) {
        JFastText.ProbLabel probLabel = jft.predictProba(text);
        logger.info("jft: " + jft + ", text: " + text + ", probLabel: " + probLabel);
        if (probLabel == null) {
            return FALLBACK;
        } else {
            return probLabel.label;
        }
    }
}
