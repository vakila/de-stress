package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.FeedbackComputer

class Scorer {

    //necessary?
    String name
    String description

    Boolean useJsnooriScores

    DurationScorer durationScorer
    F0Scorer f0Scorer
    IntensityScorer intensityScorer


    Double durationWeight
    Double f0Weight
    Double intensityWeight


    static constraints = {
        name()
        description(blank:true,nullable:true)
        // must either use Jsnoori scores or have the 3 scorers
        useJsnooriScores(//validator: {val, obj ->
            // if (!val) {
            //     if (!obj.durationScorer || !obj.f0Scorer || !obj.intensityScorer) {
            //         return ['mustHaveScorers']
            //     }
            // }
            // else {
            //     if (obj.durationScorer || obj.f0Scorer || obj.intensityScorer) {
            //         return ['cannotUseScorers']
            //     }
            // }
            // }
            )
        durationScorer(blank:true,nullable:true,validator:{ val,obj ->
            if (!val && !obj.useJsnooriScores) return ['mustUseScorer']
            if (val && obj.useJsnooriscores) return ['cannotUseScorer']
            })
        f0Scorer(blank:true,nullable:true,validator:{ val,obj ->
            if (!val && !obj.useJsnooriScores) return ['mustUseScorer']
            if (val && obj.useJsnooriscores) return ['cannotUseScorer']
            })
        intensityScorer(blank:true,nullable:true,validator:{ val,obj ->
            if (!val && !obj.useJsnooriScores) return ['mustUseScorer']
            if (val && obj.useJsnooriscores) return ['cannotUseScorer']
            })

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
