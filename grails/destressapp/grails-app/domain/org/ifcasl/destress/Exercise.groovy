package org.ifcasl.destress

class Exercise {
	String name
	String text
	static belongsTo = Lesson
	static hasMany = [lessons:Lesson]//, widgets:MyWidget]
	
    static constraints = {
		name (blank: false)
		text (blank: false)
    }
	
	String toString(){
		return name
	}
}
