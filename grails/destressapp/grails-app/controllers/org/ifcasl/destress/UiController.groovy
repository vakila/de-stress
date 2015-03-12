package org.ifcasl.destress

class UiController {


    def index() {
        def id = params['id']
        def ex = Exercise.get(id)
        def cFG = WordUtterance.createCriteria()
        //def fgUtts = WordUtterance.findAllByWord(ex.word)
        def fgUtts = cFG {
            and {
                eq("word",ex.word)
                sentenceUtterance {
                    speaker {
                        eq("nativeLanguage", Language.F)
                    }
                }
            }
        }

        def ggUtts
        // def refType = ex.diagnosisMethod.referenceType
        // if (refType!=ReferenceType.ABSTRACT) {
        def nRefs = ex.diagnosisMethod.numberOfReferences
        if (nRefs > 0) {
            def cGG = WordUtterance.createCriteria()
            ggUtts = cGG {
                and {
                    eq("word",ex.word)
                    sentenceUtterance {
                        speaker {
                            eq("nativeLanguage", Language.G)
                        }
                    }
                }
            }
        }

        render(view:"exercise", model:[ex:ex,
                                       fgUtts:fgUtts,
                                       ggUtts:ggUtts,
                                       nRefs:(1..nRefs)])
    }

    def record() {
    }

    def upload() {

    }

    def download(long id) {
        SentenceUtterance utt = SentenceUtterance.get(id)
        File file = new File(utt.waveFile)
        response.setContentType('audio/wav')
        response.setContentLength (file.bytes.length)
        response.setHeader("Content-disposition", "attachment;filename=${file.getName()}")
        response.outputStream << file.newInputStream() // Performing a binary stream copy

        return false
    }

    def downloadFeedback(long id) {
        Diagnosis diag = Diagnosis.get(id)
        def path = grailsApplication.mainContext.servletContext.getRealPath("/") + "audio/feedback/" + diag.feedbackWaveFile
        File file = new File(path)
        response.setContentType('audio/wav')
        response.setContentLength (file.bytes.length)
        response.setHeader("Content-disposition", "attachment;filename=${file.getName()}")
        response.outputStream << file.newInputStream() // Performing a binary stream copy

        return false
    }

    def feedback() {
        def ex = Exercise.get(params['id'])

        //def basePath = grailsApplication.mainContext.servletContext.getRealPath("/web-app/")

        def studUtt = WordUtterance.get(params['fgUtts.id'])
        def studWav = studUtt.sentenceUtterance.sampleName + ".wav"

        def refUtts = []
        //def refWavs = []
        def nRefs = ex.diagnosisMethod.numberOfReferences
        // def refSyllSizes = [:]
        // def r0Pct
        // def r1Pct
        // def r0Size
        // def r1Size
        for (i in 1..nRefs) {
            def refUtt = WordUtterance.get(params['ggUtts.' + i])
            //def refWav = grailsApplication.parentContext.getResource("/audio/" + refUtt.sentenceUtterance.sampleName + ".wav")
            //def refWav = refUtt.sentenceUtterance.sampleName + ".wav"
            refUtts.add(refUtt)
            //refWavs.add(refWav)



        }
        //TODO validate that input from multiple refUtts selects are not the same utterance

        def diag = DiagnosisUtil.getDiagnosis(ex, studUtt, refUtts)

        // get scores & colors
        def durPct = new Float(diag.durationScore * 100f)
        def f0Pct = new Float(diag.f0Score * 100f)
        def intPct = new Float(diag.intensityScore * 100f)
        def allPct = new Float(diag.overallScore * 100f)

        def durCol = DiagnosisUtil.getColor(diag.durationScore)
        def f0Col = DiagnosisUtil.getColor(diag.f0Score)
        def intCol = DiagnosisUtil.getColor(diag.intensityScore)
        def allCol = DiagnosisUtil.getColor(diag.overallScore)

        // get feedback audio
        def fbWav
        fbWav = diag.feedbackWaveFile // might be null
        println("fbWav: " + fbWav)

        /// get syllable font sizes for studUtt
        def s0Pct = studUtt.SYLL0_DUR / studUtt.WORD_DUR
        def s1Pct = studUtt.SYLL1_DUR / studUtt.WORD_DUR
        println("studUtt: " + studUtt + "\ts0Pct: " + s0Pct + "\ts1Pct: " + s1Pct)
        def studSyllDurs = [s0Pct.round(2), s1Pct.round(2)]
        def s0Size = s0Pct * 3
        def s1Size = s1Pct * 3
        def studSyllSizes = [s0Size, s1Size]

        /// get mean-normalized F0
        println("studUtt: " + studUtt + "\tFeatures:")
        FeatureUtil.printWordUtteranceFeatures(studUtt)
        def s0F0 = (studUtt.SYLL0_F0_MEAN / studUtt.WORD_F0_MEAN).round(2)
        def s1F0 = (studUtt.SYLL1_F0_MEAN / studUtt.WORD_F0_MEAN).round(2)
        def studSyllF0s = [s0F0, s1F0]
        println("studUtt: " + studUtt + "\tstudSyllF0s: " + studSyllF0s)


        /// get syllable sizes for refUtts
        def refSyllDurs = []
        def refSyllSizes = []
        def refSyllF0s = []
        for (WordUtterance refUtt in refUtts) {
            /// get syllable font sizes
            // def r0Pct = refUtt.SYLL0_DUR / refUtt.WORD_DUR
            // def r1Pct = refUtt.SYLL1_DUR / refUtt.WORD_DUR
            // //println("refUtt: " + refUtt.toString() + "\ts0Pct: " + s0Pct + "\ts1Pct: " + s1Pct)
            // def r0Size = s0Pct * 3
            // def r1Size = s1Pct * 3

            println("refUtt: " + refUtt + "\tFeatures:")
            FeatureUtil.printWordUtteranceFeatures(refUtt)


            refSyllDurs.add([(refUtt.SYLL0_DUR/refUtt.WORD_DUR).round(2), (refUtt.SYLL1_DUR / refUtt.WORD_DUR).round(2)])

            //refSyllSizes.add([r0Size,r1Size])
            refSyllSizes.add([((refUtt.SYLL0_DUR/refUtt.WORD_DUR)*3),((refUtt.SYLL1_DUR / refUtt.WORD_DUR)*3)])
            println("refUtt: " + refUtt + "\trefSyllSizes: " + refSyllSizes[refUtts.indexOf(refUtt)])

            /// get syllable F0s
            refSyllF0s.add([(refUtt.SYLL0_F0_MEAN/refUtt.WORD_F0_MEAN).round(2), (refUtt.SYLL1_F0_MEAN/refUtt.WORD_F0_MEAN).round(2)])
            println("refUtt: " + refUtt + "\trefSyllF0s: " + refSyllF0s[refUtts.indexOf(refUtt)])
        }




        //render(view:"exercise", model:[ex:ex,fgUtts:[],ggUtts:[]])
        [ex:ex,diag:diag,
               studUtt:studUtt,
               refUtts:refUtts,
               studWav:studWav,
               fbWav:fbWav,
               durPct:durPct,durCol:durCol,
               f0Pct:f0Pct,f0Col:f0Col,
               intPct:intPct,intCol:intCol,
               allPct:allPct,allCol:allCol,
               studSyllSizes:studSyllSizes,
               refSyllSizes:refSyllSizes,
               studSyllDurs:studSyllDurs,
               refSyllDurs:refSyllDurs,
               studSyllF0s:studSyllF0s,
               refSyllF0s:refSyllF0s,
               //,refWavs:refWavs
        ]
    }




}
