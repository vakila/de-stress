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
        def fbWav = diag.feedbackWaveFile
        println fbWav


        //render(view:"exercise", model:[ex:ex,fgUtts:[],ggUtts:[]])
        [ex:ex,diag:diag,
               studUtt:studUtt,refUtts:refUtts,
               studWav:studWav,fbWav:fbWav,
               durPct:durPct,durCol:durCol,
               f0Pct:f0Pct,f0Col:f0Col,
               intPct:intPct,intCol:intCol,
               allPct:allPct,allCol:allCol]//,refWavs:refWavs]
    }




}
