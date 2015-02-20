package org.ifcasl.destress

import fr.loria.parole.jsnoori.util.lang.Language;


String FGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FG-consolidated.csv"
String GGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/GG/GG-annotation.csv"
String bothCsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FG_GG.csv"

Language language = Language.getLanguage("de")


private extractToArff(String csvName, String arffName) {
	String wavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus"
	String gridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids"
	String dataDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"
	
	println "Creating DataProcessor object..."
	DataProcessor dataproc = new DataProcessor(csvName)
	println "Done."

	def errors = dataproc.extractNewFeatures(wavDir, gridDir)

	println errors.size() + " Errors: "
	for (e in errors) {
		println e
	}

	println "Writing data to ARFF..."
	dataproc.writeArff(dataDir + arffName)
	println "Done."
}

//extractToArff(FGcsv, "FG_extracted.arff")
//extractToArff(GGcsv, "GG_extracted.arff")

extractToArff(bothCsv, "FG_GG.arff")

