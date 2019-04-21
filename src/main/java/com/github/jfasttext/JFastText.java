package com.github.jfasttext;

import org.bytedeco.javacpp.PointerPointer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class JFastText {

    private FastTextWrapper.FastTextApi fta = new FastTextWrapper.FastTextApi();;

    public JFastText() {
    }

    public JFastText(final String modelFile) {
        loadModel(modelFile);
    }

    public JFastText(final URI modelUri) throws IOException {
        loadModel(modelUri);
    }

    public JFastText(final URL modelUrl) throws IOException {
        loadModel(modelUrl);
    }

    public JFastText(final InputStream modelStream) throws IOException {
        loadModel(modelStream);
    }

    public void runCmd(String[] args) {
        // Prepend "fasttext" to the argument list so that it is compatible with C++'s main()
        String[] cArgs = new String[args.length + 1];
        cArgs[0] = "fasttext";
        System.arraycopy(args, 0, cArgs, 1, args.length);
        fta.runCmd(cArgs.length, new PointerPointer(cArgs));
    }

    public void loadModel(String modelFile) throws IllegalArgumentException {
        if (!new File(modelFile).exists()) {
            throw new IllegalArgumentException("Model file " + modelFile + " doesn't exist!");
        }
        if (!fta.checkModel(modelFile)) {
            throw new IllegalArgumentException(
                    "Model file's format is not compatible with this JFastText version!");
        }
        fta.loadModel(modelFile);
    }

    /**
     * Loads model from location specified by URI, by copying its content into local file & then loading it.
     *
     * @param modelUri location of the model
     */
    public void loadModel(URI modelUri) throws IOException {
        loadModel(modelUri.toURL());
    }

    /**
     * Loads model from location specified by URL, by copying its content into local file & then loading it.
     *
     * @param modelUrl location of the model
     */
    public void loadModel(URL modelUrl) throws IOException {
        loadModel(modelUrl.openStream());
    }

    /**
     * Loads model given InputStream, by copying its content into local file & then loading it.
     *
     * @param modelStream stream for model
     */
    public void loadModel(InputStream modelStream) throws IOException {
        Path tmpFile = Files.createTempFile("jft-", ".model");
        try {
            Files.copy(modelStream, tmpFile, StandardCopyOption.REPLACE_EXISTING);
            loadModel(tmpFile.toString());
        } finally {
            Files.deleteIfExists(tmpFile);
        }
    }

    public void unloadModel() {
        fta.unloadModel();
    }

    public void test(String testFile) {
        test(testFile, 1);
    }

    public void test(String testFile, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        fta.test(testFile, k);
    }

    public String predict(String text){
        List<String> predictions = predict(text, 1);
        return predictions.size() > 0? predictions.get(0): null;
    }

    public List<String> predict(String text, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        FastTextWrapper.StringVector sv = fta.predict(text, k);
        List<String> predictions = new ArrayList<>();
        for (int i = 0; i < sv.size(); i++) {
            predictions.add(sv.get(i).getString());
        }
        return predictions;
    }

    public ProbLabel predictProba(String text){
        List<ProbLabel> probaPredictions = predictProba(text, 1);
        return probaPredictions.size() > 0? probaPredictions.get(0): null;
    }

    public List<ProbLabel> predictProba(String text, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        FastTextWrapper.FloatStringPairVector fspv = fta.predictProba(text, k);
        List<ProbLabel> probaPredictions = new ArrayList<>();
        for (int i = 0; i < fspv.size(); i++) {
            float logProb = fspv.first(i);
            String label = fspv.second(i).getString();
            probaPredictions.add(new ProbLabel(logProb, label));
        }
        return probaPredictions;
    }

    public List<Float> getVector(String word) {
        FastTextWrapper.RealVector rv = fta.getVector(word);
        List<Float> wordVec = new ArrayList<>();
        for (int i = 0; i < rv.size(); i++) {
            wordVec.add(rv.get(i));
        }
        return wordVec;
    }

    public int getNWords() {
        return fta.getNWords();
    }

    public List<String> getWords() {
        return stringVec2Strings(fta.getWords());
    }

    public int getNLabels() {
        return fta.getNLabels();
    }

    public List<String> getLabels() {
        return stringVec2Strings(fta.getWords());
    }

    public double getLr() {
        return fta.getLr();
    }

    public int getLrUpdateRate() {
        return fta.getLrUpdateRate();
    }

    public int getDim() {
        return fta.getDim();
    }

    public int getContextWindowSize() {
        return fta.getContextWindowSize();
    }

    public int getEpoch() {
        return fta.getEpoch();
    }

    public int getMinCount() {
        return fta.getMinCount();
    }

    public int getMinCountLabel() {
        return fta.getMinCountLabel();
    }

    public int getNSampledNegatives() {
        return fta.getNSampledNegatives();
    }

    public int getWordNgrams() {
        return fta.getWordNgrams();
    }

    public String getLossName() {
        return fta.getLossName().getString();
    }

    public String getModelName() {
        return fta.getModelName().getString();
    }

    public int getBucket() {
        return fta.getBucket();
    }

    public int getMinn() {
        return fta.getMinn();
    }

    public int getMaxn() {
        return fta.getMaxn();
    }

    public double getSamplingThreshold() {
        return fta.getSamplingThreshold();
    }

    public String getLabelPrefix() {
        return fta.getLabelPrefix().getString();
    }

    public String getPretrainedVectorsFileName() {
        return fta.getPretrainedVectorsFileName().getString();
    }

    private static List<String> stringVec2Strings(FastTextWrapper.StringVector sv) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < sv.size(); i++) {
            strings.add(sv.get(i).getString());
        }
        return strings;
    }

    public static class ProbLabel {
        public float logProb;
        public String label;
        public ProbLabel(float logProb, String label) {
            this.logProb = logProb;
            this.label = label;
        }
        @Override
        public String toString() {
            return String.format("logProb = %f, label = %s", logProb, label);
        }
    }

    public static void main(String[] args) {
        JFastText jft = new JFastText();
        jft.runCmd(args);
    }
}
