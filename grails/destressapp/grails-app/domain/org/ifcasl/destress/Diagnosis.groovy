package org.ifcasl.destress

class Diagnosis {

    Exercise exercise

    WordUtterance studentWordUtterance
    //WordUtterance referenceWordUtterance

    DiagnosisMethod diagnosisMethod

    Float durationScore
    Float f0Score
    Float intensityScore
    Float overallScore

    //self-assessment score?

    Date dateCreated

    static constraints = {
        exercise()
        studentWordUtterance()
        //referenceWordUtterance(blank:true,nullable:true)
    }
}
