package org.ifcasl.destress

class UiController {

    def index() {

    }

    def exercise() {
        def id = params['id']
        def ex = Exercise.get(id)
        [ex:ex]
    }
}
