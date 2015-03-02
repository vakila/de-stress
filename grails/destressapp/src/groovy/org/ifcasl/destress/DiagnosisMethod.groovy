package org.ifcasl.destress

abstract class DiagnosisMethod {


    // Necessary?
    String name
    String description


    Scorer scorer


    static constraints = {
        name()
        description(blank:true,nullable:true)
        scorer()
    }

}
