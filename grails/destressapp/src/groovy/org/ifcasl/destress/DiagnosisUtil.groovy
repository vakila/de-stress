package org.ifcasl.destress

class DiagnosisUtil {


    static Diagnosis getClassificationDiagnosis(Exercise ex, WordUtterance studUtt) {
        String label = WekaUtil.classify(studUtt)

        // Create diagnosis object
        def diag = new Diagnosis(exercise:ex,
                                 studentUtterance:studUtt,
                                 label:label,
                                 )
        diag.save()
        return diag
    }

    static List findNBestRefUtts(WordUtterance learnerUtt, int n) {
        def bestSpeakers = findNBestRefSpeakers(learnerUtt.sentenceUtterance.speaker, n)
        def bestUtts = []
        for (Speaker refSpkr in bestSpeakers) {

            def crit = WordUtterance.createCriteria()
            //def fgUtts = WordUtterance.findAllByWord(ex.word)
            def refUtt = crit {
                and {
                    eq("word",learnerUtt.word)
                    sentenceUtterance {
                        eq("speaker",refSpkr)
                    }
                }
            }

            if (refUtt.size() != 1) {
                println("DiagnosisUtil Line 22: FOUND STRANGE NUMBER OF REFS: " + refUtt.size() + "refUtt: " + refUtt)
            }

            bestUtts.add(refUtt[0])
        }

        println("Found bestUtts: " + bestUtts)
        return bestUtts
    }

    static List findNBestRefSpeakers(Speaker learner, int n) {
        def crit = Speaker.createCriteria()
        def ggSpeakers = crit {
            eq("nativeLanguage", Language.G)
        }

        //def bestDiff = learner.f0Mean + learner.f0Range //initialize as worst possible diff
        //def bestSpeaker

        def speakerDiffs = [:]

        for (Speaker thisGG in ggSpeakers) {
            def thisMeanDiff = thisGG.f0Mean - learner.f0Mean
            def thisRangeDiff = thisGG.f0Range - learner.f0Range
            def thisDiff = thisMeanDiff.abs() + thisRangeDiff.abs()//TODO take absolute values and add
            // if (thisDiff < bestDiff) {
            //     bestDiff = thisDiff
            //     bestSpeaker = thisGG
            // }
            speakerDiffs[thisGG] = thisDiff
        }

        def rankedRefs = speakerDiffs.sort { it.value }

        def nBestRefs = rankedRefs.keySet().toList()[0..n-1]

        return nBestRefs

    }

    static Diagnosis getComparisonDiagnosis(Exercise ex, WordUtterance studUtt, List refUtts) {
        def scorer = ex.diagnosisMethod.scorer
        def fbc

        // Get 3 scores
        def durScore
        def f0Score
        def intScore

        def fbWaves = []

        def scorerType = scorer.type
        println ("scorerType: " + scorerType)
        //switch (scorerType) {

            if (scorerType == ScorerType.JSNOORI) {
            //case "JSNOORI":
                if (refUtts.size()==0) {
                    println("ERROR: EMPTY refUtts!!!")
                    return
                }
                else if (refUtts.size()==1) {
                    def refUtt = refUtts.get(0)

                    // get scores from FeedbackComputer
                    fbc = JsnooriUtil.getFeedbackComputer(ex, studUtt, refUtt)
                    durScore = fbc.timeFeedback.getScore()
                    f0Score = fbc.pitchFeedback.getPitchScore()
                    intScore = fbc.energyFeedback.getEnergyScore()
                }
                else { // not exactly 1 reference
                    def durTotal = 0f
                    def f0Total = 0f
                    def intTotal = 0f
                    for (refUtt in refUtts) {
                        // Get FeedbackComputer
                        println("Getting diag for refUtt: " + refUtt.toString())
                        fbc = JsnooriUtil.getFeedbackComputer(ex, studUtt, refUtt)

                        // Tally scores
                        durTotal += fbc.timeFeedback.getScore()
                        f0Total += fbc.pitchFeedback.getPitchScore()
                        intTotal += fbc.energyFeedback.getEnergyScore()

                        // Get resynthesized audio
                        def waveName = "FB-" + studUtt.word.toString() + "-" + studUtt.sentenceUtterance.toString() + "-" + refUtt.sentenceUtterance.toString() + ".wav"
                        println ("waveName: " + waveName)
                        def grailsApplication = new Diagnosis().domainClass.grailsApplication
                        def feedbackPath = grailsApplication.mainContext.servletContext.getRealPath("/") + "audio/feedback/" + waveName

                        if (fbc.feedbackSignal != null) {
                            //diag.feedbackWaveFile = waveName
                            fbc.feedbackSignal.saveWave(new File(feedbackPath))
                            fbWaves.add(waveName)
                        }
                        else {
                            println("ERROR feedbackSignal is null!")
                        }

                    }
                    def n = new Float(refUtts.size())
                    durScore = durTotal/n
                    f0Score = f0Total/n
                    intScore = intTotal/n
                }
             //end case JSNOORI
            }

            //// This shouldn't get reached, because this method isn't called if ScorerType == WEKA
            // //case ScorerType.WEKA:
            // else if (scorerType == ScorerType.WEKA) {
            //     //convert studUtt to Weka instance
            //     //TEMP
            //     WekaUtil.getInstance(studUtt)
            // //end case WEKA
            // }

            //default:
            else { //not using Jsnoori scores
                println "Unhandled scorer type: " + scorerType
                //throw new Exception("I can only handle Jsnoori scores at the moment, and the scorer " + scorer.toString() + " doesn't use them")
            }

        //} //end switch


        // Get overall score
        def durWt = scorer.durationWeight
        def f0Wt = scorer.f0Weight
        def intWt = scorer.intensityWeight
        def allScore = new Float(durScore*durWt + f0Score*f0Wt + intScore*intWt)

        // Create diagnosis object
        def diag = new Diagnosis(exercise:ex,
                                 studentUtterance:studUtt,
                                 referenceUtterances:refUtts,
                                 durationScore:durScore,
                                 f0Score:f0Score,
                                 intensityScore:intScore,
                                 overallScore:allScore,
                                 feedbackWaves:fbWaves,
                                 )

        ///// MOVED TO LINE 119ff.
        // if (refUtts.size() == 1) {
        //     // save feedbackSignal to file
        //     def waveName = diag.toString() + ".wav"
        //     println ("waveName: " + waveName)
        //     def grailsApplication = new Diagnosis().domainClass.grailsApplication
        //     def feedbackPath = grailsApplication.mainContext.servletContext.getRealPath("/") + "audio/feedback/" + waveName
        //
        //     if (fbc.feedbackSignal != null) {
        //         //TODO append to diag.feedbackWaves
        //         diag.feedbackWaveFile = waveName
        //         fbc.feedbackSignal.saveWave(new File(feedbackPath))
        //     }
        //     else {
        //         println("ERROR feedbackSignal is null!")
        //     }
        // }

        // save & return
        diag.save(flush:true)
        return diag
    }

    static String getColor(Float score) {
        def col
        if (score < 0.25) {
            col = "red"
        } else if (score < 0.7) {
            col = "yellow"
        } else {
            col = "green"
        }
        return col
    }

    static String getColor(String label) {
        def col
        if (label == "correct") {
            col = "green"
        } else if (label == "none") {
            col = "red"//"yellow"
        } else {
            col = "red"
        }
        return col
    }

    public static String getClassificationMessage(Diagnosis diag) {
        def message = "Sorry, I wasn't able to classify your utterance."
        if (diag.label == "correct") {
            message = "You stressed the correct syllable. Great job!"
        }
        else if (diag.label == "incorrect") {
            message = "It sounds like you stressed the incorrect syllable. Remember that the stress in "
            message += diag.studentUtterance.word.text + " should be on the first syllable."
        }
        else if (diag.label == "none") {
            message = "It sounds like you pronounced both syllables with equal stress. Next time, try to use duration, pitch, and loudness to make the first syllable sound more important than the second syllable."
        }
        //TODO include bad_* label cases?
        return message
    }

    public static String getDurationMessage(Diagnosis diag) {
        def durScore = diag.durationScore
        //println("durScore: " + durScore)
        def message = "Sorry, I wasn't able to analyze duration in your utterance."
        if (durScore <= 0.1f) {
            message = "I think you pronounced an incorrect number of syllables for this word."
        }
        else if (durScore <= 0.3f) {
            message = "I think you pronounced an incorrect number of phones in at least one of the word's syllables."
        }
        else if (durScore <= 0.5f) {
            message = "The wrong syllable has the longest vowel."
        }
        else if (durScore <= 0.8f) {
            message = "The correct syllable's vowel is longest, good job! But it should be even longer compared to the unstressed syllable."
        }
        else if (durScore <= 1.0f) {
            message = "No problems with duration, great job!"
        }
        return message
    }

    public static String getF0Message(Diagnosis diag) {
        def f0Score = diag.f0Score
        def message = "Sorry, I wasn't able to analyze pitch in your utterance."
        if (f0Score <= 0.1) {
            message = "The wrong syllable has the highest pitch."
        }
        else if (f0Score <= 0.8) {
            message = "The correct syllable has the highest pitch, good job! But it should be even higher compared to the unstressed syllable."
        }
        else if (f0Score <= 1.0) {
            message = "Your pitch was pitch-perfect, great job!"
        }
        return message
    }

    public static String getIntensityMessage(Diagnosis diag) {
        def intScore = diag.intensityScore
        println("intScore: " + intScore)
        def message = "Sorry, I wasn't able to analyze the loudness of your utterance."
        if (intScore <= 0.1) {
            message = "The wrong syllable is loudest."
        }
        else if (intScore <= 0.8) {
            message = "The correct syllable is loudest, good job! But it should be even louder compared to the unstressed syllable."
        }
        else if (intScore <= 1.0) {
            message = "No problems with loudness, great job!"
        }
        return message
    }

}
