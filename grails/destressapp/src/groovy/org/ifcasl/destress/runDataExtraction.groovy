package org.ifcasl.destress

import fr.loria.parole.jsnoori.util.lang.Language;

String dataDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/"
String FGwavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/FG"
String GGwavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/GG"
String FGgridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/FG"
String GGgridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/GG"
String FGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Data/FG-consolidated.csv"
String GGcsv = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/GG/GG-annotation.csv"

Language language = Language.getLanguage("de")

println "Creating DataProcessor object..."
DataProcessor dataproc = new DataProcessor(FGcsv)
println "Done."

def errors = dataproc.extractNewFeatures(FGwavDir, FGgridDir)

println errors.size() + " Errors: "
for (e in errors) {
	println e
}

println "Writing data to ARFF..."
dataproc.writeArff(dataDir + "FG_extracted.arff")
println "Done."