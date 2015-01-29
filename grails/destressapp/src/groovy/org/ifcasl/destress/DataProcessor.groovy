package org.ifcasl.destress

//import org.grails.plugins.csv.CSVMapReader
//import org.grails.plugins.csv.CSVWriter

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.core.converters.ArffSaver
import weka.core.converters.CSVLoader


class DataProcessor {
	
	String inputCsv
	Instances data
		
	//String outputFile
	
	
	//CSVMapReader mapReader
	//CSVWriter writer
	//List mapList
	//Set columns
	
	

	public DataProcessor(String inputCsvPath) {
		this.inputCsv = inputCsvPath
		this.data = loadCsv()
	}
	
	public String toString() {
		String output = "<p>DataProcessor object - " 
		output += this.inputCsv 
		//output += " - " 
		//output += this.mapReader.toList()[0]
		output += "</p>"	
		output += "<p>Header of dataset:</p>"
		output += "<p>" + (new Instances(this.data, 0)).toString() + "</p>"
		return output
	}
	
	public Instances loadCsv() {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(this.inputCsv));
		return loader.getDataSet();
	}

	public void writeArff(String outputFilePath) {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(this.data);
		saver.setFile(new File(outputFilePath));
		//saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		saver.writeBatch();
	}

}
