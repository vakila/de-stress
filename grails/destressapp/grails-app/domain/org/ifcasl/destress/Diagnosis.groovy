package org.ifcasl.destress

class Diagnosis {

    Exercise exercise

    DiagnosisMethod diagnosisMethod

    WordUtterance studentUtterance
    List referenceUtterances
    static hasMany = [referenceUtterances:WordUtterance]


    Float durationScore
    Float f0Score
    Float intensityScore
    Float overallScore

    //self-assessment score?

    Date dateCreated

    static constraints = {
        //exercise()
        //studentUtterance()
        //referenceWordUtterance(blank:true,nullable:true)
    }
}
