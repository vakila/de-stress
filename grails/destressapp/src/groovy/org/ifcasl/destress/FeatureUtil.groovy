package org.ifcasl.destress

class FeatureUtil {

    public static extractSentenceUtteranceFeatures(SentenceUtterance sentUtt) {

    }

    public static extractWordUtteranceFeatures(WordUtterance wordUtt) {
        println("Current features for WordUtterance: " + wordUtt.toString())
        printWordUtteranceFeatures(wordUtt)



        println("\nPerforming word feature extraction...")
        String word = wordUtt.word.text
        String wav = wordUtt.sentenceUtterance.waveFile
        String grid = wordUtt.sentenceUtterance.gridFile

        FeatureExtractor fx = new FeatureExtractor(wav, grid, word)


        wordUtt.WORD_DUR = fx.getWordDuration()
        wordUtt.SYLL0_DUR = fx.getSyllableDuration(0)
        wordUtt.SYLL1_DUR = fx.getSyllableDuration(1)
        wordUtt.WORD_F0_MEAN = fx.getWordF0Mean()
        wordUtt.SYLL0_F0_MEAN = fx.getSyllableF0Mean(0)
        wordUtt.SYLL1_F0_MEAN = fx.getSyllableF0Mean(1)
        wordUtt.SYLL0_F0_RANGE = fx.getSyllableF0Range(0)
        wordUtt.SYLL1_F0_RANGE = fx.getSyllableF0Range(1)
        wordUtt.save()
        println("Done.\n")


        println("New features for WordUtterance: " + wordUtt.toString())
        printWordUtteranceFeatures(wordUtt)

    }

    public static printWordUtteranceFeatures(WordUtterance wordUtt) {
        println("WORD_DUR:       " + wordUtt.WORD_DUR)
        println("SYLL0_DUR:      " + wordUtt.SYLL0_DUR)
        println("SYLL1_DUR:      " + wordUtt.SYLL1_DUR)
        println("WORD_F0_MEAN:   " + wordUtt.WORD_F0_MEAN)
        println("SYLL0_F0_MEAN:  " + wordUtt.SYLL0_F0_MEAN)
        println("SYLL1_F0_MEAN:  " + wordUtt.SYLL1_F0_MEAN)
        println("SYLL0_F0_RANGE: " + wordUtt.SYLL0_F0_RANGE)
        println("SYLL1_F0_RANGE: " + wordUtt.SYLL1_F0_RANGE)
    }

}
