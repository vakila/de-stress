package org.ifcasl.destress


import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import fr.loria.parole.jsnoori.model.teacher.feedback.*
//import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
//import fr.loria.parole.jsnoori.model.teacher.feedback.FeedbackComputer
//import fr.loria.parole.jsnoori.model.teacher.feedback.TimeFeedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.TextGridSegmentationFileUtils;
import fr.loria.parole.jsnoori.util.lang.Language;


class FeatureExtractor {

	public Language language = Language.getLanguage("de")
	
	String wavFile
	String gridFile
	String word
	AudioSignal audioSignal
	
	Segmentation wordsSegmentation
	Segment wordSegment
	Segmentation extractedSylls
	Segmentation extractedPhons
	
	FeedbackComputer fb
	
	
	public FeatureExtractor(String wavFile, String gridFile, String word) {
		this.wavFile = wavFile
		this.gridFile = gridFile
		this.word = word.toLowerCase()
		this.audioSignal = new AudioSignal(wavFile, wavFile) //passing wavFile as both "name" and "wavefile" params
		//SegmentationList segList = new SegmentationList(this.audioSignal)
		//this.audioSignal.setSegmentationList(segList)
		String nameSegFile = gridFile
		//println "nameSegFile: " + nameSegFile
		this.audioSignal.openSegmentationFile(new TextGridSegmentationFileUtils(), nameSegFile)
		this.audioSignal.segmentationList.setPhoneSegmentation(this.audioSignal.segmentationList.getSegmentation("RealTier"))
		this.audioSignal.segmentationList.setSyllablesSegmentation(this.audioSignal.segmentationList.getSegmentation("SyllableTier"))
		//println this.audioSignal.segmentationList
		this.wordsSegmentation = this.audioSignal.segmentationList.getSegmentation("WordTier")
		this.wordSegment = getWordSegment()
		this.extractedSylls = extractPartialSegmentation(this.audioSignal.segmentationList.seg_syllables, this.wordSegment)
		this.extractedPhons = extractPartialSegmentation(this.audioSignal.segmentationList.seg_phones, this.wordSegment)
		
		// Create a new FeedbackComputer 
		// (includes Feedback, TimeFeedback, and PitchFeedback)
		// where the trial and example audio are the same
		this.fb = new FeedbackComputer(this.audioSignal, this.audioSignal, language)
		
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
		
		//TODO Pitch
		
		//TODO Intensity
		
		return output
		
	}
	
	public String printVowelDurations() {
		String output = "<h3>Vowel durations</h3>"
		double wordDur = this.wordSegment.getLength()
		output += "<p>Word: " + this.word + "</p>"
		output += "<p>wordDur: " + wordDur.toString() + "</p>"
		//output += "<p>" + this.word + " " + wordDur.toString() + "</p>"
		
//		double totalVowelDur = this.fb.timeFeedback.computeTotalVowelDuration(this.extractedPhons)
//		double totalVowelDurWithSylls = this.fb.timeFeedback.computeTotalVowelDurationWithSyllables(this.extractedPhons, this.extractedSylls)
//		output += "<p>totalVowelDur: " + totalVowelDur.toString() + "</p>"
//		output += "<p>totalVowelDurWithSylls: " + totalVowelDurWithSylls.toString() + "</p>"
		
		//output += "<br><br>"
		
//		double totalvowelduration_in_syllables = 0
//		for (int p = 0; p < this.extractedPhons.getSegmentCount(); p++) {
//			String phSampa = this.extractedPhons.getSegment(p).getName();
//			if (! phSampa.contains("_")) {
//				String phIpa = this.fb.feedback.getPhoneticSymbolMapper().SAMPAtoIPA(phSampa)
//						output += "<p>" + p.toString() + " " + phSampa + " " + phIpa + " " + this.fb.feedback.getPhoneticFeatures().isVowel(phIpa).toString() + "</p>"
//						if (this.fb.feedback.getPhoneticFeatures().isVowel(phIpa)) {
//							totalvowelduration_in_syllables += this.extractedPhons.getSegment(p).getLength();
//						}
//			}
//		}
		
		double wordVowelDur = getTotalVowelDuration(this.extractedPhons)
		output += "<p>wordVowelDur: " + wordVowelDur.toString() + "</p><br>"
		
		output += """<table>
						<tr>
							<td>Syllable</td>
							<td>Dur</td>
							<td>VowelDur</td>
							<td>V%WordDur</td>
							<td>V%SyllDur</td>
							<td>V%WordVowelDur</td>
						</tr>"""
		
		for (Segment syll : this.extractedSylls) {
			Segmentation syllPhonSeg = extractPartialSegmentation(this.extractedPhons, syll)
			double syllVowelDur = getTotalVowelDuration(syllPhonSeg)
			output += "<tr><td>" + syll.name + "</td>"
			output += "<td>" + syll.getLength().toString() + "</td>"
			output += "<td>" + syllVowelDur.toString() + "</td>"
			output += "<td>" + (syllVowelDur/wordDur*100).toString() + "%</td>"
			output += "<td>" + (syllVowelDur/syll.getLength()*100).toString() + "%</td>"
			output += "<td>" + (syllVowelDur/wordVowelDur*100).toString() + "%</td>"
			output += "</tr>"
		}
		
		output += "</table>"
		return output
	}
	
	////////// DURATION METHODS //////////
	
	/**
	 * Modified version of TimeFeedback.computeTotalVowelDuration(seg_phone)
	 * that converts from SAMPA to IPA before checking phone type
	 * @param seg_phones	The (partial) phone-level segmentation to be scanned for vowels
	 * @return				Sum of durations of all vowel segments in the segmentation
	 */
	public double getTotalVowelDuration(Segmentation seg_phones) {
		double totalvowelduration_in_syllables = 0
		for (int p = 0; p < seg_phones.getSegmentCount(); p++) {
			String phSampa = seg_phones.getSegment(p).getName();
			if (! phSampa.contains("_")) {
				String phIpa = this.fb.feedback.getPhoneticSymbolMapper().SAMPAtoIPA(phSampa)
						if (this.fb.feedback.getPhoneticFeatures().isVowel(phIpa)) {
							totalvowelduration_in_syllables += seg_phones.getSegment(p).getLength();
						}
			}
		}
		return totalvowelduration_in_syllables
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
	 * Searches for a segment in wordsSeg whose name matches
	 * the given name. 
	 * Possible outputs:
	 * 	-1 : No segment found with this name 
	 * 	-2 : Multiple segments found with this name
	 * 	int >= 0 : Index of the unique segment found with this name
	 * 
	 * @param name
	 * @return
	 */
	public int getWordId(String name) {
		println "Searching for word: " + name
		boolean segFound = false;
		int segID = -1;
		for (int i = 0; i < wordsSegmentation.segments.size(); i++) {
			//print i + " : "
			//print wordsSegmentation.getSegment(i).getName() + " : "
			if (wordsSegmentation.getSegment(i).getName() == name) {
				//print "name matches : "
				if (segFound == true) {
					// we have found more than one segment with that name
					println "multiple matches"
					return -2;
				}
				else {
					//println "storing ID"
					segID = i;
					segFound = true;
				}
			}
		}
		if (segFound == false) {
			println "Word not found"
		} else { println "Word found" }
		return segID;
	}
	

	static main(args) {
		println "It works"
	
	}

}
