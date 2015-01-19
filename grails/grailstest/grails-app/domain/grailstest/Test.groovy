package grailstest

class Test {
	String name
	String description
	String testType
	static belongsTo = Lesson
	static hasMany = [lessons:Lesson]
	
    static constraints = {
		name (blank:false) 
		description(widget:'textarea')
		testType(blank:false, inList:['Pre-test', 'Post-test'])
		
    }
	
	String toString(){
		return name
	}
}
