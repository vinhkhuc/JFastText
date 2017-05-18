#include <iostream>
#include <sstream>

#include "fasttext_wrapper.h"
#include "fastText/src/main.cc"

/**
 * FastText's wrapper
 */
namespace FastTextWrapper {

    using namespace fasttext;

    FastTextApi::FastTextApi() {
        // HACK: A trick to get access to FastText's private members.
        // Reference: http://stackoverflow.com/a/8282638
        privateMembers = (FastTextPrivateMembers*) &fastText;
    }

    void FastTextApi::runCmd(int argc, char **argv) {
        main(argc, argv);  // call fastText's main()
    }

    bool FastTextApi::checkModel(const std::string& filename) {
        // Replicate the logic in FastText::checkModel() since it's a private function
        std::ifstream in(filename, std::ifstream::binary);
        int32_t magic;
        int32_t version;
        in.read((char*)&(magic), sizeof(int32_t));
        if (magic != FASTTEXT_FILEFORMAT_MAGIC_INT32) {
            return false;
        }
        in.read((char*)&(version), sizeof(int32_t));
        if (version != FASTTEXT_VERSION) {
            return false;
        }
        return true;
    }

    void FastTextApi::loadModel(const std::string& filename) {
        fastText.loadModel(filename);
    }

    void FastTextApi::unloadModel() {
        // Call shared_ptr's reset() to release memory's ownership so
        // that the memory can be collected by GC (new in C++ 11).
        // Ref: http://en.cppreference.com/w/cpp/memory/shared_ptr/reset
        privateMembers->args_.reset();
        privateMembers->dict_.reset();
        privateMembers->input_.reset();
        privateMembers->output_.reset();
        privateMembers->model_.reset();
    }

    void FastTextApi::test(const std::string& filename, int32_t k) {
        std::ifstream ifs(filename);
        if(!ifs.is_open()) {
            std::cerr << "fasttest_wrapper.cc: Test file cannot be opened!" << std::endl;
            exit(EXIT_FAILURE);
        }
        fastText.test(ifs, k);
    }

    std::vector<std::string> FastTextApi::predict(const std::string& text, int32_t k) {
        std::vector<std::pair<real,std::string>> predictions = predictProba(text, k);
        std::vector<std::string> labels;
        for (auto it = predictions.cbegin(); it != predictions.cend(); ++it) {
            labels.push_back(it->second);
        }
        return labels;
    }

    std::vector<std::pair<real,std::string>> FastTextApi::predictProba(
            const std::string& text, int32_t k) {
        std::vector<std::pair<real,std::string>> predictions;
        std::istringstream in(text);
        fastText.predict(in, k, predictions);
        return predictions;
    }

    std::vector<real> FastTextApi::getVector(const std::string& word) {
        Vector vec(privateMembers->args_->dim);
        fastText.getVector(vec, word);
        return std::vector<real>(vec.data_, vec.data_ + vec.m_);
    }

    std::vector<std::string> FastTextApi::getWords() {
        std::vector<std::string> words;
        int32_t nwords = getNWords();
        for (int32_t i = 0; i < nwords; i++) {
            words.push_back(getWord(i));
        }
        return words;
    }

    std::vector<std::string> FastTextApi::getLabels() {
        std::vector<std::string> labels;
        int32_t nlabels = getNLabels();
        for (int32_t i = 0; i < nlabels; i++) {
            labels.push_back(getLabel(i));
        }
        return labels;
    }

    int32_t FastTextApi::getNWords() {
        return privateMembers->dict_->nwords();
    }

    std::string FastTextApi::getWord(int32_t i) {
        return privateMembers->dict_->getWord(i);
    }

    int32_t FastTextApi::getNLabels() {
        return privateMembers->dict_->nlabels();
    }

    std::string FastTextApi::getLabel(int32_t i) {
        return privateMembers->dict_->getLabel(i);
    }

    double FastTextApi::getLr() {
        return privateMembers->args_->lr;
    }

    int FastTextApi::getLrUpdateRate() {
        return privateMembers->args_->lrUpdateRate;
    }

    int FastTextApi::getDim() {
        return privateMembers->args_->dim;
    }

    int FastTextApi::getContextWindowSize() {
        return privateMembers->args_->ws;
    }

    int FastTextApi::getEpoch() {
        return privateMembers->args_->epoch;
    }

    int FastTextApi::getMinCount() {
        return privateMembers->args_->minCount;
    }

    int FastTextApi::getMinCountLabel() {
        return privateMembers->args_->minCountLabel;
    }

    int FastTextApi::getNSampledNegatives() {
        return privateMembers->args_->neg;
    }

    int FastTextApi::getWordNgrams() {
        return privateMembers->args_->wordNgrams;
    }

    std::string FastTextApi::getLossName() {
        loss_name lossName = privateMembers->args_->loss;
        if (lossName == loss_name::ns) {
            return "ns";
        } else if (lossName == loss_name::hs) {
            return "hs";
        } else if (lossName == loss_name::softmax) {
            return "softmax";
        } else {
            std::cerr << "fasttest_wrapper.cc: Unrecognized loss name!" << std::endl;
            exit(EXIT_FAILURE);
        }
    }

    std::string FastTextApi::getModelName() {
        model_name modelName = privateMembers->args_->model;
        if (modelName == model_name::cbow) {
            return "cbow";
        } else if (modelName == model_name::sg) {
            return "sg";
        } else if (modelName == model_name::sup) {
            return "sup";
        } else {
            std::cerr << "fasttest_wrapper.cc: Unrecognized model name!" << std::endl;
            exit(EXIT_FAILURE);
        }
    }

    int FastTextApi::getBucket() {
        return privateMembers->args_->bucket;
    }

    int FastTextApi::getMinn() {
        return privateMembers->args_->minn;
    }

    int FastTextApi::getMaxn() {
        return privateMembers->args_->maxn;
    }

    double FastTextApi::getSamplingThreshold() {
        return privateMembers->args_->t;
    }

    std::string FastTextApi::getLabelPrefix() {
        return privateMembers->args_->label;
    }

    std::string FastTextApi::getPretrainedVectorsFileName() {
        return privateMembers->args_->pretrainedVectors;
    }
}
