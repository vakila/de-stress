package grailstest

class Test {
	String name
	String description
	static belongsTo = Lesson
	static hasMany = [lessons:Lesson]
	
    static constraints = {
		name (blank:false) 
    }
	
	String toString(){
		return name
	}
}
