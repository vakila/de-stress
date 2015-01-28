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
	String outputFile
	//CSVMapReader mapReader
	//CSVWriter writer
	List mapList
	Set columns
	
	Instances data
	

	public DataProcessor(String inputCsvPath) {
		this.inputCsv = inputCsvPath
		def mapReader = new File(inputCsvPath).toCsvMapReader()
		this.mapList = mapReader.toList()
		this.columns = this.mapList[0].keySet()
	}
	
	public String toString() {
		String output = "<p>DataProcessor object - " 
		output += this.inputCsv 
		//output += " - " 
		//output += this.mapReader.toList()[0]
		output += "</p><p>"	
		output += this.columns.toString()
		output += "</p>"
		return output
	}
	
	public String loadCsvWeka() {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(this.inputCsv));
		this.data = loader.getDataSet();
	
		String report = "<p>Header of dataset:</p>"
		report += (new Instances(this.data, 0)).toString();
		
		return report
	}

}
