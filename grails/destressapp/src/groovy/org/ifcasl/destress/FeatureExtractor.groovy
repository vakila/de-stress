package org.ifcasl.destress


import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.TextGridSegmentationFileUtils;


class FeatureExtractor {

	String wavFile
	String gridFile
	String word
	AudioSignal audioSignal
	Segmentation wordsSegmentation
	Segment wordSegment
	
	
	public FeatureExtractor(String wavFile, String gridFile, String word) {
		this.wavFile = wavFile
		this.gridFile = gridFile
		this.word = word.toLowerCase()
		this.audioSignal = new AudioSignal(wavFile, wavFile) //passing wavFile as both "name" and "wavefile" params
		SegmentationList segList = new SegmentationList(this.audioSignal)
		this.audioSignal.setSegmentationList(segList)
		String nameSegFile = gridFile
		//println "nameSegFile: " + nameSegFile
		this.audioSignal.openSegmentationFile(new TextGridSegmentationFileUtils(), nameSegFile)
		this.audioSignal.segmentationList.setPhoneSegmentation(this.audioSignal.segmentationList.getSegmentation("RealTier"))
		this.audioSignal.segmentationList.setSyllablesSegmentation(this.audioSignal.segmentationList.getSegmentation("SyllableTier"))
		//println this.audioSignal.segmentationList
		this.wordsSegmentation = this.audioSignal.segmentationList.getSegmentation("WordTier")
		this.wordSegment = getWordSegment()
		
	}
	
	public String toString() {
		String description = "FeatureExtractor object:\n"
		description += "\twavFile:\t" + this.wavFile + "\n"
		description += "\tgridFile:\t" + this.gridFile + "\n"
		return description
	}
	
	public String printWords() {
		return printSegLengths(this.wordsSegmentation)
	}
	
	public String printSyllables() {
		//return printSegLengths(this.audioSignal.segmentationList.seg_syllables)
		//def syllables = getSyllableSegments()
		double wordBegin = this.wordSegment.getBegin()
		double wordEnd = this.wordSegment.getEnd()
		def syllables = this.audioSignal.segmentationList.seg_syllables.getExtractedSegments(wordBegin, wordEnd)
		return printSegLengths(syllables)
	}
	
//	public String printSegLengths(Segmentation segs) {
//		if (segs == null) {
//			return "Empty segmentation"
//		}
//		else {
//			def output = []
//			for (Segment seg : segs.segments) {
//				output.add(seg.name + " : " + seg.getLength().toString())
//			}
//			return "<p>" + output.join("</p><p>") + "</p>"
//		}
//	}

	public String printSegLengths(Object segmentCollection) {
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
		String output = "<p>WORD: " + word + "</p>"
		output += "<p>ID: " + wordSegId.toString() + "</p>"
		output += "<p>START: " + wordSegment.getBegin().toString() + "</p>"
		output += "<p>END: " + wordSegment.getEnd().toString() + "</p>"
		output += "<p>DURATION: " + wordSegment.getLengthMs().toString() + " ms</p>"
		return output
		
	}
	
	
	
//	// Duplicates Segmentation.getExtractedSegments()
//	public Segment[] getSyllableSegments() {
//		def syllables = []
//		double wordBegin = wordSegment.getBegin()
//		double wordEnd = wordSegment.getEnd()
//		
//		for (Segment seg : audioSignal.segmentationList.seg_syllables) {
//			println "segment: " + seg.name
//			double segBegin = seg.getBegin()
//			if (segBegin >= wordBegin && segBegin < wordEnd) {
//				println "wordBegin <= segBegin < wordEnd"
//				double segEnd = seg.getEnd()
//				if (segEnd > wordEnd) {
//					println "segEnd > wordEnd --- WordTier and SyllableTier boundaries don't match"
//					
//				} 
//				else {
//					println "segEnd <= wordEnd --- Adding to syllables"
//					syllables.add(seg)
//				}
//			}
//		}
//		return syllables
//	}
	
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
