package org.ifcasl.destress

public class DiagnosisMethod {

    String name
    String description

    Scorer scorer

    //ReferenceType referenceType //SINGLE, MULTI, or ABSTRACT
    Integer numberOfReferences

    // for >0 references (SINGLE or MULTI reference types)
    SelectionType selectionType //MANUAL, AUTO, or FIXED

    //Boolean matchGender
    //Boolean matchAge
    //Boolean matchAgeGender

        // for AUTO selection type
        //TODO

        // for FIXED selection type
        //TODO

        // for >2 references (MULTI reference type)
            //TODO
            // combination type?


    // for 0 references (ABSTRACT reference type)
    //TODO
        // Weka classifier
        // Feature extractor?
        //Scorer's duration/f0/intensity scorers must be non-null



    static constraints = {
        name()
        description(blank:true,nullable:true)
        // if number of references is 0, scorer cannot use Jsnoori scores
        scorer(//validator:{ val,obj ->
            // if (obj.numberOfReferences==0) {
            //     if (val.type!=ScorerType.WEKA) {
            //         return ['cannotUseJsnooriScores']
            //     }
            // }
            // }
        )
        numberOfReferences(min:0, validator: {val,obj ->
            if (val==0 && obj.scorer.type!=ScorerType.WEKA) {
                return ['mustUseRefs', obj.scorer.type]
            }
            else if (val!=0 && obj.scorer.type==ScorerType.WEKA) {
                return ['cannotUseRefs', obj.scorer.type]
            }
            })
        selectionType(blank:true,nullable:true,validator:{ val,obj ->
            if (val==null && obj.numberOfReferences > 0) {
                return ['needSelectionType', obj.numberOfReferences]
            }
            })

        //matchGender(blank:true,nullable:true)
        //matchAge(blank:true,nullable:true)
        //matchAgeGender(blank:true,nullable:true)

    }

    String toString() {
        // String stringy = name + "-" + referenceType
        // if (referenceType != ReferenceType.ABSTRACT) {
        //     stringy += "-" + selectionType
        //     if (referenceType == ReferenceType.MULTI) {
        //         stringy += "-" + numberOfReferences + "refs"
        //     }
        // }
        String stringy = name + "-" + numberOfReferences + "refs"
        if (numberOfReferences > 0) stringy += "-" + selectionType
        return stringy
        //return name
    }

}
