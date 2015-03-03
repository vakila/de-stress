package org.ifcasl.destress

enum ReferenceType {
    SINGLE('SINGLE'),
    MULTI('MULTI'),
    ABSTRACT('ABSTRACT')

    // SINGLE   > Student utterance compared to 1 reference utterance
    // MULTI    > Student utterance compared to multiple references (many 1-to-1s)
    // ABSTRACT > Student utterance classified as Weka instance

    String value

    ReferenceType(value) {
        this.value = value
    }
}
