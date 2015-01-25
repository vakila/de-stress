package org.ifcasl.destress

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.SegmentationList;
import fr.loria.parole.jsnoori.util.file.segmentation.TextGridSegmentationFileUtils;

class FeatureExtractor {

	String wavFile
	String gridFile
	AudioSignal audioSignal
	
	
	public FeatureExtractor(wavFile, gridFile) {
		// TODO Auto-generated constructor stub
		this.wavFile = wavFile
		this.gridFile = gridFile
		this.audioSignal = new AudioSignal(wavFile, wavFile) //passing wavFile as both "name" and "wavefile" params
		//SegmentationList segList = new SegmentationList(this.audioSignal)
		//this.audioSignal.setSegmentationList(segList)
		//this.audioSignal.openSegmentationFile(TextGridSegmentationFileUtils, gridFile)
		//this.audioSignal.segmentationList.setPhoneSegmentation(this.audioSignal.segmentationList.getSegmentation("RealTier"))
		//this.audioSignal.segmentationList.setSyllablesSegmentation(this.audioSignal.segmentationList.getSegmentation("SyllableTier"))
		
	}
	
	public String toString() {
		String description = "FeatureExtractor object:\n"
		description += "\twavFile:\t" + this.wavFile + "\n"
		description += "\tgridFile:\t" + this.gridFile + "\n"
		return description
	}
	
	public getDuration(word) {
		
	}

	static main(args) {
		println "It works"
	
	}

}
