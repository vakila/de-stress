package org.ifcasl.destress

class Speaker {

    String speakerNumber
    String nativeLanguage

    AgeGender ageGender
    SkillLevel skillLevel

    // speech characteristics? e.g. F0 range

    static constraints = {
        speakerNumber(unique: true)
    }
}
