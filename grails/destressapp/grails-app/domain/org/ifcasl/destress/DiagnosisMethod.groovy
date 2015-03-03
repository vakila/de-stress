package org.ifcasl.destress

public class DiagnosisMethod {

    String name
    String description

    Scorer scorer

    ReferenceType referenceType //SINGLE, MULTI, or ABSTRACT

    // for SINGLE or MULTI reference types
    SelectionType selectionType //MANUAL, AUTO, or FIXED

        // for AUTO selection type
        //TODO

        // for FIXED selection type
        //TODO

        // for MULTI reference type
        Integer numberOfReferences
            //TODO
            // combination type?


    // for ABSTRACT reference type
    //TODO
        // Weka classifier
        // Feature extractor?
        //Scorer's duration/f0/intensity scorers must be non-null



    static constraints = {
        name()
        description(blank:true,nullable:true)
        scorer()
        referenceType()
        selectionType(blank:true,nullable:true,validator:{ val, obj ->
            if (val==null && obj.referenceType!=ReferenceType.ABSTRACT) {
                return ['needSelectionType', obj.referenceType]
            }
        })
        numberOfReferences(min:2,blank:true,nullable:true,validator:{ val, obj ->
            if (val==null && obj.referenceType==ReferenceType.MULTI) {
                return ['needNumberOfReferences']
            }
        })

    }

    String toString() {
        //TODO more informative string?
        return name
    }

}
