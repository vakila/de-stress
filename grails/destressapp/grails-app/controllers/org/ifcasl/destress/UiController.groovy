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
        def cGG = WordUtterance.createCriteria()
        def ggUtts = cGG {
            and {
                eq("word",ex.word)
                sentenceUtterance {
                    speaker {
                        eq("nativeLanguage", Language.G)
                    }
                }
            }
        }
        // println "WordUtterances for this word:"
        // for (utt in fgUtts) {
        //     println utt
        // }
        render(view:"exercise", model:[ex:ex,fgUtts:fgUtts,ggUtts:ggUtts])
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

        //def grailsApplication = new SentenceUtterance().domainClass.grailsApplication
        def basePath = grailsApplication.mainContext.servletContext.getRealPath("/web-app/")

        //def studUtt = params['studentUtterance']
        def studUtt = WordUtterance.get(params['fgUtts.id'])
        //def studWav = studUtt.sentenceUtterance.waveFile
        //def studWav = "/audio/" + studUtt.sentenceUtterance.sampleName + ".wav"
        def studWav = studUtt.sentenceUtterance.sampleName + ".wav"

        def refUtt = WordUtterance.get(params['ggUtts.id'])
        //def refWav = grailsApplication.parentContext.getResource("/audio/" + refUtt.sentenceUtterance.sampleName + ".wav")
        def refWav = refUtt.sentenceUtterance.sampleName + ".wav"

        //render(view:"exercise", model:[ex:ex,fgUtts:[],ggUtts:[]])
        [ex:ex,studUtt:studUtt,refUtt:refUtt,studWav:studWav,refWav:refWav]
    }
}
