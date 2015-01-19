package grailstest



import destress.Exercise;
import destress.ExerciseAutoController;
import grails.test.mixin.*
import spock.lang.*

@TestFor(ExerciseAutoController)
@Mock(Exercise)
class ExerciseAutoControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
		params["name"] = 'Test Exercise'
		params["text"] = 'This is an example sentence.'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.exerciseInstanceList
            model.exerciseInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.exerciseInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def exercise = new Exercise()
            exercise.validate()
            controller.save(exercise)

        then:"The create view is rendered again with the correct model"
            model.exerciseInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            exercise = new Exercise(params)

            controller.save(exercise)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/exercise/show/1'
            controller.flash.message != null
            Exercise.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def exercise = new Exercise(params)
            controller.show(exercise)

        then:"A model is populated containing the domain instance"
            model.exerciseInstance == exercise
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def exercise = new Exercise(params)
            controller.edit(exercise)

        then:"A model is populated containing the domain instance"
            model.exerciseInstance == exercise
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/exercise/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def exercise = new Exercise()
            exercise.validate()
            controller.update(exercise)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.exerciseInstance == exercise

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            exercise = new Exercise(params).save(flush: true)
            controller.update(exercise)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/exercise/show/$exercise.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/exercise/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def exercise = new Exercise(params).save(flush: true)

        then:"It exists"
            Exercise.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(exercise)

        then:"The instance is deleted"
            Exercise.count() == 0
            response.redirectedUrl == '/exercise/index'
            flash.message != null
    }
}
