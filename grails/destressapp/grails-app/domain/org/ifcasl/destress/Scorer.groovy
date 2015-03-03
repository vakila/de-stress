package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.FeedbackComputer

class Scorer {

    //necessary?
    String name
    String description

    //FeedbackComputer feedbackComputer

    DurationScorer durationScorer
    F0Scorer f0Scorer
    IntensityScorer intensityScorer


    Double durationWeight
    Double f0Weight
    Double intensityWeight


    static constraints = {
        name()
        description(blank:true,nullable:true)
        // //must either have a feedbackComputer or the 3 scorers
        // feedbackComputer(blank:true,nullable:true, validator: {val, obj ->
        //     if (val == null) {
        //         if (!obj.durationScorer || !obj.f0Scorer || !obj.intensityScorer) {
        //             return ['mustHaveScorers']
        //         }
        //     }
        //     })
        durationScorer(blank:true,nullable:true)
        f0Scorer(blank:true,nullable:true)
        intensityScorer(blank:true,nullable:true)

        //weights must be between 0 and 1 (inclusive)
        //weights must sum to 1 (constrain on intensityWeight?)
        durationWeight(range:0..1)
        f0Weight(range:0..1)
        intensityWeight(range:0..1, validator: { val, obj ->
                if ((val + obj.durationWeight + obj.f0Weight) != 1) {
                    return ['mustSumTo1', val + obj.durationWeight + obj.f0Weight]
                }})

    }

    String toString() {
        return name
    }

}
