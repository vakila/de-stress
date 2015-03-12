package org.ifcasl.destress

import fr.loria.parole.jsnoori.util.lang.Language;

// String wavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus"
// String gridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids"
String dataDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"
String FGcsv = dataDir + "FG-consolidated-NEW.csv"
String GGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/GG/GG-annotation.csv"
String bothCsv = dataDir + "GG-FG_extracted.csv"


Language language = Language.getLanguage("de")


private extractToArff(String csvName, String arffName) {
	// String wavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus"
	// String gridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids"
	//String dataDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"

	println "Extracting CSV: " + csvName
	println "To ARFF: " + arffName
	println ""


	DataProcessor dataproc = getDataProc(csvName)
	// println "Creating DataProcessor object..."
	// DataProcessor dataproc = new DataProcessor(csvName)
	// println "Done."

	extractData(dataproc)
	// def errors = dataproc.extractNewFeatures(wavDir, gridDir)
	//
	// println errors.size() + " Errors: "
	// for (e in errors) {
	// 	println e
	// }


	convertToArff(dataproc, arffName)
	// println "Writing data to ARFF..."
	// dataproc.writeDataToArff(arffName)
	// println "Done."

	println "Done with extraction."
}

private convertCsvToArff(csvName, arffName) {
	println "Converting"
	println "From CSV: " + csvName
	println "To  ARFF: " + arffName
	println ""
	DataProcessor dataproc = getDataProc(csvName)
	convertToArff(dataproc, arffName)
	println "Done with conversion."
}

private DataProcessor getDataProc(csvName) {
	println "Creating DataProcessor object for: "+ csvName
	DataProcessor dataproc = new DataProcessor(csvName)
	println "Done."
	return dataproc
}

private convertToArff(dataproc, arffName) {
	println "Writing data to ARFF: " + arffName
	dataproc.writeDataToArff(arffName)
	println "Done."
}


private extractData(dataProcObj) {
	String wavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus"
	String gridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids"
	def errors = dataProcObj.extractNewFeatures(wavDir, gridDir)

	println errors.size() + " Errors: "
	for (e in errors) {
		println e
	}
}

/////////////////////////////////////////////////////

//extractToArff(FGcsv, dataDir+"FG_extracted.arff")
//extractToArff(GGcsv, dataDir+"GG_extracted.arff")

//extractToArff(bothCsv, dataDir + "GG-FG_extracted.arff")

//DataProcessor.arffToCsv(dataDir+"FG_GG-extracted.arff", dataDir+"FG_GG-extracted.csv")
//DataProcessor.arffToCsv(dataDir+"FG_extracted.arff", dataDir+"FG_extracted.csv")
//DataProcessor.arffToCsv(dataDir+"GG_extracted.arff", dataDir+"GG_extracted.csv")
//DON'T USE THIS ----- DataProcessor.csvToArff(dataDir+"GG-FG_extracted.csv", dataDir+"GG-FG_extracted.arff")

//DataProcessor.arffToCsv(dataDir+"FG_randomized.arff", dataDir+"FG_randomized.csv")

convertCsvToArff(dataDir+"splits/random/FGGG-random.csv", dataDir+"splits/random/FGGG-random.arff")

// For splitting FG data into 10 different files
// for (int i = 1; i<=10; i++) {
// 	def splitDir = dataDir + "split_FG/"
// 	def trainFile = splitDir + "FG-train-" + i.toString() + "-of-10"
// 	def testFile = splitDir + "FG-test-" + i.toString() + "-of-10"
// 	println trainFile
// 	println testFile
// 	DataProcessor.arffToCsv(trainFile+".arff", trainFile+".csv")
// 	DataProcessor.arffToCsv(testFile+".arff", testFile+".csv")
// }
// for (int i = 1; i<=10; i++) {
// 	def splitDir = dataDir + "split_GGFG/"
// 	def trainFile = splitDir + "GGFG-train-" + i.toString() + "-of-10"
// 	def testFile = splitDir + "GGFG-test-" + i.toString() + "-of-10"
// 	println trainFile
// 	println testFile
// 	//DataProcessor.csvToArff(trainFile+".csv", trainFile+".arff")
// 	//DataProcessor.csvToArff(testFile+".csv", testFile+".arff")
// 	extractToArff(trainFile+".csv", trainFile+".arff")
// 	extractToArff(testFile+".csv", testFile+".arff")
// }
