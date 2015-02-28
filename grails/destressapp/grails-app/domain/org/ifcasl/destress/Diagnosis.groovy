package org.ifcasl.destress

class Diagnosis {

    Word word

    Utterance studentUtterance

    Float durationScore
    Float durationWeight

    Float f0Score
    Float f0Weight

    Float intensityScore
    Float intensityWeight

    Float overallScore

    //? static hasMany = [durationFeatures:DurationFeature] //make enums
    

}
