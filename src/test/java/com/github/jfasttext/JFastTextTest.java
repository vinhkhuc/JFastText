package com.github.jfasttext;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JFastTextTest {

    @Test
    public void test01TrainSupervisedCmd() {
        System.out.printf("\nTraining supervised model ...\n");
        JFastText jft = new JFastText();
        jft.runCmd(new String[] {
                "supervised",
                "-input", "src/test/resources/data/labeled_data.txt",
                "-output", "src/test/resources/models/supervised.model"
        });
    }

    @Test
    public void test02TrainSkipgramCmd() {
        System.out.printf("\nTraining skipgram word-embedding ...\n");
        JFastText jft = new JFastText();
        jft.runCmd(new String[] {
                "skipgram",
                "-input", "src/test/resources/data/unlabeled_data.txt",
                "-output", "src/test/resources/models/skipgram.model",
                "-bucket", "100",
                "-minCount", "1"
        });
    }

    @Test
    public void test03TrainCbowCmd() {
        System.out.printf("\nTraining cbow word-embedding ...\n");
        JFastText jft = new JFastText();
        jft.runCmd(new String[] {
                "skipgram",
                "-input", "src/test/resources/data/unlabeled_data.txt",
                "-output", "src/test/resources/models/cbow.model",
                "-bucket", "100",
                "-minCount", "1"
        });
    }

    @Test
    public void test04Predict() throws Exception {
        JFastText jft = new JFastText();
        jft.loadModel("src/test/resources/models/supervised.model.bin");
        String text = "I like soccer";
        String predictedLabel = jft.predict(text);
        System.out.printf("\nText: '%s', label: '%s'\n", text, predictedLabel);
    }

    @Test
    public void test05PredictProba() throws Exception {
        JFastText jft = new JFastText();
        jft.loadModel("src/test/resources/models/supervised.model.bin");
        String text = "What is the most popular sport in the US ?";
        JFastText.ProbLabel predictedProbLabel = jft.predictProba(text);
        System.out.printf("\nText: '%s', label: '%s', probability: %f\n",
                text, predictedProbLabel.label, Math.exp(predictedProbLabel.logProb));
    }

    @Test
    public void test06MultiPredictProba() throws Exception {
        JFastText jft = new JFastText();
        jft.loadModel("src/test/resources/models/supervised.model.bin");
        String text = "Do you like soccer ?";
        System.out.printf("Text: '%s'\n", text);
        for (JFastText.ProbLabel predictedProbLabel: jft.predictProba(text, 2)) {
            System.out.printf("\tlabel: '%s', probability: %f\n",
                    predictedProbLabel.label, Math.exp(predictedProbLabel.logProb));
        }
    }

    @Test
    public void test07GetVector() throws Exception {
        JFastText jft = new JFastText();
        jft.loadModel("src/test/resources/models/supervised.model.bin");
        String word = "soccer";
        List<Float> vec = jft.getVector(word);
        System.out.printf("\nWord embedding vector of '%s': %s\n", word, vec);
    }

    /**
     * Test retrieving model's information: words, labels, learning rate, etc.
     */
    @Test
    public void test08ModelInfo() throws Exception {
        System.out.printf("\nSupervised model information:\n");
        JFastText jft = new JFastText();
        jft.loadModel("src/test/resources/models/supervised.model.bin");
        System.out.printf("\tnumber of words = %d\n", jft.getNWords());
        System.out.printf("\twords = %s\n", jft.getWords());
        System.out.printf("\tlearning rate = %g\n", jft.getLr());
        System.out.printf("\tdimension = %d\n", jft.getDim());
        System.out.printf("\tcontext window size = %d\n", jft.getContextWindowSize());
        System.out.printf("\tepoch = %d\n", jft.getEpoch());
        System.out.printf("\tnumber of sampled negatives = %d\n", jft.getNSampledNegatives());
        System.out.printf("\tword ngrams = %d\n", jft.getWordNgrams());
        System.out.printf("\tloss name = %s\n", jft.getLossName());
        System.out.printf("\tmodel name = %s\n", jft.getModelName());
        System.out.printf("\tnumber of buckets = %d\n", jft.getBucket());
        System.out.printf("\tlabel prefix = %s\n\n", jft.getLabelPrefix());
    }

    /**
     * Test model unloading to release memory (Java's GC doesn't collect memory
     * allocated by native function calls).
     */
    @Test
    public void test09ModelUnloading() throws Exception {
        JFastText jft = new JFastText();
        System.out.println("\nLoading model ...");
        jft.loadModel("src/test/resources/models/supervised.model.bin");
        System.out.println("Unloading model ...");
        jft.unloadModel();
    }
}
