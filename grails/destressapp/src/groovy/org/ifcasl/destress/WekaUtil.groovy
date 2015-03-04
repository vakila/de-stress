package org.ifcasl.destress

import weka.core.Attribute
import weka.core.Instance
//import weka.core.DenseInstance;
import weka.core.Instances

class WekaUtil {

    public static String DATAFILE = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/GG-FG_extracted.arff"
    public static Instances DATASET = DataProcessor.loadArff(DATAFILE)

    public static Instance getInstance(WordUtterance wordUtt) {
        def nAtts = DATASET.numAttributes()
        Instance inst = new Instance(nAtts)

        println inst.toString()

        for (att in DATASET.enumerateAttributes()) {
            println att.name()
        }

        return inst

    }

}
