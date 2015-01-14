package grailstest

class Lesson {
	String name
	Date dateCreated
	static hasMany = [exercises:Exercise]
    static constraints = {
		name (blank: false)
    }
//	static mapping = {
//		autoTimestamp true
//	}
}
