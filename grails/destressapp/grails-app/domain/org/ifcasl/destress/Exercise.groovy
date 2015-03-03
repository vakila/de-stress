package org.ifcasl.destress

//import org.ifcasl.destress.DiagnosisMethod

class Exercise {
	String name
	String description

	Word word

	DiagnosisMethod diagnosisMethod
	//static hasOne = [diagnosisMethod:DiagnosisMethod]

	static belongsTo = Lesson
	static hasMany = [lessons:Lesson]//, widgets:MyWidget]


    static constraints = {
		name (blank: false)
		description (widget:'textarea')
		word()
		diagnosisMethod()//blank:true,nullable:true)
		lessons()
    }

	String toString(){
		return name
	}
}
