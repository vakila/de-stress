import org.ifcasl.destress.Role
import org.ifcasl.destress.User
import org.ifcasl.destress.UserRole

class BootStrap {

    def init = { servletContext ->
		def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
		def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
  
		def adminUser = new User(username: 'admin', password: 'admin', email: 'admin@admin.com')
		adminUser.save(flush: true)
		
		def normalUser = new User(username: 'student', password: 'student', email: 'student@student.com')
		normalUser.save(flush: true)
  
		UserRole.create adminUser, adminRole, true
		UserRole.create normalUser, userRole, true
  
		assert User.count() == 2
		assert Role.count() == 2
		assert UserRole.count() == 2
    }
    def destroy = {
    }
}
