package org.ifcasl.destress

class DurationScorer {

    //properties for each of the possible duration features store their weights

    Double relativeSyllableDuration
    Double relativeVowelDuration
    //etc.

    //constraints: weights must sum to 1 (see Scorer)
}
