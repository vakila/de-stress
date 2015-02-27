package org.ifcasl.destress

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Word)
class WordSpec extends Specification {



    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
		setup:
		Word w = new Word(sentence:"SR23",
			text:"frühling",
			//syllables:["Früh","ling"],
			stressIndex:0,
			wordsBefore:"In",
			wordsAfter:"fliegen Pollen durch die Luft.")
		w.addToSyllables("Früh")
		w.addToSyllables("ling")
		w.save()

		println w.text


		expect:
		w.text.equals("frühling")
    }
}
