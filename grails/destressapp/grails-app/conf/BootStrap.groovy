import org.ifcasl.destress.*
//import org.ifcasl.destress.Role
//import org.ifcasl.destress.User
//import org.ifcasl.destress.UserRole

import org.apache.commons.io.FileUtils

class BootStrap {

	def WAVEDIR = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/"
	def GRIDDIR = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/"
	def DATADIR = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"

	def WORDS = [
			//[sentence, text, syllables, stressIndex, wordsBefore, wordsAfter],
			["SR23", "frühling", ["Früh","ling"], 0, "In", "fliegen Pollen durch die Luft."],
			["SR23", "fliegen", ["flie","gen"], 0, "In Frühling", "Pollen durch die Luft."],
			["SR23", "pollen", ["Pol","len"], 0, "In Frühling fliegen", "durch die Luft."],
			["SR31", "mörder", ["Mör","der"], 0, "Der", "hat den Tatort wieder besucht."],
			["SR31", "tatort", ["Tat","ort"], 0, "Der Mörder hat den", "wieder besucht."]
			//["SH05", "Flagge", ["Flag","ge"], "Die", "des IOC besteht aus 5 Ringen as Symbol"
		]

	def SPEAKERS = [
			["F", "B", "A2", "546"],
			["F", "M", "B2", "540"],
			["F", "M", "C1", "545"],
			["F", "W", "A2", "522"],
			["F", "W", "B1", "536"],
			["F", "W", "C1", "530"],
			["G", "B", "A2", "007"],
			["G", "M", "B1", "001"],
			["G", "M", "C1", "034"],
			["G", "W", "A2", "018"],
			["G", "W", "B1", "026"],
			["G", "W", "C1", "013"]
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




		////// CREATE SPEAKERS
		print "\nCreating speakers... "
		for (spkrInfo in SPEAKERS) {
			Speaker spkr = new Speaker(
				speakerNumber:spkrInfo[3],
				nativeLanguage:spkrInfo[0],
				ageGender:spkrInfo[1],
				skillLevel:spkrInfo[2]
				)
			spkr.save()

		}
		assert Speaker.count() == 12
		println "Done."


		////// CREATE WORDS & UTTERANCES
		//print "\nCreating words... "
		for (wordInfo in WORDS) {
			print "Creating word " + wordInfo[1] + "..."
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
			println "Done."
			//assert w.toString().contains(wordInfo[0])
			// assert w.toString().contains(wordInfo[2][0])
			// assert w.toString().contains(wordInfo[2][1])

			// Create SentenceUtterances for each speaker/sentence
			String sentNum = wordInfo[0]
			for (spkr in Speaker.getAll()) {
				println "Speaker: " + spkr.speakerNumber
				//if sentence utterance doesn't exist, create it
				def cSent = SentenceUtterance.createCriteria()
				def sentResults = cSent {
					and {
						eq("speaker",spkr)
						eq("sentence",sentNum)
					}
				}
				println "SentenceUtterance results ("+sentResults.size()+"): " + sentResults

				// def speakerResults = SentenceUtterance.findAllBySpeaker(spkr)
				// println "speakerResults ("+speakerResults.size()+"): " + speakerResults
				//
				// def results2 = SentenceUtterance.findAllBySentence(sentNum)
				// println "Results2 (" + results2.size() + "): " + results2
				// //def sentenceResults = SentenceUtterance.
				//println "Found existing SentenceUtterance " + sentUtt

				SentenceUtterance sentUtt
				if (sentResults.size() == 0) {
					println "Creating SentenceUtterance " + sentNum + "_" + spkr.speakerNumber +"..."
					// Find wav and textgrid files
					String sentType = sentNum.substring(0,2)
					String sampName = "2" + sentNum + "_"
					sampName += spkr.nativeLanguage.value + "G" + spkr.ageGender + spkr.skillLevel
					sampName += "_" + spkr.speakerNumber
					println "sampName: " + sampName
					String waveOrigPath = WAVEDIR + spkr.nativeLanguage + ["G", sentType, sentNum, sampName+".wav"].join("/")
					String gridOrigPath = GRIDDIR + spkr.nativeLanguage + ["G", sentType, sentNum, sampName+".textgrid"].join("/")
					File waveOrigFile = new File(waveOrigPath)
					File gridOrigFile = new File(gridOrigPath)
					assert waveOrigFile.exists() && gridOrigFile.exists()
					println "Wave and Textgrid files found."

					// Copy files to web-app/
					def grailsApplication = new SentenceUtterance().domainClass.grailsApplication

			        String waveName = sampName + ".wav"
			        String waveNewPath = grailsApplication.mainContext.servletContext.getRealPath("/") + "audio/" + waveName

					String gridName = sampName + ".textgrid"
					String gridNewPath = grailsApplication.mainContext.servletContext.getRealPath("/") + "grids/" + gridName

			        println "################# TESTING ###################"
			        println "sampName: " + sampName
					println "waveOrigPath: " + waveOrigPath
					println "gridOrigPath: " + gridOrigPath
					println "waveNewPath: " + waveNewPath
					println "gridNewPath: " + gridNewPath

			        File waveNewFile = new File(waveNewPath)
			        if (!waveNewFile.exists()) FileUtils.copyFile(waveOrigFile, waveNewFile)
			        assert waveOrigFile.exists() && waveNewFile.exists()
			        println "WAVE FILE SAVED"
					File gridNewFile = new File(gridNewPath)
			        if (!gridNewFile.exists()) FileUtils.copyFile(gridOrigFile, gridNewFile)
			        assert gridOrigFile.exists() && gridNewFile.exists()
			        println "GRID FILE SAVED"

					// Save SentenceUtterance
					sentUtt = new SentenceUtterance(
						sentence:sentNum,
						speaker:spkr,
						sampleName:sampName,
						waveFile:waveNewPath,
						gridFile:gridNewPath,
						)
					sentUtt.save()
					println "Done."
				}
				else if (sentResults.size() == 1){
					sentUtt = sentResults[0]
				}
				else {
					println "Found more than one SentenceUtterance - I'm confused"
					assert false
				}

				// if word utterance doesn't exist, create it
				def cWord = WordUtterance.createCriteria()
				def wordResults = cWord {
					and{
						eq("word",w)
						eq("sentenceUtterance",sentUtt)
					}
				}
				println "WordUtterance results ("+wordResults.size()+"): " + wordResults
				if (wordResults.size()==0) {
					print "Creating word utterance " + sentUtt.toString() + "_" + w.text + "..."
					def wordUtt = new WordUtterance(
						sentenceUtterance:sentUtt,
						word:w
						)
					wordUtt.save()
					print "Done."
				}

			}
		}
		assert Word.count() == 5
		assert SentenceUtterance.count() == 24
		assert WordUtterance.count() == 60
		//println "Done."

		////// CREATE SCORER & DIAGNOSIS METHOD
		def scorer = new Scorer(
			name:"SimpleScorer",
			description:"Uses FeedbackComputer scores, equal weights",
			durationWeight:0.34d,
			f0Weight:0.33d,
			intensityWeight:0.33d)
		scorer.save()
		assert Scorer.count() == 1

		def dm1 = new DiagnosisMethod(
			name:"SimpleDM",
			description:"Manual choice, SimpleScorer",
			scorer:scorer,
			//referenceType:"SINGLE",
			numberOfReferences:1,
			selectionType:"MANUAL",
			)
		dm1.save()
		//println(dm1)

		def dm2 = new DiagnosisMethod(
			name:"MultiDM",
			description:"Multi Manual choice, SimpleScorer",
			scorer:scorer,
			//referenceType:"MULTI",
			selectionType:"MANUAL",
			numberOfReferences:2,
			)
		dm2.save()
		//println(dm2)
		assert DiagnosisMethod.count() == 2


		//// CREATE EXERCISES
		def ex1 = new Exercise(
			name:"SimpleExercise",
			description:"Simplest config possible",
			word:Word.get(1),
			diagnosisMethod:dm1)
		ex1.save()

		def ex2 = new Exercise(
			name:"MultiExercise",
			description:"Simple multiref",
			word:Word.get(2),
			diagnosisMethod:dm2)
		ex2.save()


		assert Exercise.count() == 2




    }
    def destroy = {
    }
}
