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
	
	//String wavPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/Audio/SH05_501.wav"
	//String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/TextGrids/SH05_501.textgrid"
	
	String wavPath = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/FG/SH/SH02/2SH02_FGBA2_546.wav"
	String gridPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/FG/SH/SH02/2SH02_FGBA2_546.textgrid"
	
	String csvPath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/Results/backups/DATA.csv"
	String arffOutputPath = "/Users/Anjana/Desktop/DATA.arff"
	String dataDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"
	
	String FGwavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/FG"
	String GGwavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/GG"
	String FGgridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/FG"
	String GGgridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/GG"
	String FGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FG-consolidated.csv"
	String GGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/GG/GG-annotation.csv"
	
	Language language = Language.getLanguage("de")
	
	
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
			FeatureExtractor featex = new FeatureExtractor(wavPath, gridPath, "kinder")
			//String output = featex.printWordInfo()
			//String output = featex.getWordId("kleines")
			//String output = featex.printWords()
			if (featex.wordSegment == null) {
				output = "Word not found"
			}
			else {
				//output = featex.printSyllables()
				output = featex.printVowelDurations()
			}
			render output
		} 
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
	
	def addattrs() {
		try {
			DataProcessor dataproc = new DataProcessor(csvPath)
			dataproc.addNumAttribute("WORD_DUR")
			dataproc.addNumAttribute("SYLL2/SYLL1")
			dataproc.addNumAttribute("V2/V1")
			
			dataproc.writeDataToArff(dataDir + "addedAttributes.arff")
			
			//render dataproc.printRandomFileNames(10)
			render dataproc.printFileNames()
		}
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
	
	def extractFG() {
		//try {
			DataProcessor dataproc = new DataProcessor(FGcsv)
			def errors = dataproc.extractNewFeatures(FGwavDir, FGgridDir)
			dataproc.writeDataToArff(dataDir + "FG_extracted.arff")
			
			def output = "<h3>Errors:</h3>"
			for (e in errors) {
				output += "<p>" + e + "</p>"
			}
			render output
		//}
		//catch (Exception e) {
		//	e.printStackTrace()
		//	render e.toString()
		//}
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
			dataproc.writeDataToArff(arffOutputPath)
			render "Wrote ARFF to: " + arffOutputPath
		}
		catch (Exception e) {
			e.printStackTrace()
			render e.toString()
		}
	}
	
	def phon() {
		try {
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
	
	def map() {
		try {
			PhoneticSymbolMapper mapper = PhoneticSymbolMapperFactory.createPhoneticSymbolMapper(language);
			Map ipaSampa = mapper.getIPAtoSAMPAmap()
			
			String output = "<h3>German Phonetic Symbols</h3>"
			output += "<table>"
			output += """<tr>
							<td>IPA</td>
							<td>SAMPA</td>
						</tr>"""
			
			SortedSet<String> ipas = new TreeSet<String>(ipaSampa.keySet());
			for (String ipa in ipas) {
				output += "<tr>"
				output += "<td>" + ipa + "</td>"
				output += "<td>" + mapper.IPAtoSAMPA(ipa) + "</td>"
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
