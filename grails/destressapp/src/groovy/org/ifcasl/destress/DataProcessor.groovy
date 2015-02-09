package org.ifcasl.destress

//import org.grails.plugins.csv.CSVMapReader
//import org.grails.plugins.csv.CSVWriter

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances

import weka.core.converters.ArffSaver
import weka.core.converters.CSVLoader

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal

//import FeatureExtractor


class DataProcessor {
	
	static final NumericToNominal NUMTONOM = getNumToNomFilter()
	
	String inputCsv
	Instances originalData
	Instances data
	
	//NumericToNominal numToNom
	//String outputFile
	
	
	//CSVMapReader mapReader
	//CSVWriter writer
	//List mapList
	//Set columns
		

	public DataProcessor(String inputCsvPath) {
		this.inputCsv = inputCsvPath
		this.originalData = loadCsv(inputCsvPath)
		NUMTONOM.setInputFormat(this.originalData)
		this.data = Filter.useFilter(this.originalData, NUMTONOM)
		
	}
	
	/**
	 * Creates a NumericToNominal Filter object that converts
	 * certain Attributes in the data to nominal values.
	 * Attributes converted ([index] name):
	 *     [4] SPEAKER
	 * @return
	 */
	public static NumericToNominal getNumToNomFilter() {
		String[] options = new String[2];
		options[0] = "-R"
		options[1] = "4" //indices of Attributes to convert
		NumericToNominal filter = new NumericToNominal()
		filter.setOptions(options)
		return filter
	}
	
	
//	public HashMap<String, Attribute> getAttributes() {
//		HashMap<String, Attribute> attributes = new HashMap<String, Attribute>
//		for (Attribute a : this.data.enumerateAttributes()) {
//			attributes.put(a.name, a)
//		}
//		return attributes;
//	}


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
	
	/**
	 * Reads a Weka Instances object from the given CSV file
	 * @param inputFilePath
	 * @return
	 */
	public Instances loadCsv(String inputFilePath) {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(inputFilePath));
		return loader.getDataSet();
	}

	/**
	 * Writes the given Weka Instances object to ARFF file
	 * @param dataToWrite
	 * @param outputFilePath
	 */
	public void writeArff(Instances dataToWrite, String outputFilePath) {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataToWrite);
		saver.setFile(new File(outputFilePath));
		//saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		saver.writeBatch();
	}
	
	/**
	 * Writes the Weka Instances object this.data to ARFF file
	 * @param outputFilePath
	 */
	public void writeArff(String outputFilePath) {
		writeArff(this.data, outputFilePath)
	}

	/**
	 * Adds a numeric attribute with the given name 
	 * to the Instances object this.data. 
	 * The attribute is inserted at the penultimate position,
	 * i.e. before the class attribute.
	 * @param inputData
	 * @param attrName
	 */
	public void addNumAttribute(String attrName) {
		//Instances dataOut = new Instances(dataIn)
		this.data.insertAttributeAt(new Attribute(attrName), this.data.numAttributes()-1)
	}
	
	/**
	 * Returns the file name (e.g. 2SH02_FGMA2_502) for a given instance
	 * @param row	Weka Instance object
	 * @return
	 */
	public String getFileName(Instance row) {
		Attribute sentence = this.data.attribute("SENTENCE")
		Attribute speakerL1 = this.data.attribute("SPEAKER_L1")
		Attribute speakerGen = this.data.attribute("SPEAKER_GENDER")
		Attribute speakerLevel = this.data.attribute("SPEAKER_LEVEL")
		Attribute speakerID = this.data.attribute("SPEAKER")
		
		String chunk1 = "2" + row.stringValue(sentence)
		String chunk2 = row.stringValue(speakerL1) + "G" + row.stringValue(speakerGen) + row.stringValue(speakerLevel)
		String chunk3 = row.stringValue(speakerID)
		
		String filename = chunk1 + "_" + chunk2 + "_" + chunk3
		return filename
	}
	
	/**
	 * Returns HTML code displaying the names for every 
	 * Instance (row) in this.data
	 * @return
	 */
	public String printFileNames() {
		int n = data.numInstances();
		String output = ""
		for (int i in 0..n-1) {
			Instance inst = this.data.instance(i)
			output += "<p>" + getFileName(inst) + "</p>"
		}
		return output
	}
	
	/**
	 * Returns a string containing the filenames for 
	 * n rows (Instances) randomly selected from this.data
	 * @param n		Number of instances
	 * @return
	 */
	public String printRandomFileNames(int n) {
		Instances randomData = new Instances(this.data)
		randomData.randomize(this.data.getRandomNumberGenerator(n))
		Instances smallData = new Instances(randomData, n)
		
		String output = ""
		for (int i in 0..smallData.numInstances()) {
			Instance inst = this.data.instance(i)
			output += "<p>" + getFileName(inst) + "</p>"
		} 
		return output
	}
	
	/**
	 * Massive function that will:
	 * - Add extra attributes to the dataset
	 * - TODO remove useless attributes from the dataset
	 * - Use FeatureExtractor objects to set each Instance's values for the new Attributes
	 * @param wavDir
	 * @param gridDir
	 */
	public void extractNewFeatures(String wavDir, String gridDir) {
		this.addNumAttribute("WORD_DUR")
		this.addNumAttribute("SYLL2/SYLL1")
		this.addNumAttribute("V2/V1")
		
		Attribute wordDurAttr = this.data.attribute("WORD_DUR")
		Attribute relSyllDurAttr = this.data.attribute("SYLL2/SYLL1")
		Attribute relVowelDurAttr = this.data.attribute("V2/V1")
		Attribute wordAttr = this.data.attribute("WORD")
		
		for (Instance inst in this.data.enumerateInstances()) {
			String word = inst.stringValue("WORD")
			String fileName = getFileName(inst)
			String wavName = [wavDir, fileName, ".wav"].join(File.separator)
			String gridName = [gridDir, fileName, ".textgrid"].join(File.separator)
			FeatureExtractor featex = new FeatureExtractor(wavName, gridName, word)
		}
	}
}
