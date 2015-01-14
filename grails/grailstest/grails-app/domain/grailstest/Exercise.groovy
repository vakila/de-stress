package grailstest

class Exercise {
	String name
	String text
	static belongsTo = Lesson
	static hasMany = [lessons:Lesson]
    static constraints = {
		name (blank: false)
		text (blank: false)
    }
}
