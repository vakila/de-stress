import org.ifcasl.destress.*
//import org.ifcasl.destress.Role
//import org.ifcasl.destress.User
//import org.ifcasl.destress.UserRole

class BootStrap {

	def WORDS = [
			//[sentence, text, syllables, stressIndex, wordsBefore, wordsAfter],
			["SR23", "frühling", ["Früh","ling"], 0, "In", "fliegen Pollen durch die Luft."],
			["SR23", "fliegen", ["flie","gen"], 0, "In Frühling", "Pollen durch die Luft."],
			["SR23", "pollen", ["Pol","len"], 0, "In Frühling fliegen", "durch die Luft."],
			["SR31", "mörder", ["Mör","der"], 0, "Der", "hat den Tatort wieder besucht."],
			["SR31", "tatort", ["Tat","ort"], 0, "Der Mörder hat den", "wieder besucht."]
			//["SH05", "Flagge", ["Flag","ge"], "Die", "des IOC besteht aus 5 Ringen as Symbol"
		]

    def init = { servletContext ->

		////// CREATE USERS
		def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
		def userRole = new Role(authority: 'ROLE_USER').save(flush: true)

		def adminUser = new User(username: 'admin', password: 'admin', email: 'admin@admin.com')
		adminUser.save(flush: true)

		def normalUser = new User(username: 'student', password: 'student', email: 'student@student.com')
		normalUser.save(flush: true)

		UserRole.create adminUser, adminRole, true
		UserRole.create normalUser, userRole, true

		assert User.count() == 2
		assert Role.count() == 2
		assert UserRole.count() == 2


		////// CREATE WORDS & SYLLABLES
		for (wordInfo in WORDS) {
			println "\nCreating word..."
			Word w = new Word(
				sentence:wordInfo[0],
				text:wordInfo[1],
				//syllables:wordInfo[2],
				stressIndex:wordInfo[3],
				wordsBefore:wordInfo[4],
				wordsAfter:wordInfo[5]
			)
			w.addToSyllables(wordInfo[2][0])
			w.addToSyllables(wordInfo[2][1])
			w.save()
			assert w.toString().contains(wordInfo[0])
			// assert w.toString().contains(wordInfo[2][0])
			// assert w.toString().contains(wordInfo[2][1])
		}

		assert Word.count() == 5



    }
    def destroy = {
    }
}
