package org.ifcasl.destress



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SentenceUtteranceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SentenceUtterance.list(params), model:[sentenceUtteranceInstanceCount: SentenceUtterance.count()]
    }

    def show(SentenceUtterance sentenceUtteranceInstance) {
        respond sentenceUtteranceInstance
    }

    def create() {
        respond new SentenceUtterance(params)
    }

    @Transactional
    def save(SentenceUtterance sentenceUtteranceInstance) {
        if (sentenceUtteranceInstance == null) {
            notFound()
            return
        }

        if (sentenceUtteranceInstance.hasErrors()) {
            respond sentenceUtteranceInstance.errors, view:'create'
            return
        }

        sentenceUtteranceInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'sentenceUtterance.label', default: 'SentenceUtterance'), sentenceUtteranceInstance.id])
                redirect sentenceUtteranceInstance
            }
            '*' { respond sentenceUtteranceInstance, [status: CREATED] }
        }
    }

    def edit(SentenceUtterance sentenceUtteranceInstance) {
        respond sentenceUtteranceInstance
    }

    @Transactional
    def update(SentenceUtterance sentenceUtteranceInstance) {
        if (sentenceUtteranceInstance == null) {
            notFound()
            return
        }

        if (sentenceUtteranceInstance.hasErrors()) {
            respond sentenceUtteranceInstance.errors, view:'edit'
            return
        }

        sentenceUtteranceInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'SentenceUtterance.label', default: 'SentenceUtterance'), sentenceUtteranceInstance.id])
                redirect sentenceUtteranceInstance
            }
            '*'{ respond sentenceUtteranceInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(SentenceUtterance sentenceUtteranceInstance) {

        if (sentenceUtteranceInstance == null) {
            notFound()
            return
        }

        sentenceUtteranceInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'SentenceUtterance.label', default: 'SentenceUtterance'), sentenceUtteranceInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'sentenceUtterance.label', default: 'SentenceUtterance'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
