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
<<<<<<< HEAD
	Instances data
		
=======
	String outputFile
	//CSVMapReader mapReader
	//CSVWriter writer
	List mapList
	Set columns
	
	Instances data
	
>>>>>>> dd30d00542e6f173327b59fbfa3c8008d4ad699a

	public DataProcessor(String inputCsvPath) {
		this.inputCsv = inputCsvPath
		this.data = loadCsvWeka()
	}
	
	public String toString() {
		String output = "<p>DataProcessor object - " 
		output += this.inputCsv 
		//output += " - " 
		//output += this.mapReader.toList()[0]
		output += "</p>"	
		output += "<p>Header of dataset:</p>"
		output += (new Instances(this.data, 0)).toString();
		return output
	}
	
<<<<<<< HEAD
	public Instances loadCsvWeka() {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(this.inputCsv));
		return loader.getDataSet();
=======
	public String loadCsvWeka() {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(this.inputCsv));
		this.data = loader.getDataSet();
	
		String report = "<p>Header of dataset:</p>"
		report += (new Instances(this.data, 0)).toString();
		
		return report
>>>>>>> dd30d00542e6f173327b59fbfa3c8008d4ad699a
	}

}
