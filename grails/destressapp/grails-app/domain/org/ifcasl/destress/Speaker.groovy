package org.ifcasl.destress

class Speaker {

    String speakerNumber
    Language nativeLanguage
    AgeGender ageGender
    SkillLevel skillLevel

    static hasMany = [sentenceUtterances: SentenceUtterance]

    // speech characteristics? e.g. F0 range
    //Float speakingRate      //syllables/second?
    Float f0Mean
    Float f0Range
    Float intensityMean
    //Float intensityMax

    static constraints = {
        speakerNumber(unique: true)
        f0Mean(blank:true,nullable:true)
        f0Range(blank:true,nullable:true)
        intensityMean(blank:true,nullable:true)
    }

    String toString(){
		return [nativeLanguage, ageGender, skillLevel, speakerNumber].join("_")
	}
}
