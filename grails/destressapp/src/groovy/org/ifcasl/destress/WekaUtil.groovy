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
    //public static String FILTERFILE = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/filters/wordLevelExtracted.filter"

    public static FilteredClassifier CLASSIFIER = getClassifier()


    // public static String classify(WordUtterance wordUtt) {
    //     Instance inst = getInstance(wordUtt)
    //
    //     double pred = CLASSIFIER.classifyInstance(inst)
    //     String label = DATASET.classAttribute().value((int) pred)
    //
    //     return label
    // }

    public static Instance getInstance(WordUtterance wordUtt) {
        def nAtts = DATASET.numAttributes()
        Instance inst = new Instance(nAtts)

        println inst.toString()

        for (att in DATASET.enumerateAttributes()) {
            println att.name()
        }

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
