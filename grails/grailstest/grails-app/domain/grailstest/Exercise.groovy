package grailstest

class Exercise {
	String name
	String text
    static constraints = {
		name (blank: false)
		text (blank: false)
    }
}
