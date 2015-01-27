package org.ifcasl.destress

import org.grails.plugins.csv.CSVMapReader
import org.grails.plugins.csv.CSVWriter

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


class DataProcessor {
	
	String inputCsv
	String outputFile
	//CSVMapReader mapReader
	//CSVWriter writer
	List mapList
	Set columns
	

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

}
