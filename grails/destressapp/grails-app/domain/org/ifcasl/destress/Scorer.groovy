package org.ifcasl.destress

class Scorer {

    //necessary?
    String name
    String description


    DurationScorer durationScorer
    Float durationWeight

    F0Scorer f0Scorer
    Float f0Weight

    IntensityScorer intensityScorer
    Float intensityWeight

    //constraint: weights must sum to 1

}
