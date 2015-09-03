import org.ifcasl.destress.*
//import org.ifcasl.destress.Role
//import org.ifcasl.destress.User
//import org.ifcasl.destress.UserRole

import org.apache.commons.io.FileUtils

class BootStrap {

	def grailsApplication = new SentenceUtterance().domainClass.grailsApplication

	def WAVEDIR = grailsApplication.mainContext.servletContext.getRealPath("/") + "/audio/" //"/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/"
	def GRIDDIR = grailsApplication.mainContext.servletContext.getRealPath("/") + "/grids/" //"/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/"
	def DATADIR = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"

	def WORDS = [
			//[sentence, text, syllables, stressIndex, wordsBefore, wordsAfter],
			["SR23", "frühling", ["Früh","ling"], 0, "Im", "fliegen Pollen durch die Luft."],
			["SR23", "fliegen", ["flie","gen"], 0, "Im Frühling", "Pollen durch die Luft."],
			["SR23", "pollen", ["Pol","len"], 0, "Im Frühling fliegen", "durch die Luft."],
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
		//DEBUG print "\nCreating speakers... "
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
		//DEBUG println "Done."


		////// CREATE WORDS & UTTERANCES
		//print "\nCreating words... "
		for (wordInfo in WORDS) {
			//DEBUG print "Creating word " + wordInfo[1] + "..."
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
			//DEBUG println "Done."
			//assert w.toString().contains(wordInfo[0])
			// assert w.toString().contains(wordInfo[2][0])
			// assert w.toString().contains(wordInfo[2][1])

			// Create SentenceUtterances for each speaker/sentence
			String sentNum = wordInfo[0]
			for (spkr in Speaker.getAll()) {
				//DEBUG println "Speaker: " + spkr.speakerNumber
				//if sentence utterance doesn't exist, create it
				def cSent = SentenceUtterance.createCriteria()
				def sentResults = cSent {
					and {
						eq("speaker",spkr)
						eq("sentence",sentNum)
					}
				}
				//DEBUG println "SentenceUtterance results ("+sentResults.size()+"): " + sentResults

				// def speakerResults = SentenceUtterance.findAllBySpeaker(spkr)
				// println "speakerResults ("+speakerResults.size()+"): " + speakerResults
				//
				// def results2 = SentenceUtterance.findAllBySentence(sentNum)
				// println "Results2 (" + results2.size() + "): " + results2
				// //def sentenceResults = SentenceUtterance.
				//println "Found existing SentenceUtterance " + sentUtt

				SentenceUtterance sentUtt
				if (sentResults.size() == 0) {
					//DEBUG println "Creating SentenceUtterance " + sentNum + "_" + spkr.speakerNumber +"..."
					// Find wav and textgrid files
					String sentType = sentNum.substring(0,2)
					String sampName = "2" + sentNum + "_"
					sampName += spkr.nativeLanguage.value + "G" + spkr.ageGender + spkr.skillLevel
					sampName += "_" + spkr.speakerNumber
					//DEBUG println "sampName: " + sampName
					String waveOrigPath = WAVEDIR + spkr.nativeLanguage + ["G", sentType, sentNum, sampName+".wav"].join("/")
					String gridOrigPath = GRIDDIR + spkr.nativeLanguage + ["G", sentType, sentNum, sampName+".textgrid"].join("/")
					File waveOrigFile = new File(waveOrigPath)
					File gridOrigFile = new File(gridOrigPath)
					//assert waveOrigFile.exists() && gridOrigFile.exists()
					//println "Wave and Textgrid files found."

					// Copy files to web-app/
					//def grailsApplication = new SentenceUtterance().domainClass.grailsApplication

			        String waveName = sampName + ".wav"
			        String waveNewPath = grailsApplication.mainContext.servletContext.getRealPath("/") + "/audio/" + waveName

					String gridName = sampName + ".textgrid"
					String gridNewPath = grailsApplication.mainContext.servletContext.getRealPath("/") + "/grids/" + gridName

			        //DEBUG println "################# TESTING ###################"
			        //DEBUG println "sampName: " + sampName
					//DEBUG println "waveOrigPath: " + waveOrigPath
					//DEBUG println "gridOrigPath: " + gridOrigPath
					//DEBUG println "waveNewPath: " + waveNewPath
					//DEBUG println "gridNewPath: " + gridNewPath

			        File waveNewFile = new File(waveNewPath)
			        if (!waveNewFile.exists()) {
						assert waveOrigFile.exists()
						FileUtils.copyFile(waveOrigFile, waveNewFile)
					}
			        //assert waveOrigFile.exists() && waveNewFile.exists()
					assert waveNewFile.exists()
			        //DEBUG println "WAVE FILE SAVED"
					File gridNewFile = new File(gridNewPath)
			        if (!gridNewFile.exists()) {
						assert gridOrigFile.exists()
						FileUtils.copyFile(gridOrigFile, gridNewFile)
					}
			        //assert gridOrigFile.exists() && gridNewFile.exists()
					assert gridNewFile.exists()
			        //DEBUG println "GRID FILE SAVED"

					// Save SentenceUtterance
					sentUtt = new SentenceUtterance(
						sentence:sentNum,
						//speaker:spkr,
						sampleName:sampName,
						waveFile:waveNewPath,
						gridFile:gridNewPath,
						)
					spkr.addToSentenceUtterances(sentUtt)
					spkr.save()
					//sentUtt.save()
					//DEBUG println "Done."

					assert spkr.sentenceUtterances != null
					//DEBUG println("SPEAKER " + spkr.toString() + " HAS " + spkr.sentenceUtterances.size() + " SENTENCES")

					// Extract sentence features
					FeatureUtil.extractSentenceUtteranceFeatures(sentUtt)

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
				//DEBUG println "WordUtterance results ("+wordResults.size()+"): " + wordResults
				if (wordResults.size()==0) {
					//DEBUG print "Creating word utterance " + sentUtt.toString() + "_" + w.text + "..."
					def wordUtt = new WordUtterance(
						sentenceUtterance:sentUtt,
						word:w
						)
					//wordUtt.save()
					w.addToUtterances(wordUtt)
					w.save()
					//DEBUG print "Done."

					// Extract wordUtterance Features
					FeatureUtil.extractWordUtteranceFeatures(wordUtt)
				}

			}

			FeatureUtil.computeWordFeatures(w)
		}
		assert Word.count() == 5
		assert SentenceUtterance.count() == 24
		assert WordUtterance.count() == 60
		//println "Done."



		////// COMPUTE SPEAKER FEATURES
		for (Speaker speaker in Speaker.list()) {
			FeatureUtil.computeSpeakerFeatures(speaker)
		}



		////// CREATE SCORERS
		def scorer = new Scorer(
			name:"SimpleScorer",
			description:"Uses JSnoori scores, equal weights",
			//useJsnooriScores:true,
			type:"JSNOORI",
			durationWeight:0.34d,
			f0Weight:0.33d,
			intensityWeight:0.33d)
		scorer.save()

		def scorerDur = new Scorer(
			name:"DurationPriority",
			description:"JSnoori scores with Duration given priority",
			type:"JSNOORI",
			durationWeight:0.6d,
			f0Weight:0.3d,
			intensityWeight:0.1d)
		scorerDur.save()

		def scorerW = new Scorer(
			name:"WekaScorer",
			description:"Uses weka classification",
			type:"WEKA",
			)
		scorerW.save()

		assert Scorer.count() == 3


		//// CREATE DIAGNOSIS METHODS

		def dm1 = new DiagnosisMethod(
			name:"Simple",
			description:"Manual choice, SimpleScorer",
			scorer:scorer,
			//referenceType:"SINGLE",
			numberOfReferences:1,
			selectionType:"MANUAL",
			)
		dm1.save()
		//println(dm1)

		def dm2 = new DiagnosisMethod(
			name:"Multi",
			description:"Multi Manual choice, SimpleScorer",
			scorer:scorer,
			//referenceType:"MULTI",
			selectionType:"MANUAL",
			numberOfReferences:2,
			)
		dm2.save()
		//println(dm2)

		def dm3 = new DiagnosisMethod(
			name:"Weka",
			description:"No references, Weka scorer",
			scorer:scorerW,
			numberOfReferences:0,
			)
		dm3.save()

		def dm4 = new DiagnosisMethod(
			name:"SingleAuto",
			description:"Single ref, auto choice, SimpleScorer",
			scorer:scorer,
			selectionType:"AUTO",
			numberOfReferences:1,
			)
		dm4.save()

		def dm5 = new DiagnosisMethod(
			name:"MultiAuto",
			description:"Multi ref, auto choice, SimpleScorer",
			scorer:scorer,
			selectionType:"AUTO",
			numberOfReferences:3
			)
		dm5.save()

		def dm6 = new DiagnosisMethod(
			name:"AutoDuration",
			description:"Single ref, auto choice, DurationPriority scorer",
			scorer:scorerDur,
			selectionType:"AUTO",
			numberOfReferences:1
			)
		dm6.save()

		assert DiagnosisMethod.count() == 6


		//// CREATE FEEDBACK METHODS
		def fm1 = new FeedbackMethod(
			name:"JsnooriAll",
			description:"All possible feedback from Jsnoori scorer",
			requiresScorerType:"JSNOORI",
			showSkillBars:true,
			playFeedbackSignal:true,
			displayShapes:true,
			styleText:true,
			selfAssessment:true,
			displayMessages:true,
			)
		fm1.save()

		def fm2 = new FeedbackMethod(
			name:"JsnooriNonvisual",
			description:"Simple, requires Jsnoori scorer, no visual fb",
			requiresScorerType:"JSNOORI",
			//showSkillBars:false,
			playFeedbackSignal:true,
			)
		fm2.save()

		def fm3 = new FeedbackMethod(
			name:"JsnooriVisual",
			description:"Only visual FB types, Jsnoori scores",
			requiresScorerType:"JSNOORI",
			showSkillBars:true,
			displayShapes:true,
			styleText:true,
			displayMessages:true,
			)
		fm3.save()

		def fmW = new FeedbackMethod(
			name:"ClassifierFeedback",
			description:"All FB types compatible with WEKA classification",
			requiresScorerType:"WEKA",
			//showSkillBars:false,
			//playFeedbackSignal:false,
			selfAssessment:true,
			displayShapes:true,
			styleText:true,
			displayMessages:true,
			)
		fmW.save()

		def fm4 = new FeedbackMethod(
			name:"ShapesAndText",
			description:"Only shapes and stylized text",
			displayShapes:true,
			styleText:true,
			)
		fm4.save()


		//// CREATE EXERCISES
		def ex4 = new Exercise(
			name:"FeedbackShowcase",
			description:"Single-reference comparison, all feedback types",
			word:Word.get(1),
			diagnosisMethod:dm1,
			feedbackMethod:fm1)
		ex4.save()

		def ex2 = new Exercise(
			name:"VisualLearners",
			description:"Multiple-reference diagnosis, only visual feedback",
			word:Word.get(2),
			diagnosisMethod:dm2,
			feedbackMethod:fm3)
		ex2.save()

		def ex3 = new Exercise(
			name:"Classification",
			description:"Classification diagnosis, all compatible feedback types",
			word:Word.get(3),
			diagnosisMethod:dm3,
			feedbackMethod:fmW,
			)
		ex3.save()

		def ex1 = new Exercise(
			name:"Simple",
			description:"Single-reference diagnosis with limited feedback",
			word:Word.get(1),
			diagnosisMethod:dm1,
			feedbackMethod:fm4)
		ex1.save()

		def ex5 = new Exercise(
			name:"SpeakerMatch",
			description:"Best reference speaker is selected automatically, visual feedback",
			word:Word.get(4),
			diagnosisMethod:dm6,
			feedbackMethod:fm3)
		ex5.save()

		def ex6 = new Exercise(
			name:"MultiMatch",
			description:"Multi-reference with automatic selection, simpler feedback",
			word:Word.get(5),
			diagnosisMethod:dm5,
			feedbackMethod:fm4,
			)
		ex6.save()


		assert Exercise.count() == 6




    }
    def destroy = {
    }
}
