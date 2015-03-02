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

    def diagnosis() {
        def ex = Exercise.get(params['id'])
        def studUtt = params['studentUtterance']
        def refUtt = params['referenceUtterance']

        //render(view:"exercise", model:[ex:ex,fgUtts:[],ggUtts:[]])
        [ex:ex,studUtt:studUtt,refUtt:refUtt]
    }
}
