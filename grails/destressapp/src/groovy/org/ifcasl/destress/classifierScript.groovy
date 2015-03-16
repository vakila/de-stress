package org.ifcasl.destress

import weka.core.Instances
import weka.filters.unsupervised.attribute.Remove
import weka.classifiers.trees.SimpleCart

import weka.classifiers.meta.FilteredClassifier
import weka.core.SerializationHelper





/////// FUNCTIONS
def FilteredClassifier getClassifier(Instances data, String filterFile) {
    data.setClassIndex(data.numAttributes() - 1)

    Remove thisFilter = (Remove) weka.core.SerializationHelper.read(filterFile)
    thisFilter.setInputFormat(data)
    thisFilter.setOutputFormat(data)

    def thisClassifier = new SimpleCart()

    FilteredClassifier fc = new FilteredClassifier()
    fc.setFilter(thisFilter)
    fc.setClassifier(thisClassifier)

    fc.buildClassifier(data)

    println("\nBuilt classifier:\n" + fc.toString())

    return fc
}

/////// SET VARIABLES
def filterFile = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/filters/wordLevelExtracted.filter"

def classifiersDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/classifiers/"
def saveFile = classifiersDir + "SimpleCart-WordLevelExtracted.model"



/////// SCRIPT
println("\nBuilding classifier...")
FilteredClassifier classifier = getClassifier(WekaUtil.DATASET, filterFile)
println("Done.\n")

println("\nSaving classifier to file: " + saveFile)
SerializationHelper.write(saveFile, classifier)
println("Done.\n")
