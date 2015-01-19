package destress

class AdminController {

    def index() { 
		def modelList = ['Lesson', 'Exercise', 'Test']	
		[modelList:modelList]
	}
}
