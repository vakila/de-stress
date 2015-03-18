package org.ifcasl.destress

import weka.core.Attribute
import weka.core.Instance
import weka.core.Instances

import weka.filters.Filter
//import weka.filters.unsupervised.attribute.Remove
//import weka.filters.unsupervised.attribute.NumericToNominal

import weka.classifiers.meta.FilteredClassifier
//import weka.classifiers.trees.SimpleCart

import weka.core.SerializationHelper



class WekaUtil {

    public static String DATAFILE = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FGGG-random.arff"
    public static Instances DATASET = DataProcessor.loadArff(DATAFILE)
    public static String CLASSIFIERFILE = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/classifiers/SimpleCart-WordLevelExtracted.model"
    public static FilteredClassifier CLASSIFIER = (FilteredClassifier) SerializationHelper.read(CLASSIFIERFILE)


    public static String classify(WordUtterance wordUtt) {
        DATASET.setClassIndex(DATASET.numAttributes() - 1)

        Instance inst = getInstance(wordUtt)

        double pred = CLASSIFIER.classifyInstance(inst)
        String label = DATASET.classAttribute().value((int) pred)

        return label
    }

    public static Instance getInstance(WordUtterance wordUtt) {
        def nAtts = DATASET.numAttributes()
        Instance inst = new Instance(nAtts)
        inst.setDataset(DATASET)


        FeatureExtractor featex =  new FeatureExtractor(wordUtt.sentenceUtterance.waveFile,
                                                        wordUtt.sentenceUtterance.gridFile,
                                                        wordUtt.word.text)

        //println("Equal headers? " + inst.equalHeaders(DATASET.instance(57)))
        println("Instance 57:")
        println(DATASET.instance(57).toString())


        // for (att in DATASET.enumerateAttributes()) {
        //     println att.name()
        // }


        //// WORD & SPEAKER
        inst.setValue(DATASET.attribute("SENTENCE_TYPE"), wordUtt.word.sentence.substring(0,2))
        inst.setValue(DATASET.attribute("SENTENCE"), wordUtt.word.sentence)
        inst.setValue(DATASET.attribute("WORD"), wordUtt.word.text)
        inst.setValue(DATASET.attribute("SPEAKER"), wordUtt.sentenceUtterance.speaker.speakerNumber)
        inst.setValue(DATASET.attribute("SPEAKER_L1"), wordUtt.sentenceUtterance.speaker.nativeLanguage.toString())
        inst.setValue(DATASET.attribute("SPEAKER_GENDER"), wordUtt.sentenceUtterance.speaker.ageGender.toString())
        inst.setValue(DATASET.attribute("SPEAKER_LEVEL"), wordUtt.sentenceUtterance.speaker.skillLevel.toString())


        //// DURATION
        inst.setValue(DATASET.attribute("WORD_DUR"), featex.getWordDuration())
        inst.setValue(DATASET.attribute("SYLL0_DUR"), featex.getSyllableDuration(0))
        inst.setValue(DATASET.attribute("SYLL1_DUR"), featex.getSyllableDuration(1))
        inst.setValue(DATASET.attribute("V0_DUR"), featex.getRelSyllDuration())
        inst.setValue(DATASET.attribute("V1_DUR"), featex.getVowelDurationInSyllable(0))
        inst.setValue(DATASET.attribute("REL_SYLL_DUR"), featex.getVowelDurationInSyllable(1))
        inst.setValue(DATASET.attribute("REL_V_DUR"), featex.getRelVowelDuration())


        //// F0
        // Word
        inst.setValue(DATASET.attribute("WORD_F0_MEAN"),  featex.getWordF0Mean())
        inst.setValue(DATASET.attribute("WORD_F0_MAX"), featex.getWordF0Max())
        inst.setValue(DATASET.attribute("WORD_F0_MIN"), featex.getWordF0Min())
        inst.setValue(DATASET.attribute("WORD_F0_RANGE"), featex.getWordF0Range())
        // Syllables
        inst.setValue(DATASET.attribute("SYLL0_F0_MEAN"), featex.getSyllableF0Mean(0))
        inst.setValue(DATASET.attribute("SYLL0_F0_MAX"), featex.getSyllableF0Max(0))
        inst.setValue(DATASET.attribute("SYLL0_F0_MIN"), featex.getSyllableF0Min(0))
        inst.setValue(DATASET.attribute("SYLL0_F0_RANGE"), featex.getSyllableF0Range(0))
        inst.setValue(DATASET.attribute("SYLL1_F0_MEAN"), featex.getSyllableF0Mean(1))
        inst.setValue(DATASET.attribute("SYLL1_F0_MAX"), featex.getSyllableF0Max(1))
        inst.setValue(DATASET.attribute("SYLL1_F0_MIN"), featex.getSyllableF0Min(1))
        inst.setValue(DATASET.attribute("SYLL1_F0_RANGE"), featex.getSyllableF0Range(1))
        // // Vowels
        // inst.setValue(V0_F0_MEAN, featex.getVowelF0Mean(0))
        // inst.setValue(V0_F0_MAX, featex.getVowelF0Max(0))
        // inst.setValue(V0_F0_MIN, featex.getVowelF0Min(0))
        // inst.setValue(V0_F0_RANGE, featex.getVowelF0Range(0))
        // inst.setValue(V1_F0_MEAN, featex.getVowelF0Mean(1))
        // inst.setValue(V1_F0_MAX, featex.getVowelF0Max(1))
        // inst.setValue(V1_F0_MIN, featex.getVowelF0Min(1))
        // inst.setValue(V1_F0_RANGE, featex.getVowelF0Range(1))
        // // inst.setValue(DATASET.attribute("V0_F0_MEAN"), )
        // // inst.setValue(DATASET.attribute("V0_F0_MAX"), )
        // // inst.setValue(DATASET.attribute("V0_F0_MIN"), )
        // // inst.setValue(DATASET.attribute("V0_F0_RANGE"), )
        // // inst.setValue(DATASET.attribute("V1_F0_MEAN"), )
        // // inst.setValue(DATASET.attribute("V1_F0_MAX"), )
        // // inst.setValue(DATASET.attribute("V1_F0_MIN"), )
        // // inst.setValue(DATASET.attribute("V1_F0_RANGE"), )
        // // Relative
        // inst.setValue(REL_SYLL_F0_MEAN, featex.getRelSyllF0Mean())
        // inst.setValue(REL_SYLL_F0_MAX, featex.getRelSyllF0Max())
        // inst.setValue(REL_SYLL_F0_MIN, featex.getRelSyllF0Min())
        // inst.setValue(REL_SYLL_F0_RANGE, featex.getRelSyllF0Range())
        // inst.setValue(REL_VOWEL_F0_MEAN, featex.getRelVowelF0Mean())
        // inst.setValue(REL_VOWEL_F0_MAX, featex.getRelVowelF0Max())
        // inst.setValue(REL_VOWEL_F0_MIN, featex.getRelVowelF0Min())
        // inst.setValue(REL_VOWEL_F0_RANGE, featex.getRelVowelF0Range())
        // inst.setValue(F0_MAX_INDEX, featex.getMaxF0Index())
        // inst.setValue(F0_MIN_INDEX, featex.getMinF0Index())
        // inst.setValue(F0_MAXRANGE_INDEX, featex.getMaxRangeF0Index())
        // // inst.setValue(DATASET.attribute("REL_SYLL_F0_MEAN"), )
        // // inst.setValue(DATASET.attribute("REL_SYLL_F0_MAX"), )
        // // inst.setValue(DATASET.attribute("REL_SYLL_F0_MIN"), )
        // // inst.setValue(DATASET.attribute("REL_SYLL_F0_RANGE"), )
        // // inst.setValue(DATASET.attribute("REL_VOWEL_F0_MEAN"), )
        // // inst.setValue(DATASET.attribute("REL_VOWEL_F0_MAX"), )
        // // inst.setValue(DATASET.attribute("REL_VOWEL_F0_MIN"), )
        // // inst.setValue(DATASET.attribute("REL_VOWEL_F0_RANGE"), )
        // // inst.setValue(DATASET.attribute("F0_MAX_INDEX"), )
        // // inst.setValue(DATASET.attribute("F0_MIN_INDEX"), )
        // // inst.setValue(DATASET.attribute("F0_MAXRANGE_INDEX"), )
        //
        // //// ENERGY
        // //Word
        // inst.setValue(WORD_ENERGY_MEAN, featex.getWordEnergyMean())
        // inst.setValue(WORD_ENERGY_MAX, featex.getWordEnergyMax())
        // // inst.setValue(DATASET.attribute("WORD_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("WORD_ENERGY_MAX"), )
        // //Syllables
        // inst.setValue(SYLL0_ENERGY_MEAN, featex.getSyllableEnergyMean(0))
        // inst.setValue(SYLL0_ENERGY_MAX, featex.getSyllableEnergyMax(0))
        // inst.setValue(SYLL1_ENERGY_MEAN, featex.getSyllableEnergyMean(1))
        // inst.setValue(SYLL1_ENERGY_MAX, featex.getSyllableEnergyMax(1))
        // // inst.setValue(DATASET.attribute("SYLL0_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("SYLL0_ENERGY_MAX"), )
        // // inst.setValue(DATASET.attribute("SYLL1_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("SYLL1_ENERGY_MAX"), )
        // //Vowels
        // inst.setValue(V0_ENERGY_MEAN, featex.getVowelEnergyMean(0))
        // inst.setValue(V0_ENERGY_MAX, featex.getVowelEnergyMax(0))
        // inst.setValue(V1_ENERGY_MEAN, featex.getVowelEnergyMean(1))
        // inst.setValue(V1_ENERGY_MAX, featex.getVowelEnergyMax(1))
        // // inst.setValue(DATASET.attribute("V0_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("V0_ENERGY_MAX"), )
        // // inst.setValue(DATASET.attribute("V1_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("V1_ENERGY_MAX"), )
        // //Relative
        // inst.setValue(REL_SYLL_ENERGY_MEAN, featex.getRelSyllEnergyMean())
        // inst.setValue(REL_SYLL_ENERGY_MAX, featex.getRelSyllEnergyMax())
        // inst.setValue(REL_VOWEL_ENERGY_MEAN, featex.getRelVowelEnergyMean())
        // inst.setValue(REL_VOWEL_ENERGY_MAX, featex.getRelVowelEnergyMax())
        // inst.setValue(ENERGY_MAX_INDEX, featex.getMaxEnergyIndex())
        // // inst.setValue(DATASET.attribute("REL_SYLL_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("REL_SYLL_ENERGY_MAX"), )
        // // inst.setValue(DATASET.attribute("REL_VOWEL_ENERGY_MEAN"), )
        // // inst.setValue(DATASET.attribute("REL_VOWEL_ENERGY_MAX"), )
        // // inst.setValue(DATASET.attribute("ENERGY_MAX_INDEX"), )

        //// CLASS
        //inst.setValue(DATASET.attribute("STRESS"), )

        println("New instance:")
        println inst.toString()

        return inst

    }

    ///// MOVED TO classifierScript.groovy
    // public static FilteredClassifier getClassifier() {
    //     DATASET.setClassIndex(DATASET.numAttributes() - 1)
    //
    //     Remove thisFilter = (Remove) weka.core.SerializationHelper.read(FILTERFILE)
    //     thisFilter.setInputFormat(DATASET)
    //     thisFilter.setOutputFormat(DATASET)
    //
    //     def thisClassifier = new SimpleCart()
    //
    //     FilteredClassifier fc = new FilteredClassifier()
    //     fc.setFilter(thisFilter)
    //     fc.setClassifier(thisClassifier)
    //
    //     fc.buildClassifier(DATASET)
    //
    //     println("\nBuilt classifier:\n" + fc.toString())
    //
    //     return fc
    // }


}
