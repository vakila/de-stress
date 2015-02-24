package org.ifcasl.destress

//import org.grails.plugins.csv.CSVMapReader
//import org.grails.plugins.csv.CSVWriter

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.core.converters.ArffSaver
import weka.core.converters.ArffLoader
import weka.core.converters.CSVSaver
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
		renameSpeakerValues()
		
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
	
	/**
	 * Converts the given ARFF file to a CSV file with the given name
	 * @param arffFile
	 * @param csvFile
	 * @return
	 */
	public static arffToCsv(String arffFile, String csvFile) {
		ArffLoader loader = new ArffLoader()
		loader.setSource(new File(arffFile))
		Instances data = loader.getDataSet()
		CSVSaver saver = new CSVSaver()
		saver.setInstances(data)
		saver.setFile(new File(csvFile))
		saver.writeBatch()
	}
	
	/**
	 * Converts the given CSV file to an ARFF file with the given name
	 * @param csvFile
	 * @param arffFile
	 * @return
	 */
	public static csvToArff(String csvFile, String arffFile) {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(csvFile)); 
		Instances data = loader.getDataSet();
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(arffFile));
		saver.writeBatch();
	}
	
	public static combineArffs(String[] arffFiles) {
		//TODO - thesis-code#66
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
	
	public void renameSpeakerValues() {
		Attribute SPEAKER = this.data.attribute("SPEAKER")
		for (int i = 0; i < SPEAKER.numValues(); i++) {
			def val_i = SPEAKER.value(i)
			this.data.renameAttributeValue(SPEAKER, val_i, val_i.padLeft(3, "0"))
		}
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
		String chunk3 = row.stringValue(speakerID).padLeft(3,'0')
		
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
	 * - Use FeatureExtractor objects to set each Instance's values for the new Attributes
	 * @param wavDir
	 * @param gridDir
	 */
	public List extractNewFeatures(String wavDir, String gridDir) {
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
		Attribute REL_SYLL_DUR = this.addNumAttribute("REL_SYLL_DUR") // SYLL0/SYLL1
		Attribute REL_V_DUR = this.addNumAttribute("REL_V_DUR")
		
		//// F0
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
		//Vowels
		Attribute V0_F0_MEAN = this.addNumAttribute("V0_F0_MEAN")
		Attribute V0_F0_MAX = this.addNumAttribute("V0_F0_MAX")
		Attribute V0_F0_MIN = this.addNumAttribute("V0_F0_MIN")
		Attribute V0_F0_RANGE = this.addNumAttribute("V0_F0_RANGE")
		Attribute V1_F0_MEAN = this.addNumAttribute("V1_F0_MEAN")
		Attribute V1_F0_MAX = this.addNumAttribute("V1_F0_MAX")
		Attribute V1_F0_MIN = this.addNumAttribute("V1_F0_MIN")
		Attribute V1_F0_RANGE = this.addNumAttribute("V1_F0_RANGE")
		//Relative
		Attribute REL_SYLL_F0_MEAN = this.addNumAttribute("REL_SYLL_F0_MEAN")
		Attribute REL_SYLL_F0_MAX = this.addNumAttribute("REL_SYLL_F0_MAX")
		Attribute REL_SYLL_F0_MIN = this.addNumAttribute("REL_SYLL_F0_MIN")
		Attribute REL_SYLL_F0_RANGE = this.addNumAttribute("REL_SYLL_F0_RANGE")
		Attribute REL_VOWEL_F0_MEAN = this.addNumAttribute("REL_VOWEL_F0_MEAN")
		Attribute REL_VOWEL_F0_MAX = this.addNumAttribute("REL_VOWEL_F0_MAX")
		Attribute REL_VOWEL_F0_MIN = this.addNumAttribute("REL_VOWEL_F0_MIN")
		Attribute REL_VOWEL_F0_RANGE = this.addNumAttribute("REL_VOWEL_F0_RANGE")
		Attribute F0_MAX_INDEX = this.addNumAttribute("F0_MAX_INDEX")
		Attribute F0_MIN_INDEX = this.addNumAttribute("F0_MIN_INDEX")
		Attribute F0_MAXRANGE_INDEX = this.addNumAttribute("F0_MAXRANGE_INDEX")
		
		//// Energy
		//Word
		Attribute WORD_ENERGY_MEAN = this.addNumAttribute("WORD_ENERGY_MEAN")
		Attribute WORD_ENERGY_MAX = this.addNumAttribute("WORD_ENERGY_MAX")
		//Syllables
		Attribute SYLL0_ENERGY_MEAN = this.addNumAttribute("SYLL0_ENERGY_MEAN")
		Attribute SYLL0_ENERGY_MAX = this.addNumAttribute("SYLL0_ENERGY_MAX")
		Attribute SYLL1_ENERGY_MEAN = this.addNumAttribute("SYLL1_ENERGY_MEAN")
		Attribute SYLL1_ENERGY_MAX = this.addNumAttribute("SYLL1_ENERGY_MAX")
		//Vowels
		Attribute V0_ENERGY_MEAN = this.addNumAttribute("V0_ENERGY_MEAN")
		Attribute V0_ENERGY_MAX = this.addNumAttribute("V0_ENERGY_MAX")
		Attribute V1_ENERGY_MEAN = this.addNumAttribute("V1_ENERGY_MEAN")
		Attribute V1_ENERGY_MAX = this.addNumAttribute("V1_ENERGY_MAX")	
		//Relative
		Attribute REL_SYLL_ENERGY_MEAN = this.addNumAttribute("REL_SYLL_ENERGY_MEAN")
		Attribute REL_SYLL_ENERGY_MAX = this.addNumAttribute("REL_SYLL_ENERGY_MAX")
		Attribute REL_VOWEL_ENERGY_MEAN = this.addNumAttribute("REL_VOWEL_ENERGY_MEAN")
		Attribute REL_VOWEL_ENERGY_MAX = this.addNumAttribute("REL_VOWEL_ENERGY_MAX")
		Attribute ENERGY_MAX_INDEX = this.addNumAttribute("ENERGY_MAX_INDEX")
		
		println "Attributes added."
		println ""
		
		println "Current attributes:"
		for (Attribute attr in this.data.enumerateAttributes()) {
			println attr.name()
		}
		println ""
		
		println "Beginning Instance iteration..."
		Attribute WORD = this.data.attribute("WORD")
		Attribute SENTENCE_TYPE = this.data.attribute("SENTENCE_TYPE")
		Attribute SENTENCE = this.data.attribute("SENTENCE")
		//Attribute SPEAKER = this.data.attribute("SPEAKER")
		Attribute SPEAKER_L1 = this.data.attribute("SPEAKER_L1")
		
		def errors = []
		
		for (Instance inst in this.data.enumerateInstances()) {
			
			//// Get this instance's files & create FeatureExtractor
			String wordText = inst.stringValue(WORD)
			String sentType = inst.stringValue(SENTENCE_TYPE)
			String sentence = inst.stringValue(SENTENCE)
			String fileName = getFileName(inst)
			//println "Instance: " + fileName
			
//			if (errors.contains(fileName)) {
//				println "Skipping instance to avoid error."
//				continue
//			}
			
			String langPair = inst.stringValue(SPEAKER_L1) + "G"
			String wavName = [wavDir, langPair, sentType, sentence, fileName+".wav"].join(File.separator)
			String gridName = [gridDir, langPair, sentType, sentence, fileName+".textgrid"].join(File.separator)
			
			//assert new File(wavName).exists()
			//assert new File(gridName).exists()
			for (f in [wavName, gridName]) {
				if (! new File(f).exists()) {
					println fileName + " - " + wordText +  " ERROR: File missing - " + f
					errors.add(fileName + " - " + wordText + " ----- File missing - " + f)
				}
			}

			
			def featex
			
			try {
				featex = new FeatureExtractor(wavName, gridName, wordText)

			} catch (Exception e) {
				println fileName + " - " + wordText +  " ERROR: couldn't create FeatureExtractor"
				e.printStackTrace()
				errors.add(fileName + " - " + wordText + " ----- couldn't create FeatureExtractor - " + e.message)
				continue
			}
		
		
			try {
				//// Duration 
				//println "Adding duration features..."
				inst.setValue(WORD_DUR, featex.getWordDuration())
				inst.setValue(SYLL0_DUR, featex.getSyllableDuration(0))
				inst.setValue(SYLL1_DUR, featex.getSyllableDuration(1))
				inst.setValue(REL_SYLL_DUR, featex.getRelSyllDuration())
				inst.setValue(V0_DUR, featex.getVowelDurationInSyllable(0))
				inst.setValue(V1_DUR, featex.getVowelDurationInSyllable(1))
				inst.setValue(REL_V_DUR, featex.getRelVowelDuration())
				//println "Done."
			} catch (Exception e) {
				println fileName + " - " + wordText + " ERROR: couldn't extract Duration features"
				e.printStackTrace()
				errors.add(fileName + " - " + wordText + " ----- couldn't extract Duration features - " + e.message)
				continue
			}
			
			try {
				//// F0 
				//println "Adding F0 features..."
				// Word
				inst.setValue(WORD_F0_MEAN, featex.getWordF0Mean())
				inst.setValue(WORD_F0_MAX, featex.getWordF0Max())
				inst.setValue(WORD_F0_MIN, featex.getWordF0Min())
				inst.setValue(WORD_F0_RANGE, featex.getWordF0Range())
				// Syllables
				inst.setValue(SYLL0_F0_MEAN, featex.getSyllableF0Mean(0))
				inst.setValue(SYLL0_F0_MAX, featex.getSyllableF0Max(0))
				inst.setValue(SYLL0_F0_MIN, featex.getSyllableF0Min(0))
				inst.setValue(SYLL0_F0_RANGE, featex.getSyllableF0Range(0))
				inst.setValue(SYLL1_F0_MEAN, featex.getSyllableF0Mean(1))
				inst.setValue(SYLL1_F0_MAX, featex.getSyllableF0Max(1))
				inst.setValue(SYLL1_F0_MIN, featex.getSyllableF0Min(1))
				inst.setValue(SYLL1_F0_RANGE, featex.getSyllableF0Range(1))
				inst.setValue(V0_F0_MEAN, featex.getVowelF0Mean(0))
				inst.setValue(V0_F0_MAX, featex.getVowelF0Max(0))
				inst.setValue(V0_F0_MIN, featex.getVowelF0Min(0))
				inst.setValue(V0_F0_RANGE, featex.getVowelF0Range(0))
				inst.setValue(V1_F0_MEAN, featex.getVowelF0Mean(1))
				inst.setValue(V1_F0_MAX, featex.getVowelF0Max(1))
				inst.setValue(V1_F0_MIN, featex.getVowelF0Min(1))
				inst.setValue(V1_F0_RANGE, featex.getVowelF0Range(1))
				// Relative
				inst.setValue(REL_SYLL_F0_MEAN, featex.getRelSyllF0Mean())
				inst.setValue(REL_SYLL_F0_MAX, featex.getRelSyllF0Max())
				inst.setValue(REL_SYLL_F0_MIN, featex.getRelSyllF0Min())
				inst.setValue(REL_SYLL_F0_RANGE, featex.getRelSyllF0Range())
				inst.setValue(REL_VOWEL_F0_MEAN, featex.getRelVowelF0Mean())
				inst.setValue(REL_VOWEL_F0_MAX, featex.getRelVowelF0Max())
				inst.setValue(REL_VOWEL_F0_MIN, featex.getRelVowelF0Min())
				inst.setValue(REL_VOWEL_F0_RANGE, featex.getRelVowelF0Range())
				inst.setValue(F0_MAX_INDEX, featex.getMaxF0Index())
				inst.setValue(F0_MIN_INDEX, featex.getMinF0Index())
				inst.setValue(F0_MAXRANGE_INDEX, featex.getMaxRangeF0Index())
				//println "Done."
			} catch (Exception e) {
				println fileName + " - " + wordText + " ERROR: couldn't extract F0 features"
				e.printStackTrace()
				errors.add(fileName + " - " + wordText + " ----- couldn't extract F0 features - " + e.message)
			}
			
			
			//Energy 
			try {
				//Word
				inst.setValue(WORD_ENERGY_MEAN, featex.getWordEnergyMean())
				inst.setValue(WORD_ENERGY_MAX, featex.getWordEnergyMax())
				//Syllables
				inst.setValue(SYLL0_ENERGY_MEAN, featex.getSyllableEnergyMean(0))
				inst.setValue(SYLL0_ENERGY_MAX, featex.getSyllableEnergyMax(0))
				inst.setValue(SYLL1_ENERGY_MEAN, featex.getSyllableEnergyMean(1))
				inst.setValue(SYLL1_ENERGY_MAX, featex.getSyllableEnergyMax(1))
				//VOWELS
				inst.setValue(V0_ENERGY_MEAN, featex.getVowelEnergyMean(0))
				inst.setValue(V0_ENERGY_MAX, featex.getVowelEnergyMax(0))
				inst.setValue(V1_ENERGY_MEAN, featex.getVowelEnergyMean(1))
				inst.setValue(V1_ENERGY_MAX, featex.getVowelEnergyMax(1))
				//Relative
				inst.setValue(REL_SYLL_ENERGY_MEAN, featex.getRelSyllEnergyMean())
				inst.setValue(REL_SYLL_ENERGY_MAX, featex.getRelSyllEnergyMax())
				inst.setValue(REL_VOWEL_ENERGY_MEAN, featex.getRelVowelEnergyMean())
				inst.setValue(REL_VOWEL_ENERGY_MAX, featex.getRelVowelEnergyMax())
				inst.setValue(ENERGY_MAX_INDEX, featex.getMaxEnergyIndex())
			} catch (Exception e) {
				println fileName + " - " + wordText + " ERROR: couldn't extract Energy features"
				e.printStackTrace()
				errors.add(fileName + " - " + wordText + " ----- couldn't extract Energy features - " + e.message)
			
			}
			
		} //end for loop over instances
		println "Done with Instance iteration."
		
//		println "Saving this.data to file..."
//		writeArff("DATA_extractnewfeatures.arff")
//		println "Done."
		
		return errors
	}
}
