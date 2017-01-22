#ifndef FASTTEXT_WRAPPER_H
#define FASTTEXT_WRAPPER_H

#include "fastText/src/fasttext.h"
#include "fasttext_wrapper_misc.h"

/**
 * FastText's wrapper
 */
namespace FastTextWrapper {

    using namespace fasttext;

    class FastTextApi {
    private:
        FastText fastText;
        FastTextPrivateMembers* privateMembers;
    public:
        FastTextApi();
        // We don't make runTrainCmd() a static method so that Loader.load() is always be called in FastTextApi().
        void runTrainCmd(int, char **);
        void loadModel(const std::string&);
        void test(const std::string&, int32_t);
        // TODO: Check if model was loaded
        std::vector<std::string> predict(const std::string&, int32_t);
        std::vector<std::pair<real,std::string>> predictProba(const std::string&, int32_t);
        std::vector<real> getVector(const std::string&);
        std::vector<std::string> getWords();
        std::vector<std::string> getLabels();
        std::string getWord(int32_t);
        int32_t getNWords();
        std::string getLabel(int32_t);
        int32_t getNLabels();
        std::string getInput();
        std::string getOutput();
        double getLr();
        int getLrUpdateRate();
        int getDim();
        int getContextWindowSize();
        int getEpoch();
        int getMinCount();
        int getMinCountLabel();
        int getNSampledNegatives();
        int getWordNgrams();
        std::string getLossName();
        std::string getModelName();
        int getBucket();
        int getMinn();
        int getMaxn();
        double getSamplingThreshold();
        std::string getLabelPrefix();
        std::string getPretrainedVectorsFileName();
    };
}

//int main(int argc, char** argv) {
//    FastTextWrapper::FastTextApi fta;
//    fta.loadModel("../javacpp/test.model.bin");
//    std::cout << "Loading model ..." << std::endl;
//    std::vector<float> vec = fta.getVector("you");
//    std::cout << vec.front();
//    return 0;
//}

#endif
