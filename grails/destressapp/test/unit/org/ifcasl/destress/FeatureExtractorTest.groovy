package org.ifcasl.destress;

import groovy.util.GroovyTestCase
import static org.junit.Assert.*;

//import org.junit.Before;
//import org.junit.Test;

//import grails.test.mixin.*
//import spock.lang.*


class FeatureExtractorTest extends GroovyTestCase {

	String wavDir = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/"
	String gridDir = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/"
	
	String wavFile = wavDir + "FG/SR/SR31/2SR31_FGMB2_525.wav" //2SR31_FGWB2_510.wav" //
	String gridFile = gridDir +"FG/SR/SR31/2SR31_FGMB2_525.textgrid" //2SR31_FGWB2_510.textgrid" //
	String word = "tatort" //SR31
	
	//String wavFile = wavDir + "FG/SR/SR15/2SR15_FGMC1_545.wav"
	//String gridFile = gridDir + "FG/SR/SR15/2SR15_FGMC1_545.textgrid"
	//String word = "halten" //SR15
	
	FeatureExtractor featex
	
	
	public void setUp() throws Exception {
		featex = new FeatureExtractor(wavFile, gridFile, word)
		assert featex.wordSegment != null
	}

	
//	public void testDuration() {
//		//Word
//		assert featex.getWordDuration() > 0
//		println "wordDuration: " + featex.getWordDuration()
//		assert featex.getVowelDurationInWord() > 0
//		println "vowelDurationInWord: " + featex.getVowelDurationInWord()
//		
//		//Syllables
//		assert featex.getSyllableDuration(0) > 0
//		println "syllableDuration(0): " + featex.getSyllableDuration(0)
//		assert featex.getSyllableDuration(1) > 0
//		println "syllableDuration(1): " + featex.getSyllableDuration(1)
//		
//		//Vowels
//		assert featex.getVowelDurationInSyllable(0) > 0
//		println "vowelDurationInSyllable(0): " + featex.getVowelDurationInSyllable(0)
//		assert featex.getVowelDurationInSyllable(1) > 0
//		println "vowelDurationInSyllable(1): " + featex.getVowelDurationInSyllable(1)
//		
//		assert featex.getRelSyllDuration() > 0
//		println "relSyllDuration: " + featex.getRelSyllDuration()
//		assert featex.getRelVowelDuration() > 0
//		println "relVowelDuration: " + featex.getRelVowelDuration()
//	}
//	
//	public void testF0() {
//		
//		//Word
//		assert featex.getWordF0Mean() > 0 
//		println "wordF0Mean: " + featex.getWordF0Mean()
//		assert featex.getWordF0Max() > 0
//		println "wordF0Max: " + featex.getWordF0Max()
//		assert featex.getWordF0Min() > 0 
//		println "wordF0Min: " + featex.getWordF0Min()
//		assert featex.getWordF0Range() > 0
//		println "wordF0Range: " + featex.getWordF0Range()
//		
//		//Syllables
//		assert featex.getSyllableF0Mean(0) > 0
//		println "syllableF0Mean(0): " + featex.getSyllableF0Mean(0)
//		assert featex.getSyllableF0Max(0) > 0
//		println "syllableF0Max(0): " + featex.getSyllableF0Max(0)
//		assert featex.getSyllableF0Min(0) > 0
//		println "syllableF0Min(0): " + featex.getSyllableF0Min(0)
//		assert featex.getSyllableF0Range(0) > 0
//		println "syllableF0Range(0): " + featex.getSyllableF0Range(0)
//		assert featex.getSyllableF0Mean(1) > 0
//		println "syllableF0Mean(1): " + featex.getSyllableF0Mean(1)
//		assert featex.getSyllableF0Max(1) > 0
//		println "syllableF0Max(1): " + featex.getSyllableF0Max(1)
//		assert featex.getSyllableF0Min(1) > 0
//		println "syllableF0Min(1): " + featex.getSyllableF0Min(1)
//		assert featex.getSyllableF0Range(1) > 0
//		println "syllableF0Range(1): " + featex.getSyllableF0Range(1)
//		
//		//Vowels
//		assert featex.getVowelF0Mean(0) > 0
//		println "vowelF0Mean(0): " + featex.getVowelF0Mean(0)
//		assert featex.getVowelF0Max(0) > 0
//		println "vowelF0Max(0): " + featex.getVowelF0Max(0)
//		assert featex.getVowelF0Min(0) > 0
//		println "vowelF0Min(0): " + featex.getVowelF0Min(0)
//		assert featex.getVowelF0Range(0) > 0
//		println "vowelF0Range(0): " + featex.getVowelF0Range(0)
//		assert featex.getVowelF0Mean(1) > 0
//		println "vowelF0Mean(1): " + featex.getVowelF0Mean(1)
//		assert featex.getVowelF0Max(1) > 0
//		println "vowelF0Max(1): " + featex.getVowelF0Max(1)
//		assert featex.getVowelF0Min(1) > 0
//		println "vowelF0Min(1): " + featex.getVowelF0Min(1)
//		assert featex.getVowelF0Range(1) > 0
//		println "vowelF0Range(1): " + featex.getVowelF0Range(1)
//		
//		//Relative
//		assert featex.getRelSyllF0Mean() > 0
//		println "relSyllF0Mean: " + featex.getRelSyllF0Mean()
//		assert featex.getRelSyllF0Max() > 0
//		println "relSyllF0Max: " + featex.getRelSyllF0Max()
//		assert featex.getRelSyllF0Min() > 0
//		println "relSyllF0Min: " + featex.getRelSyllF0Min()
//		assert featex.getRelSyllF0Range() > 0
//		println "relSyllF0Range: " + featex.getRelSyllF0Range()
//		assert featex.getRelVowelF0Mean() > 0
//		println "relVowelF0Mean: " + featex.getRelVowelF0Mean()
//		assert featex.getRelVowelF0Max() > 0
//		println "relVowelF0Max: " + featex.getRelVowelF0Max()
//		assert featex.getRelVowelF0Min() > 0
//		println "relVowelF0Min: " + featex.getRelVowelF0Min()
//		assert featex.getRelVowelF0Range() > 0
//		println "relVowelF0Range: " + featex.getRelVowelF0Range()
//		assertNotNull(featex.getMaxF0Index())
//		println "maxF0Index: " + featex.getMaxF0Index()
//		assertNotNull(featex.getMinF0Index())
//		println "minF0Index: " + featex.getMinF0Index()
//		assertNotNull(featex.getMaxRangeF0Index())
//		println "maxRangeF0Index: " + featex.getMaxRangeF0Index()
//		
//	}
	
	public void testEnergy() {
		
		//Word
		assert featex.getWordEnergyMean() > 0
		println "wordEnergyMean: " + featex.getWordEnergyMean()
		assert featex.getWordEnergyMax() > 0
		println "wordEnergyMax: " + featex.getWordEnergyMax()
		
		//Syllables
		assert featex.getSyllableEnergyMean(0) > 0
		println "syllableEnergyMean(0): " + featex.getSyllableEnergyMean(0)
		assert featex.getSyllableEnergyMax(0) > 0
		println "syllableEnergyMax(0): " + featex.getSyllableEnergyMax(0)
		assert featex.getSyllableEnergyMean(1) > 0
		println "syllableEnergyMean(1): " + featex.getSyllableEnergyMean(1)
		assert featex.getSyllableEnergyMax(1) > 0
		println "syllableEnergyMax(1): " + featex.getSyllableEnergyMax(1)
		
		//Vowels
		assert featex.getVowelEnergyMean(0) > 0
		println "vowelEnergyMean(0): " + featex.getVowelEnergyMean(0)
		assert featex.getVowelEnergyMax(0) > 0
		println "vowelEnergyMax(0): " + featex.getVowelEnergyMax(0)
		assert featex.getVowelEnergyMean(1) > 0
		println "vowelEnergyMean(1): " + featex.getVowelEnergyMean(1)
		assert featex.getVowelEnergyMax(1) > 0
		println "vowelEnergyMax(1): " + featex.getVowelEnergyMax(1)
		
		//Relative
		assert featex.getRelSyllEnergyMean() > 0
		println "relSyllEnergyMean: " + featex.getRelSyllEnergyMean()
		assert featex.getRelSyllEnergyMax() > 0
		println "relSyllEnergyMax: " + featex.getRelSyllEnergyMax()
		assert featex.getRelVowelEnergyMean() > 0
		println "relVowelEnergyMean: " + featex.getRelVowelEnergyMean()
		assert featex.getRelVowelEnergyMax() > 0
		println "relVowelEnergyMax: " + featex.getRelVowelEnergyMax()
		assertNotNull(featex.getMaxEnergyIndex())
		println "maxEnergyIndex: " + featex.getMaxEnergyIndex()
		
	}
	
	
}
