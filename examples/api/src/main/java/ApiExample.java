import com.github.jfasttext.JFastText;

public class ApiExample {
    public static void main(String[] args) {
        JFastText jft = new JFastText();

        // Train supervised model
        jft.runCmd(new String[] {
                "supervised",
                "-input", "../../src/test/resources/data/labeled_data.txt",
                "-output", "../../src/test/resources/models/supervised.model"
        });

        // Load model from file
        jft.loadModel("../../src/test/resources/models/supervised.model.bin");

        // Do label prediction
        String text = "What is the most popular game in the US ?";
        JFastText.ProbLabel probLabel = jft.predictProba(text);
        System.out.printf("\nThe label of '%s' is '%s' with probability %f\n",
                text, probLabel.label, Math.exp(probLabel.logProb));
    }
}
