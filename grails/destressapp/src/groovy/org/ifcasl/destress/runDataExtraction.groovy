package org.ifcasl.destress

import fr.loria.parole.jsnoori.util.lang.Language;


String FGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FG-consolidated.csv"
String GGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/GG/GG-annotation.csv"
String bothCsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FG_GG-original.csv"

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

//extractToArff(bothCsv, "FG_GG-extracted.arff")

String dataDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"
//DataProcessor.arffToCsv(dataDir+"FG_GG-extracted.arff", dataDir+"FG_GG-extracted.csv")
//DataProcessor.arffToCsv(dataDir+"FG_extracted.arff", dataDir+"FG_extracted.csv")
//DataProcessor.arffToCsv(dataDir+"GG_extracted.arff", dataDir+"GG_extracted.csv")
//DataProcessor.csvToArff(dataDir+"FG_GG-extracted.csv", dataDir+"test.arff")

for (int i = 1; i<=10; i++) {
	def splitDir = dataDir + "FG_split/"
	def trainFile = splitDir + "FG-train-" + i.toString() + "-of-10"
	def testFile = splitDir + "FG-test-" + i.toString() + "-of-10"
	println trainFile
	println testFile
	DataProcessor.arffToCsv(trainFile+".arff", trainFile+".csv")
	DataProcessor.arffToCsv(testFile+".arff", testFile+".csv")
}
