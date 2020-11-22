[![Build Status](https://travis-ci.org/carschno/JFastText.svg?branch=master)](https://travis-ci.org/carschno/JFastText)

Table of Contents
=================

  * [Introduction](#introduction)
  * [Maven Dependency](#maven-dependency)
    * [Windows and Mac OSX](#windows-and-mac-os-x)
  * [Building](#building)
  * [Quick Application - Language Identification](#quick-application-\--language-identification)
  * [Detailed Examples](#detailed-examples)
  * [API](#api)
  * [FastText's Command Line](#fasttexts-command-line)
  * [License](#license)
  * [References](#references)
  * [Changelog](CHANGELOG.md)
  

## Introduction
JFastText is a Java wrapper for Facebook's [fastText](https://github.com/facebookresearch/fastText), 
a library for efficient learning of word embeddings and fast sentence classification. The JNI interface
is built using [javacpp](https://github.com/bytedeco/javacpp).

The library provides full fastText's command line interface. It also provides the API for
loading trained model from file to do label prediction in memory. Model training and quantization
are supported via the command line interface.

JFastText is ideal for building fast text classifiers in Java.

## Maven Dependency
```xml
<dependency>
  <groupId>io.github.carschno</groupId>
  <artifactId>jfasttext</artifactId>
  <version>0.9.1</version>
</dependency>
```
The Jar package on Maven Central is bundled with precompiled fastText library for ~~Windows,~~ Linux ~~and
MacOSX~~ 64bit.

### Windows and Mac OS X

Currently, the Maven dependency only contains binaries for Linux (64 bit), _not_ for Windows or Mac OS X.
In order to use JFastText for Windows or Mac OS X (or any other system), you need to build it yourself (see [below](#building)). 

## Building
C++ compiler (g++ on Mac/Linux or `cl.exe` on Windows) is required to compile fastText's code.

```bash
git clone --recursive https://github.com/carschno/JFastText
cd JFastText
git submodule init
git submodule update
mvn package
```

### Building on Windows

The (automatic) build seems to fail on some Windows systems/C++ compilers.
See [this issue](https://github.com/carschno/JFastText/issues/5#issuecomment-546485377): 

> I used MS's developer tools, not the full-blown Visual Studio. If I run `cl` directly, the compilation fails with the same error.
>
>  I was able to build on Windows by changing the call to `cl.exe` and running it outside the Maven build. I changed one parameter in the call to `cl`: I use `/MT` (whereas Maven uses `/MD`). Bundling the generated DLLs works fine.

## Quick Application - Language Identification
JFastText can use FastText's pretrained models directly. Language identification models can be downloaded [here](https://fasttext.cc/docs/en/language-identification.html).
In this quick example, we will use the [quantized model](https://s3-us-west-1.amazonaws.com/fasttext-vectors/supervised_models/lid.176.ftz)
which is super small and a bit less accurate than the original model.

```bash
$ wget -q https://s3-us-west-1.amazonaws.com/fasttext-vectors/supervised_models/lid.176.ftz \
    && { echo "This is English"; echo "Xin chào"; echo "Привет"; } \
    | java -jar target/jfasttext-*-jar-with-dependencies.jar predict lid.176.ftz -
__label__en
__label__vi
__label__ru
```

## Detailed Examples
Examples on how to use JFastText can be found at [examples/api](examples/api) and [examples/cmd](examples/cmd).

## API

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

## FastText's Command Line
FastText's command line interface can be accessed as follows:
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
  print-ngrams            print ngrams given a trained model and word
  nn                      query for nearest neighbors
  analogies               query for analogies
  dump                    dump arguments,dictionary,input/output vectors

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
