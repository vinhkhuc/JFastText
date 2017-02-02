package com.github.jfasttext;

import org.junit.Test;

public class JFastTextTest {

    @Test
    public void testTrain() {
        JFastText jft = new JFastText();
        jft.runCmd(new String[] {
                "supervised",
                "-input", "src/test/resources/test_supervised_data.txt",
                "-output", "src/test/resources/test_supervised_data.model"
        });
    }
}
