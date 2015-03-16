package org.ifcasl.destress

class FeatureUtil {

    public static Speaker findBestReference(Speaker learner) {
        def crit = Speaker.createCriteria()
        def ggSpeakers = crit {
            eq("nativeLanguage", Language.G)
        }

        def bestDiff = learner.f0Mean + learner.f0Range //initialize as worst possible diff
        def bestSpeaker

        // for (Speaker thisGG in ggSpeakers) {
        //     thisMeanDiff = thisGG.f0Mean - learner.f0Mean
        //     thisRangeDiff = thisGG.f0Range - learner.f0Range
        //     thisDiff = 0//TODO take absolute values and add
        //     if (thisDiff < bestDiff) {
        //         bestDiff = thisDiff
        //         bestSpeaker = thisGG
        //     }
        // }

        return bestSpeaker

    }

    public static computeSpeakerFeatures(Speaker speaker) {
        println("Current features for Speaker: " + speaker.toString())
        printSpeakerFeatures(speaker)

        println("\nComputing features for speaker: " + speaker.toString())


        /// Sum up features from all sentence utterances for that speaker
        def sumSpeakingRate = 0f
        def sumF0Mean = 0f
        def sumF0Range = 0f
        def sumIntensityMean = 0f

        for (SentenceUtterance sentUtt in speaker.sentenceUtterances) {
            sumSpeakingRate += sentUtt.speakingRate
            sumF0Mean += sentUtt.f0Mean
            sumF0Range += sentUtt.f0Range
            sumIntensityMean += sentUtt.intensityMean
        }

        /// Set speaker features as averages of sentence utterance features
        def nSents = (Float) speaker.sentenceUtterances.size()
        speaker.speakingRate = sumSpeakingRate/nSents
        speaker.f0Mean = sumF0Mean/nSents
        speaker.f0Range = sumF0Range/nSents
        speaker.intensityMean = sumIntensityMean/nSents
        speaker.save()


        println("Done.")

        println("New features for Speaker: " + speaker.toString())
        printSpeakerFeatures(speaker)
    }

    public static extractSentenceUtteranceFeatures(SentenceUtterance sentUtt) {
        println("Current features for SentenceUtterance: " + sentUtt.toString())
        printSentenceUtteranceFeatures(sentUtt)

        println("\nPerforming sentence feature extraction...")
        String wav = sentUtt.waveFile
        String grid = sentUtt.gridFile

        FeatureExtractor fx = new FeatureExtractor(wav, grid)


        // duration
        sentUtt.totalDuration = fx.getTotalDuration()
        sentUtt.speakingDuration = fx.getSpeakingDuration()
        def syllCount = fx.pitchAnalysis.getSyllable_real_number()
        sentUtt.speakingRate = syllCount / sentUtt.speakingDuration

        // f0
        sentUtt.f0Mean = fx.getOverallF0Mean()
        sentUtt.f0Range = fx.getOverallF0Range()

        // intensity
        sentUtt.intensityMean = fx.getOverallEnergyMean()
        sentUtt.intensityMax = fx.getOverallEnergyMax()

        sentUtt.save()
        println("Done.\n")

        println("New features for SentenceUtterance: " + sentUtt.toString())
        printSentenceUtteranceFeatures(sentUtt)
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
        wordUtt.WORD_ENERGY_MEAN = fx.getWordEnergyMean()
        wordUtt.SYLL0_ENERGY_MEAN = fx.getSyllableEnergyMean(0)
        wordUtt.SYLL1_ENERGY_MEAN = fx.getSyllableEnergyMean(1)
        wordUtt.save()
        println("Done.\n")


        println("New features for WordUtterance: " + wordUtt.toString())
        printWordUtteranceFeatures(wordUtt)

    }

    public static printWordUtteranceFeatures(WordUtterance wordUtt) {
        println("WORD_DUR:          " + wordUtt.WORD_DUR)
        println("SYLL0_DUR:         " + wordUtt.SYLL0_DUR)
        println("SYLL1_DUR:         " + wordUtt.SYLL1_DUR)
        println("WORD_F0_MEAN:      " + wordUtt.WORD_F0_MEAN)
        println("SYLL0_F0_MEAN:     " + wordUtt.SYLL0_F0_MEAN)
        println("SYLL1_F0_MEAN:     " + wordUtt.SYLL1_F0_MEAN)
        println("SYLL0_F0_RANGE:    " + wordUtt.SYLL0_F0_RANGE)
        println("SYLL1_F0_RANGE:    " + wordUtt.SYLL1_F0_RANGE)
        println("WORD_ENERGY_MEAN:  " + wordUtt.WORD_ENERGY_MEAN)
        println("SYLL0_ENERGY_MEAN: " + wordUtt.SYLL0_ENERGY_MEAN)
        println("SYLL1_ENERGY_MEAN: " + wordUtt.SYLL1_ENERGY_MEAN)
    }

    public static printSentenceUtteranceFeatures(SentenceUtterance sentUtt) {
        // duration
        println("totalDuration    " + sentUtt.totalDuration)
        println("speakingDuration " + sentUtt.speakingDuration)
        println("speakingRate     " + sentUtt.speakingRate)

        // f0
        println("f0Mean           " + sentUtt.f0Mean)
        println("f0Range          " + sentUtt.f0Range)

        // intensity
        println("intensityMean    " + sentUtt.intensityMean)
        println("intensityMax     " + sentUtt.intensityMax)
    }

    public static printSpeakerFeatures(Speaker speaker) {
        println("speakingRate   " + speaker.speakingRate)
        println("f0Mean         " + speaker.f0Mean)
        println("f0Range        " + speaker.f0Range)
        println("intensityMean  " + speaker.intensityMean)
    }

}
