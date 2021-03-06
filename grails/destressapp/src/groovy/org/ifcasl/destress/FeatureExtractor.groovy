package org.ifcasl.destress


import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import fr.loria.parole.jsnoori.model.teacher.feedback.*
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.TextGridSegmentationFileUtils;
import fr.loria.parole.jsnoori.util.lang.Language;
import fr.loria.parole.jsnoori.view.phoneticKeyboard.*;
import fr.loria.parole.jsnoori.util.TimeConversion;




class FeatureExtractor {

	Language language = Language.getLanguage("de")
	PhoneticSymbolMapper phonMapper = PhoneticSymbolMapperFactory.createPhoneticSymbolMapper(language);
	PhoneticFeatures phonFeatures = PhoneticFeaturesFactory.createPhoneticFeatures(language);

	String wavFile
	String gridFile
	String word
	AudioSignal audioSignal

	Segmentation wordsSegmentation
	Segment wordSegment
	Segmentation extractedSylls
	Segmentation extractedPhons

	//Removing and refactoring FeedbackComputer fbc
	PitchAnalysis pitchAnalysis
	EnergyAnalysis energyAnalysis


	public FeatureExtractor(String wavFile, String gridFile, String word) {
		this.wavFile = wavFile
		this.gridFile = gridFile
		this.word = word.toLowerCase()
		//println "word: " + this.word
		//this.audioSignal = new AudioSignal(wavFile)
		this.audioSignal = new AudioSignal(wavFile, wavFile) //passing wavFile as both "name" and "wavefile" params
		//SegmentationList segList = new SegmentationList(this.audioSignal)
		//this.audioSignal.setSegmentationList(segList)
		String nameSegFile = gridFile
		//println "nameSegFile: " + nameSegFile
		this.audioSignal.openSegmentationFile(new TextGridSegmentationFileUtils(), nameSegFile)
		this.audioSignal.segmentationList.setPhoneSegmentation(this.audioSignal.segmentationList.getSegmentation("RealTier"))
		this.audioSignal.segmentationList.setSyllablesSegmentation(this.audioSignal.segmentationList.getSegmentation("SyllableTier"))
		//println "segmentationList: " + this.audioSignal.segmentationList
		this.wordsSegmentation = this.audioSignal.segmentationList.getSegmentation("WordTier")
		//println "wordsSegmentation.name: " + this.wordsSegmentation.getName()
		this.wordSegment = getWordSegment()
		//println "wordSegment.name: " + this.wordSegment.getName()
		this.extractedSylls = extractPartialSegmentation(this.audioSignal.segmentationList.seg_syllables, this.wordSegment)
		this.extractedPhons = extractPartialSegmentation(this.audioSignal.segmentationList.seg_phones, this.wordSegment)

		this.pitchAnalysis = new PitchAnalysis(this.audioSignal)
		this.energyAnalysis = new EnergyAnalysis(this.audioSignal, 20d)
		//println("ENERGY depEner IN MILLISECONDS: " + TimeConversion.enMillif((double) this.energyAnalysis.energyComputer.depEner, this.audioSignal))

//		// Create a new FeedbackComputer
//		// (includes Feedback, TimeFeedback, and PitchFeedback)
//		// where the trial and example audio are the same
//		this.fbc = new FeedbackComputer(this.audioSignal, this.audioSignal, language)

	}


	//TODO NEW CONSTRUCTOR FOR WHOLE SENTENCE UTTERANCE
	public FeatureExtractor(String wavFile, String gridFile) {
		this.wavFile = wavFile
		this.gridFile = gridFile
		this.audioSignal = new AudioSignal(wavFile, wavFile) //passing wavFile as both "name" and "wavefile" params
		String nameSegFile = gridFile
		this.audioSignal.openSegmentationFile(new TextGridSegmentationFileUtils(), nameSegFile)
		this.audioSignal.segmentationList.setPhoneSegmentation(this.audioSignal.segmentationList.getSegmentation("RealTier"))
		this.audioSignal.segmentationList.setSyllablesSegmentation(this.audioSignal.segmentationList.getSegmentation("SyllableTier"))
		this.wordsSegmentation = this.audioSignal.segmentationList.getSegmentation("WordTier")
		this.pitchAnalysis = new PitchAnalysis(this.audioSignal)
		this.energyAnalysis = new EnergyAnalysis(this.audioSignal, 20d)
	}




	////////// DURATION METHODS //////////

	/**
	 * Returns the duration (length) of the entire signal (includes silence)
	 * @return
	 */
	public double getTotalDuration() {
		return audioSignal.segmentationList.getLength()
	}

	/**
	* Returns the duration (length) of the segmentation once starting and ending
	* silences have been removed (based on syllable_real_start_position and
	* syllable_real_number)
	* @return
	*/
	public double getSpeakingDuration() {
		int firstSyllableIndex = pitchAnalysis.getSyllable_real_start_position()
		Segment firstSyllable = audioSignal.segmentationList.getSyllablesSegmentation().getSegment(firstSyllableIndex)
		println("firstSyllable: " + firstSyllableIndex + " - " + firstSyllable.getName())

		int lastSyllableIndex = firstSyllableIndex + pitchAnalysis.getSyllable_real_number() - 1
		Segment lastSyllable = audioSignal.segmentationList.getSyllablesSegmentation().getSegment(lastSyllableIndex)
		println("lastSyllable: " + lastSyllableIndex + " - " + lastSyllable.getName())

		double startTime = firstSyllable.getBegin() //seconds
		double endTime = lastSyllable.getEnd() //seconds

		return endTime - startTime
	}


	/**
	 * Returns the duration (length) of this.wordSegment
	 * @return
	 */
	public double getWordDuration() {
		return wordSegment.getLength()
	}

	/**
	 * Returns the sum of each vowel's
	 * @return
	 */
	public double getVowelDurationInWord() {
		return getVowelDuration(extractedPhons)
	}

	/**
	 * Returns the duration of the syllable with the given index in the word
	 * @param syllIndexInWord	Index of the syllable within the word (e.g. 0 = first syllable, 1 = second syllable)
	 * @return
	 */
	public double getSyllableDuration(int syllIndexInWord) {
		Segment syllSeg = extractedSylls.getSegment(syllIndexInWord)
		return syllSeg.getLength()
	}

	/**
	 * Returns the duration of the vowel(s) in the syllable with the given index (see getVowelDuration())
	 * @param syllIndexInWord	Index of the syllable within the word (e.g. 0 = first syllable, 1 = second syllable)
	 * @return
	 */
	public double getVowelDurationInSyllable(int syllIndexInWord) {
		Segment syllSeg = extractedSylls.getSegment(syllIndexInWord)
		Segmentation syllPhons = extractPartialSegmentation(extractedPhons, syllSeg)
		return getVowelDuration(syllPhons)
	}


	/**
	 * Uses getVowelSegments() and Segment.getLength() to sum up the
	 * durations of each vowel (or syllabic consonant) segment in the segmentation.
	 * @param seg_phones	The (partial) phone-level segmentation to be scanned for vowels
	 * @return				Sum of durations of vowels in that segmentation
	 */
	public double getVowelDuration(Segmentation seg_phones) {
		def vowels = getVowelSegments(seg_phones)

		// sum up the durations of the segments in vowels
		double totalvowelduration_in_syllables = 0
		for (vowelSeg in vowels) {
			totalvowelduration_in_syllables += vowelSeg.getLength();
		}

		return totalvowelduration_in_syllables
	}

	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableDuration(index) to get the duration of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public double getRelSyllDuration() {
		return getSyllableDuration(1)/getSyllableDuration(0)
	}

	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelDuration(index) to get the duration of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public double getRelVowelDuration() {
		return getVowelDurationInSyllable(1)/getVowelDurationInSyllable(0)
	}


	////////// F0 METHODS //////////

	public float getOverallF0Mean() {
		return pitchAnalysis.computeOverallMeanF0()
	}

	public float getOverallF0Range() {
		return pitchAnalysis.computeOverallRangeF0()
	}

	public float getOverallF0Max() {
		return pitchAnalysis.computeOverallMaxF0()
	}

	public float getOverallF0Min() {
		return pitchAnalysis.computeOverallMinF0()
	}

	public float getWordF0Mean() {
		return pitchAnalysis.computePitchMeanInSegment(wordSegment)
	}

	public float getWordF0Max() {
		return pitchAnalysis.computePitchMaxInSegment(wordSegment)
	}

	public float getWordF0Min() {
		return pitchAnalysis.computePitchMinInSegment(wordSegment)
	}

	public float getWordF0Range() {
		return getWordF0Max()-getWordF0Min()
	}

	public float getSyllableF0Mean(int syllIndexInWord) {
		def syllSeg = extractedSylls.getSegment(syllIndexInWord)
		return pitchAnalysis.computePitchMeanInSegment(syllSeg)
	}

	public float getSyllableF0Max(int syllIndexInWord) {
		def syllSeg = extractedSylls.getSegment(syllIndexInWord)
		return pitchAnalysis.computePitchMaxInSegment(syllSeg)
	}

	public float getSyllableF0Min(int syllIndexInWord) {
		def syllSeg = extractedSylls.getSegment(syllIndexInWord)
		return pitchAnalysis.computePitchMinInSegment(syllSeg)
	}

	public float getSyllableF0Range(int syllIndexInWord) {
		def syllSeg = extractedSylls.getSegment(syllIndexInWord)
		def max = pitchAnalysis.computePitchMaxInSegment(syllSeg)
		def min = pitchAnalysis.computePitchMinInSegment(syllSeg)
		return max-min
	}

	public float getVowelF0Mean(int syllIndexInWord) {
		def vowels = getVowelsInSyllable(syllIndexInWord)
		def sumMeans = 0f
		for (v in vowels) {
			sumMeans += pitchAnalysis.computePitchMeanInSegment(v)
		}
		def avgMean = sumMeans/vowels.size()
		return avgMean
	}

	public float getVowelF0Max(int syllIndexInWord) {
		def vowels = getVowelsInSyllable(syllIndexInWord)
		def maxF0 = 0f
		for (v in vowels) {
			def thisMax = pitchAnalysis.computePitchMaxInSegment(v)
			if (thisMax > maxF0) {
				maxF0 = thisMax
			}
		}
		return maxF0
	}

	public float getVowelF0Min(int syllIndexInWord) {
		def vowels = getVowelsInSyllable(syllIndexInWord)
		def minF0 = getSyllableF0Max(syllIndexInWord)
		for (v in vowels) {
			def thisMin = pitchAnalysis.computePitchMinInSegment(v)
			if (0 < thisMin && thisMin < minF0) {
				minF0 = thisMin
			}
		}
		return minF0
	}

	public float getVowelF0Range(int syllIndexInWord) {
		def max = getVowelF0Max(syllIndexInWord)
		def min = getVowelF0Min(syllIndexInWord)
		return max-min
	}


	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableF0Mean(index) to get the mean F0 of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public float getRelSyllF0Mean() {
		return getSyllableF0Mean(1)/getSyllableF0Mean(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableF0Max(index) to get the max F0 of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public float getRelSyllF0Max() {
		return getSyllableF0Max(1)/getSyllableF0Max(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableF0Min(index) to get the min F0 of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public float getRelSyllF0Min() {
		return getSyllableF0Min(1)/getSyllableF0Min(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableF0Range(index) to get the F0 range of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public float getRelSyllF0Range() {
		return getSyllableF0Range(1)/getSyllableF0Range(0)
	}

	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelF0Mean(index) to get the mean F0 of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public float getRelVowelF0Mean() {
		return getVowelF0Mean(1)/getVowelF0Mean(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelF0Max(index) to get the max F0 of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public float getRelVowelF0Max() {
		return getVowelF0Max(1)/getVowelF0Max(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelF0Min(index) to get the min F0 of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public float getRelVowelF0Min() {
		return getVowelF0Min(1)/getVowelF0Min(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelF0Range(index) to get the F0 range of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public float getRelVowelF0Range() {
		return getVowelF0Range(1)/getVowelF0Range(0)
	}


	/**
	 * Returns the index (0 or 1) of the syllable with the highest F0 max,
	 * or -1 if the two syllables' F0 max values are equal.
	 * @return
	 */
	public int getMaxF0Index() {
		def syll0 = getSyllableF0Max(0)
		def syll1 = getSyllableF0Max(1)
		if (syll0 < syll1) {
			return 1
		}
		else if (syll0 > syll1) {
			return 0
		}
		else {
			return -1 //null
		}
	}

	/**
	 * Returns the index (0 or 1) of the syllable with the lowest (nonzero) F0 min,
	 * or -1 if the two syllables' F0 min values are equal.
	 * @return
	 */
	public int getMinF0Index() {
		def syll0 = getSyllableF0Min(0)
		def syll1 = getSyllableF0Min(1)
		if (syll0 < syll1) {
			return 0
		}
		else if (syll0 > syll1) {
			return 1
		}
		else {
			return -1 //null
		}
	}

	/**
	 * Returns the index (0 or 1) of the syllable with the highest F0 range,
	 * or -1 if the two syllables' F0 range values are equal.
	 * @return
	 */
	public int getMaxRangeF0Index() {
		def syll0 = getSyllableF0Range(0)
		def syll1 = getSyllableF0Range(1)
		if (syll0 < syll1) {
			return 1
		}
		else if (syll0 > syll1) {
			return 0
		}
		else {
			return -1 //null
		}
	}

	////////// ENERGY METHODS //////////

	public double getOverallEnergyMean() {
		return energyAnalysis.computeOverallMeanEnergy()
	}

	public double getOverallEnergyMax() {
		return energyAnalysis.computeOverallMaxEnergy()
	}

	public double getWordEnergyMean() {
		return energyAnalysis.computeEnergyMeanInSegment(wordSegment)
	}

	public double getWordEnergyMax() {
		return energyAnalysis.computeEnergyMaxInSegment(wordSegment)
	}

	public double getSyllableEnergyMean(int syllIndexInWord) {
		def syllSeg = extractedSylls.getSegment(syllIndexInWord)
		return energyAnalysis.computeEnergyMeanInSegment(syllSeg)
	}

	public double getSyllableEnergyMax(int syllIndexInWord) {
		def syllSeg = extractedSylls.getSegment(syllIndexInWord)
		return energyAnalysis.computeEnergyMaxInSegment(syllSeg)
	}

	public double getVowelEnergyMean(int syllIndexInWord) {
		def vowels = getVowelsInSyllable(syllIndexInWord)
		def sumMeans = 0f
		for (v in vowels) {
			sumMeans += energyAnalysis.computeEnergyMeanInSegment(v)
		}
		def avgMean = sumMeans/vowels.size()
		return avgMean
	}

	public double getVowelEnergyMax(int syllIndexInWord) {
		def vowels = getVowelsInSyllable(syllIndexInWord)
		def maxF0 = 0f
		for (v in vowels) {
			def thisMax = energyAnalysis.computeEnergyMaxInSegment(v)
			if (thisMax > maxF0) {
				maxF0 = thisMax
			}
		}
		return maxF0
	}

	/**
	 * Assumes the word only has two syllables.
	 * Returns the index (0 or 1) of the syllable with the highest F0 max,
	 * or -1 if the two syllables' F0 max values are equal.
	 * @return
	 */
	public int getMaxEnergyIndex() {
		def syll0 = getSyllableEnergyMax(0)
		def syll1 = getSyllableEnergyMax(1)
		if (syll0 < syll1) {
			return 1
		}
		else if (syll0 > syll1) {
			return 0
		}
		else {
			return -1 //null
		}
	}

	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableEnergyMean(index) to get the mean energy of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public double getRelSyllEnergyMean() {
		return getSyllableEnergyMean(1)/getSyllableEnergyMean(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getSyllableEnergyMax(index) to get the max energy of each syllable.
	 * Returns the value for Syllable 1 divided by the value for Syllable 0.
	 * @return
	 */
	public double getRelSyllEnergyMax() {
		return getSyllableEnergyMax(1)/getSyllableEnergyMax(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelEnergyMean(index) to get the mean energy of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public double getRelVowelEnergyMean() {
		return getVowelEnergyMean(1)/getVowelEnergyMean(0)
	}
	/**
	 * Assumes the word only has two syllables, 0 and 1.
	 * Uses getVowelEnergyMax(index) to get the max energy of each syllable's vowel(s).
	 * Returns the value for Vowel 1 divided by the value for Vowel 0.
	 * @return
	 */
	public double getRelVowelEnergyMax() {
		return getVowelEnergyMax(1)/getVowelEnergyMax(0)
	}

	////////// UTILITY METHODS //////////

	/**
	 * Extracts the part of full_segmentation that falls between the start and end
	 * of the target segment (e.g. syllables in a word, phones in a syllable, etc.).
	 * The name of the new segmentation is the name of the full segmentation
	 * and the name of the target segment, separated by a hyphen.
	 * @param full_segmentation		Syllable or phone segmentation for the entire utterance
	 * @param word_segment			Segment for which syllables/phones should be extracted
	 * @return						Segmentation object containing the segments corresponding to the target
	 */
	public Segmentation extractPartialSegmentation(Segmentation full_segmentation, Segment target_segment) {
		double begin = target_segment.getBegin()
		double end = target_segment.getEnd()

		List<Segment> segmentList = full_segmentation.getExtractedSegments(begin, end)
		Segment[] segmentArray = segmentList.toArray()
		String newName = full_segmentation.name + "-" + target_segment.name
		Segmentation extractedSegmentation = new Segmentation(newName, segmentArray)

		return extractedSegmentation
	}


	//TODO use Segmentation.getSegmentByName(name) instead
	/**
	 * Returns the Segment object corresponding to this.word, by finding its index with getWordId(word).
	 * Returns null if the word is not found exactly once in the word-level segmentation.
	 * @return
	 */
	public Segment getWordSegment() {
		int wordSegId = getWordId(word)
		if (wordSegId < 0) {
			return null
		}
		else {
			Segment wordSeg = wordsSegmentation.getSegment(wordSegId)
			return wordSeg
		}
	}

	/**
	 * Searches for a unique segment in wordsSeg whose name matches
	 * the given name. Returns the index of that segment, if found.
	 * Possible outputs:
	 * 	-1 : No segment found with this name
	 * 	-2 : Multiple segments found with this name
	 * 	int >= 0 : Index of the unique segment found with this name
	 *
	 * @param name
	 * @return
	 */
	public int getWordId(String name) {
		//println "Searching for word: " + name
		boolean segFound = false;
		int segID = -1;
		for (int i = 0; i < wordsSegmentation.segments.size(); i++) {
			//print i + " : "
			//print wordsSegmentation.getSegment(i).getName() + " : "
			if (wordsSegmentation.getSegment(i).getName() == name) {
				//print "name matches : "
				if (segFound == true) {
					// we have found more than one segment with that name
					//println "multiple matches"
					return -2;
				}
				else {
					//println "storing ID"
					segID = i;
					segFound = true;
				}
			}
		}
//		if (segFound == false) {
//			println "Word not found"
//		} else { println "Word found" }
		return segID;
	}



	/**
	 * Modified version of TimeFeedback.computeTotalVowelDuration(seg_phone)
	 * that converts from SAMPA to IPA before checking phone type.
	 * Includes syllabic consonants (=l, =m, =n) as vowels.
	 * @param seg_phones	The (partial) phone-level segmentation to be scanned for vowels
	 * @return				List of vowel segments in that segmentation
	 */
	public List getVowelSegments(Segmentation seg_phones) {
		def vowels = []
		//def phonFeats = this.fbc.feedback.getPhoneticFeatures()

		// go through the phone segments and pick out those that are vowels or syllabic consonants
		for (int p = 0; p < seg_phones.getSegmentCount(); p++) {
			String phSampa = seg_phones.getSegment(p).getName()
			//println p + ": " + phSampa
			if (! phSampa.contains("_")) {
				String phIpa = phonMapper.SAMPAtoIPA(phSampa) //this.fbc.feedback.getPhoneticSymbolMapper().SAMPAtoIPA(phSampa)
				if (phonFeatures.isVowel(phIpa) || phonFeatures.isSyllabic(phIpa)) {
					vowels.add(seg_phones.getSegment(p))
					//println " |-> VOWEL"
					//totalvowelduration_in_syllables += seg_phones.getSegment(p).getLength();
				}
			}
		}

		// if no vowels/syllabics were found, go through again and
		// pick out potentially-syllabic consonants that might not have been labeled as syllabic
		if (vowels.isEmpty()) {
			for (int p = 0; p < seg_phones.getSegmentCount(); p++) {
				String phSampa = seg_phones.getSegment(p).getName()
				if (! phSampa.contains("_")) {
					String phIpa = phonMapper.SAMPAtoIPA(phSampa) //this.fbc.feedback.getPhoneticSymbolMapper().SAMPAtoIPA(phSampa)
					if (phIpa.matches("n") || phIpa.matches("m") || phIpa.matches("l")) {
						vowels.add(seg_phones.getSegment(p))
						//totalvowelduration_in_syllables += seg_phones.getSegment(p).getLength();
					}
				}
			}
		}
		//println "vowels: " + vowels.collect { it.getName() + " - " + it.getBegin() + ":" + it.getEnd()}
		return vowels
	}

	/**
	 * Uses getVowelSegments() to get the list of vowel segments
	 * in the syllable with the given index
	 * @param syllIndexInWord		0 = first syllable, 1 = second syllable
	 * @return						List of vowel segments in that syllable
	 */
	private List getVowelsInSyllable(int syllIndexInWord) {
		Segment syllSeg = extractedSylls.getSegment(syllIndexInWord)
		Segmentation syllPhons = extractPartialSegmentation(extractedPhons, syllSeg)
		def vowels = getVowelSegments(syllPhons)
		return vowels
	}



	////////// REPORTING (PRINT) METHODS //////////

	public String toString() {
		String description = "FeatureExtractor object:\n"
		description += "\twavFile:\t" + this.wavFile + "\n"
		description += "\tgridFile:\t" + this.gridFile + "\n"
		return description
	}

	public String printWordDurations() {
		return printSegDurations(this.wordsSegmentation)
	}

	public String printSyllableDurations() {
		//return printSegLengths(this.audioSignal.segmentationList.seg_syllables)
		//def syllables = getSyllableSegments()
		double wordBegin = this.wordSegment.getBegin()
		double wordEnd = this.wordSegment.getEnd()
		def syllables = this.audioSignal.segmentationList.seg_syllables.getExtractedSegments(wordBegin, wordEnd)
		return printSegDurations(syllables)
	}

	/**
	 * Returns HTML code displaying the name and duration
	 *  of each segment in the given collection of Segments.
	 *  Collection can be a Segmentation object or a
	 *  List or Array of Segment objects.
	 * @param segmentCollection		Segmentation, List<Segment>, or Segment[]
	 * @return						String of HTML code
	 */
	public String printSegDurations(Object segmentCollection) {
		def segs
		if (segmentCollection == null) {
			return "Null segmentation"
		}
		else if (segmentCollection.getClass().equals(Segmentation)) {
			segs = segmentCollection.segments
		}
		else //if (segmentCollection.getClass().equals(List))
		{
			segs = segmentCollection
		}
		def output = []
		for (Segment seg : segs) {
			output.add(seg.name + " : " + seg.getLength().toString())
		}
		return "<p>" + output.join("</p><p>") + "</p>"

	}

	public String printWordInfo() {
		int wordSegId = getWordId(word)
		//Segment wordSeg = getWordSeg()

		// Word text & ID
		String output = "<p>WORD: " + word + "</p>"
		output += "<p>ID: " + wordSegId.toString() + "</p>"

		// Duration
		output += "<p>START: " + wordSegment.getBegin().toString() + "</p>"
		output += "<p>END: " + wordSegment.getEnd().toString() + "</p>"
		output += "<p>DURATION: " + wordSegment.getLengthMs().toString() + " ms</p>"

		//TODO F0

		//TODO Energy

		return output

	}

// 	public String printVowelDurations() {
// 		String output = "<h3>Vowel durations</h3>"
// 		double wordDur = this.wordSegment.getLength()
// 		output += "<p>Word: " + this.word + "</p>"
// 		output += "<p>wordDur: " + wordDur.toString() + "</p>"
// 		//output += "<p>" + this.word + " " + wordDur.toString() + "</p>"
//
// //		double totalVowelDur = this.fb.timeFeedback.computeTotalVowelDuration(this.extractedPhons)
// //		double totalVowelDurWithSylls = this.fb.timeFeedback.computeTotalVowelDurationWithSyllables(this.extractedPhons, this.extractedSylls)
// //		output += "<p>totalVowelDur: " + totalVowelDur.toString() + "</p>"
// //		output += "<p>totalVowelDurWithSylls: " + totalVowelDurWithSylls.toString() + "</p>"
//
// 		//output += "<br><br>"
//
// //		double totalvowelduration_in_syllables = 0
// //		for (int p = 0; p < this.extractedPhons.getSegmentCount(); p++) {
// //			String phSampa = this.extractedPhons.getSegment(p).getName();
// //			if (! phSampa.contains("_")) {
// //				String phIpa = this.fb.feedback.getPhoneticSymbolMapper().SAMPAtoIPA(phSampa)
// //						output += "<p>" + p.toString() + " " + phSampa + " " + phIpa + " " + this.fb.feedback.getPhoneticFeatures().isVowel(phIpa).toString() + "</p>"
// //						if (this.fb.feedback.getPhoneticFeatures().isVowel(phIpa)) {
// //							totalvowelduration_in_syllables += this.extractedPhons.getSegment(p).getLength();
// //						}
// //			}
// //		}
//
// 		double wordVowelDur = getVowelDuration(this.extractedPhons)
// 		output += "<p>wordVowelDur: " + wordVowelDur.toString() + "</p><br>"
//
// 		output += """<table>
// 						<tr>
// 							<td>Syllable</td>
// 							<td>Dur</td>
// 							<td>VowelDur</td>
// 							<td>V%WordDur</td>
// 							<td>V%SyllDur</td>
// 							<td>V%WordVowelDur</td>
// 						</tr>"""
//
// 		for (Segment syll : this.extractedSylls) {
// 			Segmentation syllPhonSeg = extractPartialSegmentation(this.extractedPhons, syll)
// 			double syllVowelDur = getVowelDuration(syllPhonSeg)
// 			output += "<tr><td>" + syll.name + "</td>"
// 			output += "<td>" + syll.getLength().toString() + "</td>"
// 			output += "<td>" + syllVowelDur.toString() + "</td>"
// 			output += "<td>" + (syllVowelDur/wordDur*100).toString() + "%</td>"
// 			output += "<td>" + (syllVowelDur/syll.getLength()*100).toString() + "%</td>"
// 			output += "<td>" + (syllVowelDur/wordVowelDur*100).toString() + "%</td>"
// 			output += "</tr>"
// 		}
//
// 		output += "</table>"
// 		return output
// 	}



}
