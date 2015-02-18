package org.ifcasl.destress

//import org.grails.plugins.csv.CSVMapReader
//import org.grails.plugins.csv.CSVWriter

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

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
	 * @param attrName	Name of the new Attribute
	 * @return			The new Attribute
	 */
	public Attribute addNumAttribute(String attrName) {
		//Instances dataOut = new Instances(dataIn)
		this.data.insertAttributeAt(new Attribute(attrName), this.data.numAttributes()-1)
		return this.data.attribute(attrName)
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
		println "Extracting New Features..."
		println "wavDir: " + wavDir
		println "gridDir: " + gridDir
		println ""
		
		println "Adding attributes..."
		
		//// Duration
		Attribute WORD_DUR = this.addNumAttribute("WORD_DUR")
		Attribute SYLL0_DUR = this.addNumAttribute("SYLL0_DUR")
		Attribute SYLL1_DUR = this.addNumAttribute("SYLL1_DUR")
		Attribute V0_DUR = this.addNumAttribute("V0_DUR")
		Attribute V1_DUR = this.addNumAttribute("V1_DUR")
		Attribute SYLL_REL_DUR = this.addNumAttribute("SYLL_REL_DUR") // SYLL0/SYLL1
		Attribute V_REL_DUR = this.addNumAttribute("V_REL_DUR")
		
		////TODO Pitch
		//Word
		Attribute WORD_F0_MEAN = this.addNumAttribute("WORD_F0_MEAN")
		Attribute WORD_F0_MAX = this.addNumAttribute("WORD_F0_MAX")
		Attribute WORD_F0_MIN = this.addNumAttribute("WORD_F0_MIN")
		Attribute WORD_F0_RANGE = this.addNumAttribute("WORD_F0_RANGE")
		//Syllables
		Attribute SYLL0_F0_MEAN = this.addNumAttribute("SYLL0_F0_MEAN")
		Attribute SYLL0_F0_MAX = this.addNumAttribute("SYLL0_F0_MAX")
		Attribute SYLL0_F0_MIN = this.addNumAttribute("SYLL0_F0_MIN")
		Attribute SYLL0_F0_RANGE = this.addNumAttribute("SYLL0_F0_RANGE")
		Attribute SYLL1_F0_MEAN = this.addNumAttribute("SYLL1_F0_MEAN")
		Attribute SYLL1_F0_MAX = this.addNumAttribute("SYLL1_F0_MAX")
		Attribute SYLL1_F0_MIN = this.addNumAttribute("SYLL1_F0_MIN")
		Attribute SYLL1_F0_RANGE = this.addNumAttribute("SYLL1_F0_RANGE")
		//Relative
		Attribute SYLL_REL_MEAN = this.addNumAttribute("SYLL_REL_MEAN")
		Attribute SYLL_REL_MAX = this.addNumAttribute("SYLL_REL_MAX")
		Attribute SYLL_REL_MIN = this.addNumAttribute("SYLL_REL_MIN")
		Attribute SYLL_REL_RANGE = this.addNumAttribute("SYLL_REL_RANGE")
		
		////TODO Intensity
		
		println "Attributes added."
		println ""
		
		println "Current attributes:"
		for (Attribute attr in this.data.enumerateAttributes()) {
			println attr.name
		}
		println ""
		
		println "Beginning Instance iteration..."
		Attribute WORD = this.data.attribute("WORD")
		for (Instance inst in this.data.enumerateInstances()) {
			String wordText = inst.stringValue(WORD)
			String fileName = getFileName(inst)
			String wavName = [wavDir, fileName, ".wav"].join(File.separator)
			String gridName = [gridDir, fileName, ".textgrid"].join(File.separator)
			
			FeatureExtractor featex = new FeatureExtractor(wavName, gridName, wordText)
			
			//// Duration attributes
			inst.setValue(WORD_DUR, featex.getWordDuration())
			def syll0dur = featex.getSyllableDuration(0)
			def syll1dur = featex.getSyllableDuration(1)
			inst.setValue(SYLL0_DUR, syll0dur)
			inst.setValue(SYLL1_DUR, syll1dur)
			inst.setValue(SYLL_REL_DUR, syll0dur/syll1dur)
			def v0dur = featex.getVowelDurationInSyllable(0)
			def v1dur = featex.getVowelDurationInSyllable(1)
			inst.setValue(V0_DUR, v0dur)
			inst.setValue(V1_DUR, v1dur)
			inst.setValue(V_REL_DUR, v0dur/v1dur)
			
			//// Pitch attributes
			// Word
			inst.setValue(WORD_F0_MEAN, featex.getWordF0Mean())
			inst.setValue(WORD_F0_MAX, featex.getWordF0Max())
			inst.setValue(WORD_F0_MIN, featex.getWordF0Min())
			inst.setValue(WORD_F0_RANGE, featex.getWordF0Range())
			// Syllables
			def syll0seg = featex.extractedSylls.getSegment(0)
			def syll0f0mean = featex.pitchAnalysis.computePitchMeanInSegment(syll0seg)
			def syll0f0max = featex.pitchAnalysis.computePitchMaxInSegment(syll0seg)
			def syll0f0min = featex.pitchAnalysis.computePitchMinInSegment(syll0seg)
			def syll0f0range = syll0f0max - syll0f0min
			inst.setValue(SYLL0_F0_MEAN, syll0f0mean)
			inst.setValue(SYLL0_F0_MAX, syll0f0max)
			inst.setValue(SYLL0_F0_MIN, syll0f0min)
			inst.setValue(SYLL0_F0_RANGE, syll0f0range)
			def syll1seg = featex.extractedSylls.getSegment(1)
			def syll1f0mean = featex.pitchAnalysis.computePitchMeanInSegment(syll1seg)
			def syll1f0max = featex.pitchAnalysis.computePitchMaxInSegment(syll1seg)
			def syll1f0min = featex.pitchAnalysis.computePitchMinInSegment(syll1seg)
			def syll1f0range = syll1f0max - syll1f0min
			inst.setValue(SYLL1_F0_MEAN, syll1f0mean)
			inst.setValue(SYLL1_F0_MAX, syll1f0max)
			inst.setValue(SYLL1_F0_MIN, syll1f0min)
			inst.setValue(SYLL1_F0_RANGE, syll1f0range)
			// Relative
			inst.setValue(SYLL_REL_MEAN, syll0f0mean/syll1f0mean)
			inst.setValue(SYLL_REL_MAX, syll0f0max/syll1f0max)
			inst.setValue(SYLL_REL_MIN, syll0f0min/syll1f0min)
			inst.setValue(SYLL_REL_RANGE, syll0f0range/syll1f0range)
			
			//TODO Intensity attributes
		}
	}
}
