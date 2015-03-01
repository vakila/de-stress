package org.ifcasl.destress

class Word {

	//static belongsTo = [sentence:Sentence]
	//Integer position

	String sentence 	//e.g. SH05

	String text			//e.g. ringen


	String wordsBefore 	//the first part of the sentence, before this word. e.g. "Die Flagge des IOC besteht aus 5"
	String wordsAfter 	//the last part of the sentence, after this word. e.g. "als Symbol der 5 Kontinente."

	//static hasOne = [syllable1:Syllable, syllable2:Syllable] //the two syllables of this word - see Syllable
	List syllables
	static hasMany = [syllables:String]

	Integer stressIndex

	Date dateCreated

    static constraints = {

		text(unique: 'sentence')
		sentence()
		wordsBefore()
		syllables(minSize: 2)
		wordsAfter()
		stressIndex(min:0)
		//stressIndex max: syllables.size()-1
    }

	String toString(){
//		String syll1Display = ""
//		if (syllable1.stressed) {
//			syll1Display += "'"
//		}
//		syll1Display += syllable1.text
//
//		String syll2Display = ""
//		if (syllable2.stressed) {
//			syll2Display += "'"
//		}
//		syll2Display += syllable2.text

		// def toJoin = []
		// for (int i=0; i<syllables.size(); i++) {
		// 	def toAdd = ""
		// 	if (i==stressIndex) {
		// 		toAdd += "'"
		// 	}
		// 	toAdd += syllables[i]
		// 	toJoin.add(toAdd)
		// }
		// def display = toJoin.join("|")
		//
		// return sentence + "-" + display

		//return sentence + "-" + text
		return text
	}
}
