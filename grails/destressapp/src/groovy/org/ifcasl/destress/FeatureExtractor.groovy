package org.ifcasl.destress

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;


import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.TextGridSegmentationFileUtils;


class FeatureExtractor {

	String wavFile
	String gridFile
	AudioSignal audioSignal
	Segmentation wordsSeg
	
	
	public FeatureExtractor(wavFile, gridFile) {
		// TODO Auto-generated constructor stub
		this.wavFile = wavFile
		this.gridFile = gridFile
		this.audioSignal = new AudioSignal(wavFile, wavFile) //passing wavFile as both "name" and "wavefile" params
		SegmentationList segList = new SegmentationList(this.audioSignal)
		this.audioSignal.setSegmentationList(segList)
		String nameSegFile = gridFile
		//println "nameSegFile: " + nameSegFile
		this.audioSignal.openSegmentationFile(new TextGridSegmentationFileUtils(), nameSegFile)
		this.audioSignal.segmentationList.setPhoneSegmentation(this.audioSignal.segmentationList.getSegmentation("RealTier"))
		this.audioSignal.segmentationList.setSyllablesSegmentation(this.audioSignal.segmentationList.getSegmentation("SyllableTier"))
		this.wordsSeg = this.audioSignal.segmentationList.getSegmentation("WordTier")
	}
	
	public String toString() {
		String description = "FeatureExtractor object:\n"
		description += "\twavFile:\t" + this.wavFile + "\n"
		description += "\tgridFile:\t" + this.gridFile + "\n"
		return description
	}
	
	public String printWords() {
		def words = []
		//Segmentation wordsSeg = this.audioSignal.segmentationList.getSegmentation("WordTier")
		for (Segment seg : this.wordsSeg.iterator()) {
			//println seg.getLengthMs()
			words.add(seg.name + " : " + seg.getLength().toString())
		}
		return "<p>" + words.join("</p><p>") + "</p>"
	}
	

	static main(args) {
		println "It works"
	
	}

}
