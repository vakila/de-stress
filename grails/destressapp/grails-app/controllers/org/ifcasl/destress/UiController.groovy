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

    def diagnosis() {
        def ex = Exercise.get(params['id'])

        def basePath = grailsApplication.mainContext.servletContext.getRealPath("/web-app/")

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




        //render(view:"exercise", model:[ex:ex,fgUtts:[],ggUtts:[]])
        [ex:ex,studUtt:studUtt,refUtts:refUtts,studWav:studWav]//,refWavs:refWavs]
    }


}
