#ifndef FASTTEXT_WRAPPER_MISC_H
#define FASTTEXT_WRAPPER_MISC_H

#include "fastText/src/args.h"
#include "fastText/src/dictionary.h"
#include "fastText/src/matrix.h"
#include "fastText/src/model.h"

/**
 * FastText's wrapper misc
 */
namespace FastTextWrapper {

    struct FastTextPrivateMembers {
        std::shared_ptr <fasttext::Args> args_;
        std::shared_ptr <fasttext::Dictionary> dict_;
        std::shared_ptr <fasttext::Matrix> input_;
        std::shared_ptr <fasttext::Matrix> output_;
        std::shared_ptr <fasttext::Model> model_;
    };
}

#endif
