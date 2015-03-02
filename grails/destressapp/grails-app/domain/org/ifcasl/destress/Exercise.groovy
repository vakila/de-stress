package org.ifcasl.destress

class Exercise {
	String name
	String description

	Word word

	DiagnosisMethod diagnosisMethod

	static belongsTo = Lesson
	static hasMany = [lessons:Lesson]//, widgets:MyWidget]


    static constraints = {
		name (blank: false)
		description (widget:'textarea')
		word()
		lessons()
    }

	String toString(){
		return name
	}
}
