package org.ifcasl.destress

class WordUtterance {

    SentenceUtterance sentenceUtterance

    Word word

    // Weka instance?

    String toString() {
        return sentenceUtterance.toString() + "_" + word.text
    }


    //
    // //// Duration
    // Float WORD_DUR
    // Float SYLL0_DUR
    // Float SYLL1_DUR
    // Float V0_DUR
    // Float V1_DUR
    // Float REL_SYLL_DUR
    // Float REL_V_DUR
    //
    // //// F0
    // //Word
    // Float WORD_F0_MEAN
    // Float WORD_F0_MAX
    // Float WORD_F0_MIN
    // Float WORD_F0_RANGE
    // //Syllables
    // Float SYLL0_F0_MEAN
    // Float SYLL0_F0_MAX
    // Float SYLL0_F0_MIN
    // Float SYLL0_F0_RANGE
    // Float SYLL1_F0_MEAN
    // Float SYLL1_F0_MAX
    // Float SYLL1_F0_MIN
    // Float SYLL1_F0_RANGE
    // //Vowels
    // Float V0_F0_MEAN
    // Float V0_F0_MAX
    // Float V0_F0_MIN
    // Float V0_F0_RANGE
    // Float V1_F0_MEAN
    // Float V1_F0_MAX
    // Float V1_F0_MIN
    // Float V1_F0_RANGE
    // //Relative
    // Float REL_SYLL_F0_MEAN
    // Float REL_SYLL_F0_MAX
    // Float REL_SYLL_F0_MIN
    // Float REL_SYLL_F0_RANGE
    // Float REL_VOWEL_F0_MEAN
    // Float REL_VOWEL_F0_MAX
    // Float REL_VOWEL_F0_MIN
    // Float REL_VOWEL_F0_RANGE
    // Integer F0_MAX_INDEX
    // Integer F0_MIN_INDEX
    // Integer F0_MAXRANGE_INDEX
    //
    // //// Energy
    // //Word
    // Float WORD_ENERGY_MEAN
    // Float WORD_ENERGY_MAX
    // //Syllables
    // Float SYLL0_ENERGY_MEAN
    // Float SYLL0_ENERGY_MAX
    // Float SYLL1_ENERGY_MEAN
    // Float SYLL1_ENERGY_MAX
    // //Vowels
    // Float V0_ENERGY_MEAN
    // Float V0_ENERGY_MAX
    // Float V1_ENERGY_MEAN
    // Float V1_ENERGY_MAX
    // //Relative
    // Float REL_SYLL_ENERGY_MEAN
    // Float REL_SYLL_ENERGY_MAX
    // Float REL_VOWEL_ENERGY_MEAN
    // Float REL_VOWEL_ENERGY_MAX
    // Integer ENERGY_MAX_INDEX

}
