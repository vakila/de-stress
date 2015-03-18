package org.ifcasl.destress

class Diagnosis {

    Exercise exercise

    //DiagnosisMethod diagnosisMethod // or make this property of exercise?

    WordUtterance studentUtterance
    List referenceUtterances

    //TODO transition from single feedbackWaveFile to list feedbackWaves
    String feedbackWaveFile     // temporary
    List feedbackWaves

    static hasMany = [referenceUtterances:WordUtterance, feedbackWaves:String]



    Float durationScore
    Float f0Score
    Float intensityScore
    Float overallScore

    String label

    //self-assessment data?

    Date dateCreated

    static constraints = {
        exercise()
        studentUtterance()
        referenceUtterances(blank:true,nullable:true)
        label(blank:true,nullable:true)
        durationScore(blank:true,nullable:true)
        f0Score(blank:true,nullable:true)
        intensityScore(blank:true,nullable:true)
        overallScore(blank:true,nullable:true)
        feedbackWaveFile(blank:true,nullable:true)
        feedbackWaves(blank:true,nullable:true)
        dateCreated()

    }

    String toString() {
        //return [exercise.toString(),studentUtterance.toString()].join("-")
        return ["Ex"+exercise.getId(),studentUtterance].join("-")
        //return "test-diagnosis"
    }
}
