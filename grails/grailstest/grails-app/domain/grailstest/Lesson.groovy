package grailstest

class Lesson {
	
	String name
	String description
	Date dateCreated
	static hasMany = [exercises:Exercise, tests:Test]
	
    static constraints = {
		name (blank: false)
		description (widget: 'textarea') //, blank: false)
    }
	
	String toString(){
		return name
	}
//	static mapping = {
//		autoTimestamp true
//	}
}
