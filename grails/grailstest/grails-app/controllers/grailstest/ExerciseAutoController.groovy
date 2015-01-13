package grailstest



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ExerciseAutoController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Exercise.list(params), model:[exerciseInstanceCount: Exercise.count()]
    }

    def show(Exercise exerciseInstance) {
        respond exerciseInstance
    }

    def create() {
        respond new Exercise(params)
    }

    @Transactional
    def save(Exercise exerciseInstance) {
        if (exerciseInstance == null) {
            notFound()
            return
        }

        if (exerciseInstance.hasErrors()) {
            respond exerciseInstance.errors, view:'create'
            return
        }

        exerciseInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'exercise.label', default: 'Exercise'), exerciseInstance.id])
                redirect exerciseInstance
            }
            '*' { respond exerciseInstance, [status: CREATED] }
        }
    }

    def edit(Exercise exerciseInstance) {
        respond exerciseInstance
    }

    @Transactional
    def update(Exercise exerciseInstance) {
        if (exerciseInstance == null) {
            notFound()
            return
        }

        if (exerciseInstance.hasErrors()) {
            respond exerciseInstance.errors, view:'edit'
            return
        }

        exerciseInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Exercise.label', default: 'Exercise'), exerciseInstance.id])
                redirect exerciseInstance
            }
            '*'{ respond exerciseInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Exercise exerciseInstance) {

        if (exerciseInstance == null) {
            notFound()
            return
        }

        exerciseInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Exercise.label', default: 'Exercise'), exerciseInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'exercise.label', default: 'Exercise'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
