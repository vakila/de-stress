package org.ifcasl.destress

class FeatureUtil {

    public static extractSentenceUtteranceFeatures(SentenceUtterance sentUtt) {

    }

    public static extractWordUtteranceFeatures(WordUtterance wordUtt) {
        println("Current features for WordUtterance: " + wordUtt.toString())
        println(wordUtt.WORD_DUR)
        println(wordUtt.SYLL0_DUR)
        println(wordUtt.SYLL1_DUR)
        println(wordUtt.SYLL0_F0_MEAN)
        println(wordUtt.SYLL1_F0_MEAN)
        println(wordUtt.SYLL0_F0_RANGE)
        println(wordUtt.SYLL1_F0_RANGE)



        println("\nPerforming word feature extraction...")
        String word = wordUtt.word.text
        String wav = wordUtt.sentenceUtterance.waveFile
        String grid = wordUtt.sentenceUtterance.gridFile

        FeatureExtractor fx = new FeatureExtractor(wav, grid, word)


        wordUtt.WORD_DUR = fx.getWordDuration()
        wordUtt.SYLL0_DUR = fx.getSyllableDuration(0)
        wordUtt.SYLL1_DUR = fx.getSyllableDuration(1)
        wordUtt.SYLL0_F0_MEAN = fx.getSyllableF0Mean(0)
        wordUtt.SYLL1_F0_MEAN = fx.getSyllableF0Mean(1)
        wordUtt.SYLL0_F0_RANGE = fx.getSyllableF0Range(0)
        wordUtt.SYLL1_F0_RANGE = fx.getSyllableF0Range(1)
        wordUtt.save()
        println("Done.\n")


        println("New features for WordUtterance: " + wordUtt.toString())
        println(wordUtt.WORD_DUR)
        println(wordUtt.SYLL0_DUR)
        println(wordUtt.SYLL1_DUR)
        println(wordUtt.SYLL0_F0_MEAN)
        println(wordUtt.SYLL1_F0_MEAN)
        println(wordUtt.SYLL0_F0_RANGE)
        println(wordUtt.SYLL1_F0_RANGE)

    }

}
