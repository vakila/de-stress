package org.ifcasl.destress

class IntensityScorer {

    //properties for each of the possible intensity features store their weights

    Double relativeSyllableMean
    Double relativeVowelMean
    //etc.

    //constraints: weights must sum to 1 (see Scorer)
}
