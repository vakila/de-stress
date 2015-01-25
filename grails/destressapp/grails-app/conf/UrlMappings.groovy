class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
		//"/"(controller: 'Index', action: 'index')
		"/admin/"(controller: 'Admin', action: 'index')
		"/grails/"(view:"/grails")
		
        "500"(view:'/error')
		
		//"/loggedin"(controller: 'Login', action: 'login')
		//"/loggedout"(controller: 'Login', action: 'logout')
		
		"/testing/"(controller: 'JsnooriTest', action: 'featex')
	}
}
