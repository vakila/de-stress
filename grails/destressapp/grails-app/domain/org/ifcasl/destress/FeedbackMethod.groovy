package org.ifcasl.destress

class FeedbackMethod {

    String name
    String description

    ScorerType requiresScorerType

    Boolean showSkillBars
    Boolean playFeedbackSignal

    Boolean styleText

    static constraints = {
        name()
        description()
        requiresScorerType(blank:true,nullable:true)
        showSkillBars(blank:true,nullable:true)
        playFeedbackSignal(blank:true,nullable:true)
        styleText(blank:true,nullable:true)
    }

    String toString() {
        return name
    }
}
