class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

		"/ui/"(controller:'ui', action:'list')
		"/ui/exercise/$id"(controller:'ui', action:'prompt')
		"/ui/exercise/$id/$action?"(controller:'ui')

        //"/"(view:"/index")
		"/"(view:"/grails")
		//"/"(controller: 'Index', action: 'index')
		"/admin/"(controller: 'Admin', action: 'index')
		"/grails/"(view:"/grails")

        "500"(view:'/error')

		//"/loggedin"(controller: 'Login', action: 'login')
		//"/loggedout"(controller: 'Login', action: 'logout')

		// BackendTesting
		"/testing/featex"(controller: 'BackendTesting', action: 'featex')
		"/testing/csv"(controller: 'BackendTesting', action: 'csv')
		"/testing/arff"(controller: 'BackendTesting', action: 'arff')
		"/testing/addattrs"(controller: 'BackendTesting', action: 'addattrs')
		"/testing/extractFG"(controller: 'BackendTesting', action: 'extractFG')
		"/testing/phon"(controller: 'BackendTesting', action: 'phon')
		"/testing/map"(controller: 'BackendTesting', action: 'map')
	}
}
