package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.FeedbackComputer

class Scorer {

    //necessary?
    String name
    String description

    ScorerType type
    //Boolean useJsnooriScores

    DurationScorer durationScorer
    F0Scorer f0Scorer
    IntensityScorer intensityScorer


    Double durationWeight
    Double f0Weight
    Double intensityWeight


    static constraints = {
        name()
        description(blank:true,nullable:true)
        type()
        // useJsnooriScores(//validator: {val, obj ->
        //     // if (!val) {
        //     //     if (!obj.durationScorer || !obj.f0Scorer || !obj.intensityScorer) {
        //     //         return ['mustHaveScorers']
        //     //     }
        //     // }
        //     // else {
        //     //     if (obj.durationScorer || obj.f0Scorer || obj.intensityScorer) {
        //     //         return ['cannotUseScorers']
        //     //     }
        //     // }
        //     // }
        //     )
        durationScorer(blank:true,nullable:true,validator:{ val,obj ->
            if (!val && obj.type==ScorerType.CUSTOM) return ['mustUseScorer']
            if (val && obj.type!=ScorerType.CUSTOM) return ['cannotUseScorer']
            })
        f0Scorer(blank:true,nullable:true,validator:{ val,obj ->
            if (!val && obj.type==ScorerType.CUSTOM) return ['mustUseScorer']
            if (val && obj.type!=ScorerType.CUSTOM) return ['cannotUseScorer']
            })
        intensityScorer(blank:true,nullable:true,validator:{ val,obj ->
            if (!val && obj.type==ScorerType.CUSTOM) return ['mustUseScorer']
            if (val && obj.type!=ScorerType.CUSTOM) return ['cannotUseScorer']
            })

        //weights must be between 0 and 1 (inclusive)
        //if not using Weka, weights must sum to 1 (constrain on intensityWeight?)
        durationWeight(range:0..1,blank:true,nullable:true, validator: { val, obj ->
                if (obj.type==ScorerType.WEKA) {
                    if (val!=null) return ['mustBeNull', obj.type]
                }
                else {
                    if (val==null) return ['cannotBeNull', obj.type]
                }})
        f0Weight(range:0..1,blank:true,nullable:true, validator: { val, obj ->
                if (obj.type==ScorerType.WEKA) {
                    if (val!=null) return ['mustBeNull', obj.type]
                }
                else {
                    if (val==null) return ['cannotBeNull', obj.type]
                }})
        intensityWeight(range:0..1,blank:true,nullable:true, validator: { val, obj ->
                if (obj.type==ScorerType.WEKA) {
                    if (val!=null) return ['mustBeNull', obj.type]
                }
                else {
                    if (val==null) return ['cannotBeNull', obj.type]
                    if (val&& obj.durationWeight && obj.f0Weight) {
                        if ((val + obj.durationWeight + obj.f0Weight) != 1) {
                        return ['mustSumTo1', val + obj.durationWeight + obj.f0Weight]
                        }
                    }
                }
                // if ((val + obj.durationWeight + obj.f0Weight) != 1) {
                //     return ['mustSumTo1', val + obj.durationWeight + obj.f0Weight]
                // }
                })

    }

    String toString() {
        return name
    }

}
