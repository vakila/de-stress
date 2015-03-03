package org.ifcasl.destress

class Diagnosis {

    Exercise exercise

    //DiagnosisMethod diagnosisMethod // or make this property of exercise?

    WordUtterance studentUtterance
    List referenceUtterances
    static hasMany = [referenceUtterances:WordUtterance]

    // temporary
    String feedbackWaveFile


    Float durationScore
    Float f0Score
    Float intensityScore
    Float overallScore

    //self-assessment score?

    Date dateCreated

    static constraints = {
        exercise()
        studentUtterance()
        referenceUtterances(blank:true,nullable:true)
        durationScore()
        f0Score()
        intensityScore()
        overallScore()
        feedbackWaveFile(blank:true,nullable:true)
        dateCreated()

    }

    String toString() {
        //return [exercise.toString(),studentUtterance.toString()].join("-")
        return ["Ex"+exercise.getId(),studentUtterance].join("-")
        //return "test-diagnosis"
    }
}
