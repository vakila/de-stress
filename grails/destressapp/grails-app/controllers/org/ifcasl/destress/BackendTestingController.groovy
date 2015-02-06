package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;
import fr.loria.parole.jsnoori.util.file.segmentation.*;
import fr.loria.parole.jsnoori.util.lang.Language;
import fr.loria.parole.jsnoori.view.phoneticKeyboard.GermanPhoneticFeatures;
import fr.loria.parole.jsnoori.view.phoneticKeyboard.GermanPhoneticSymbolMapper;
import fr.loria.parole.jsnoori.view.phoneticKeyboard.PhoneticFeatureSet
import fr.loria.parole.jsnoori.view.phoneticKeyboard.PhoneticFeatures
import fr.loria.parole.jsnoori.view.phoneticKeyboard.PhoneticFeaturesFactory;
import fr.loria.parole.jsnoori.view.phoneticKeyboard.PhoneticSymbolMapper
import fr.loria.parole.jsnoori.view.phoneticKeyboard.PhoneticSymbolMapperFactory;

class BackendTestingController {
	
	String wavPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/Audio/SH05_501.wav"
	//String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/TextGrids/SH05_501.textgrid"
	String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/FG/SH/SH01/2SH01_FGGB1_521.textgrid"
	
	String csvPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/Results/backups/DATA.csv"
	String arffOutputPath = "/Users/Anjana/Desktop/DATA.arff"
	
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
			render dataproc.toString()
		}
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
	
	def arff() {
		try {
			DataProcessor dataproc = new DataProcessor(csvPath)
			dataproc.writeArff(arffOutputPath)
			render "Wrote ARFF to: " + arffOutputPath
		}
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
	
	def phon() {
		try {
			Language language = Language.getLanguage("de")
			PhoneticSymbolMapper mapper = PhoneticSymbolMapperFactory.createPhoneticSymbolMapper(language);
			Map phonfeatures = PhoneticFeaturesFactory.createPhoneticFeatures(language).phoneticfeaturearray;
		
			String output = "<h3>German Phonetic Features</h3>"
			output += "<table>"
			output += """<tr>
							<td>Symbol</td>
							<td>Kind</td>
							<td>Voicing</td>
							<td>Description</td>
							<td>Example</td>
						</tr>"""
			
			SortedSet<String> phons = new TreeSet<String>(phonfeatures.keySet());
			for (String phon in phons) {
				output += "<tr>"
				
				PhoneticFeatureSet features = phonfeatures.get(phon)
				
				output += "<td>" + phon + "</td>"
				output += "<td>" + features.getKind() + "</td>"
				output += "<td>" + features.getVoicing() + "</td>"
				output += "<td>" + features.comment + "</td>"
				output += "<td>" + features.example + "</td>"
				
				output += "</tr>"
			}
			output += "</table>"
			
			
			render output
		}
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
}
