package org.ifcasl.destress

class Speaker {

    String speakerNumber
    String nativeLanguage

    AgeGender ageGender
    SkillLevel skillLevel

    static constraints = {
        speakerNumber(unique: true)
    }
}
