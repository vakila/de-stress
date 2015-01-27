package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.*;

class BackendTestingController {
	
	String wavPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/Audio/SH05_501.wav"
	//String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/TextGrids/SH05_501.textgrid"
	String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/FG/SH/SH01/2SH01_FGGB1_521.textgrid"
	
	String csvPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/Results/backups/DATA.csv"
	
	
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
			String output
			FeatureExtractor featex = new FeatureExtractor(wavPath, gridPath, "Boot")
			//String output = featex.printWordInfo()
			//String output = featex.getWordId("kleines")
			//String output = featex.printWords()
			if (featex.wordSegment == null) {
				output = "Word not found"
			}
			else {
				output = featex.printSyllables()
			}
			render output
		} 
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
	
	def csv() {
		try {
			DataProcessor dataproc = new DataProcessor(csvPath)
			render dataproc.toString();
		}
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
}
