## Introduction
JFastText is a Java wrapper for Facebook's [fastText](https://github.com/facebookresearch/fastText), 
a library for efficient learning of word embeddings and fast sentence classification. The JNI interface
is built using [javacpp](https://github.com/bytedeco/javacpp).

The library provides full fastText's command line interface. It also provides the API for
loading trained model from file to do label prediction in memory. Model training and quantization
are supported via the command line interface.

JFastText is ideal for building fast text classifiers in Java.

## Maven dependency
```xml
<dependency>
  <groupId>com.github.vinhkhuc</groupId>
  <artifactId>jfasttext</artifactId>
  <version>0.3</version>
</dependency>
```
The Jar package on Maven Central is bundled with precompiled fastText library for Windows, Linux and
MacOSX 64bit.

## Building
C++ compiler (g++ on Mac/Linux or cl.exe on Windows) is required to compile fastText's code.

```bash
git clone --recursive https://github.com/vinhkhuc/JFastText
cd JFastText
mvn package
```

## Examples
Examples on how to use JFastText can be found at [examples/api](examples/api) and [examples/cmd](examples/cmd).

## How to use

### Initialization

```java
import com.github.jfasttext.JFastText;
...
JFastText jft = new JFastText();
```

### Word embedding learning
```java
jft.runCmd(new String[] {
        "skipgram",
        "-input", "src/test/resources/data/unlabeled_data.txt",
        "-output", "src/test/resources/models/skipgram.model",
        "-bucket", "100",
        "-minCount", "1"
});
```

### Text classification
```java
// Train supervised model
jft.runCmd(new String[] {
        "supervised",
        "-input", "src/test/resources/data/labeled_data.txt",
        "-output", "src/test/resources/models/supervised.model"
});

// Load model from file
jft.loadModel("src/test/resources/models/supervised.model.bin");

// Do label prediction
String text = "What is the most popular sport in the US ?";
JFastText.ProbLabel probLabel = jft.predictProba(text);
System.out.printf("\nThe label of '%s' is '%s' with probability %f\n",
        text, probLabel.label, Math.exp(probLabel.logProb));
```

### FastText's command line
```bash
$ java -jar target/jfasttext-*-jar-with-dependencies.jar
usage: fasttext <command> <args>

The commands supported by fasttext are:

  supervised              train a supervised classifier
  quantize                quantize a model to reduce the memory usage
  test                    evaluate a supervised classifier
  predict                 predict most likely labels
  predict-prob            predict most likely labels with probabilities
  skipgram                train a skipgram model
  cbow                    train a cbow model
  print-word-vectors      print word vectors given a trained model
  print-sentence-vectors  print sentence vectors given a trained model
  nn                      query for nearest neighbors
  analogies               query for analogies

```

For example:

```bash
$ java -jar target/jfasttext-*-jar-with-dependencies.jar quantize -h
```

## License
BSD

## References
(From fastText's [references](https://github.com/facebookresearch/fastText#references))

Please cite [1](#enriching-word-vectors-with-subword-information) if using this code for learning word representations or [2](#bag-of-tricks-for-efficient-text-classification) if using for text classification.

### Enriching Word Vectors with Subword Information

[1] P. Bojanowski\*, E. Grave\*, A. Joulin, T. Mikolov, [*Enriching Word Vectors with Subword Information*](https://arxiv.org/abs/1607.04606)

```
@article{bojanowski2016enriching,
  title={Enriching Word Vectors with Subword Information},
  author={Bojanowski, Piotr and Grave, Edouard and Joulin, Armand and Mikolov, Tomas},
  journal={arXiv preprint arXiv:1607.04606},
  year={2016}
}
```

### Bag of Tricks for Efficient Text Classification

[2] A. Joulin, E. Grave, P. Bojanowski, T. Mikolov, [*Bag of Tricks for Efficient Text Classification*](https://arxiv.org/abs/1607.01759)

```
@article{joulin2016bag,
  title={Bag of Tricks for Efficient Text Classification},
  author={Joulin, Armand and Grave, Edouard and Bojanowski, Piotr and Mikolov, Tomas},
  journal={arXiv preprint arXiv:1607.01759},
  year={2016}
}
```

### FastText.zip: Compressing text classification models

[3] A. Joulin, E. Grave, P. Bojanowski, M. Douze, H. Jégou, T. Mikolov, [*FastText.zip: Compressing text classification models*](https://arxiv.org/abs/1612.03651)

```
@article{joulin2016fasttext,
  title={FastText.zip: Compressing text classification models},
  author={Joulin, Armand and Grave, Edouard and Bojanowski, Piotr and Douze, Matthijs and J{\'e}gou, H{\'e}rve and Mikolov, Tomas},
  journal={arXiv preprint arXiv:1612.03651},
  year={2016}
}
```

(\* These authors contributed equally.)
