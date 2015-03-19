package org.ifcasl.destress

class FeedbackMethod {

    String name
    String description

    ScorerType requiresScorerType

    Boolean showSkillBars
    Boolean playFeedbackSignal

    Boolean displayShapes
    Boolean styleText

    Boolean displayMessages

    Boolean selfAssessment

    static constraints = {
        name()
        description(blank:true,nullable:true)
        requiresScorerType(blank:true,nullable:true)

        showSkillBars(blank:true,nullable:true, validator: {val,obj ->
            if (val==true && obj.requiresScorerType!=ScorerType.JSNOORI) {
                return ['cannotShowBars', obj.requiresScorerType]
            }
            })
        playFeedbackSignal(blank:true,nullable:true, validator: {val,obj ->
            if (val==true && obj.requiresScorerType!=ScorerType.JSNOORI) {
                return ['cannotPlayFeedback', obj.requiresScorerType]
            }
            })

        displayShapes(blank:true,nullable:true)
        styleText(blank:true,nullable:true)
        displayMessages(blank:true,nullable:true)
        selfAssessment(blank:true,nullable:true)
    }

    String toString() {
        return name
    }
}
