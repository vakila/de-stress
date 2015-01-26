package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.*;

class JsnooriTestController {
	
	String wavPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/Audio/SH05_501.wav"
	String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/TextGrids/SH05_501.textgrid"
	
    def index() { 
		try {
			AudioSignal testaudio = new AudioSignal("testname", wavPath);
			//Segmentation[] testseg = util.open(testfile);
			String output = "I opened a file! " + testaudio.name;
			render output; 
		} catch (Exception e) {
			render e.printStackTrace();
		}
	}
	
	def featex() {
		try {
			//FeatureExtractor.main()
			FeatureExtractor featex = new FeatureExtractor(wavPath, gridPath)
			render "It worked: " + featex.toString()
		} 
		catch (Exception e) {
			render e.toString()
		}
	}
}
