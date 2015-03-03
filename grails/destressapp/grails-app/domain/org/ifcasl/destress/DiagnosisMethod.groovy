package org.ifcasl.destress

public class DiagnosisMethod {

    String name
    String description

    Scorer scorer

    //ReferenceType referenceType //SINGLE, MULTI, or ABSTRACT
    Integer numberOfReferences

    // for >0 references (SINGLE or MULTI reference types)
    SelectionType selectionType //MANUAL, AUTO, or FIXED

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
        scorer()
        numberOfReferences(min:0)
        selectionType(blank:true,nullable:true,validator:{ val,obj ->
            if (val==null && obj.numberOfReferences > 0) {
                return ['needSelectionType', obj.numberOfReferences]
            }
            })

        // old constraints
        //referenceType()
        // selectionType(blank:true,nullable:true,validator:{ val, obj ->
        //     if (val==null && obj.referenceType!=ReferenceType.ABSTRACT) {
        //         return ['needSelectionType', obj.referenceType]
        //     }
        // })
        // numberOfReferences(min:2,blank:true,nullable:true,validator:{ val, obj ->
        //     if (val==null && obj.referenceType==ReferenceType.MULTI) {
        //         return ['needNumberOfReferences']
        //     }
        // })

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
