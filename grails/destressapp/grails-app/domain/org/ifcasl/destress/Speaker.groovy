package org.ifcasl.destress

class Speaker {

    String speakerNumber
    Language nativeLanguage
    AgeGender ageGender
    SkillLevel skillLevel

    // speech characteristics? e.g. F0 range

    static constraints = {
        speakerNumber(unique: true)
    }

    String toString(){
		return [nativeLanguage, ageGender, skillLevel, speakerNumber].join("_")
	}
}
