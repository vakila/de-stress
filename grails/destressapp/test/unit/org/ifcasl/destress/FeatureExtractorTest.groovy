package org.ifcasl.destress;

import groovy.util.GroovyTestCase
import static org.junit.Assert.*;

//import org.junit.Before;
//import org.junit.Test;

//import grails.test.mixin.*
//import spock.lang.*


class FeatureExtractorTest extends GroovyTestCase {

	String wavFile = "/Users/Anjana/Dropbox/School/IFCASL/viwoll/CompleteAudioCorpus/FG/SR/SR31/2SR31_FGMB2_525.wav"
	String gridFile = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Textgrids/FG/SR/SR31/2SR31_FGMB2_525.textgrid"
	FeatureExtractor featex
	
	
	public void setUp() throws Exception {
		featex = new FeatureExtractor(wavFile, gridFile, "tatort")
		
	}

	
	public void test() {
		assert featex.getWordDuration() > 0
		println "wordDuration: " + featex.getWordDuration()
		assert featex.getVowelDurationInWord() > 0
		println "vowelDurationInWord: " + featex.getVowelDurationInWord()
		
		assert featex.getSyllableDuration(0) > 0
		println "syllableDuration(0): " + featex.getSyllableDuration(0)
		assert featex.getSyllableDuration(1) > 0
		println "syllableDuration(1): " + featex.getSyllableDuration(1)
		
		
		assert featex.getVowelDurationInSyllable(0) > 0
		println "vowelDurationInSyllable(0): " + featex.getVowelDurationInSyllable(0)
		assert featex.getVowelDurationInSyllable(1) > 0
		println "vowelDurationInSyllable(1): " + featex.getVowelDurationInSyllable(1)
		
		assert featex.getWordF0Mean() > 0 
		println "wordF0Mean: " + featex.getWordF0Mean()
		assert featex.getWordF0Max() > 0
		println "wordF0Max: " + featex.getWordF0Max()
		assert featex.getWordF0Min() > 0 
		println "wordF0Min: " + featex.getWordF0Min()
		assert featex.getWordF0Range() > 0
		println "wordF0Range: " + featex.getWordF0Range()
		
		assert featex.getSyllableF0Mean(0) > 0
		assert featex.getSyllableF0Max(0) > 0
		assert featex.getSyllableF0Min(0) > 0
		assert featex.getSyllableF0Range(0) > 0
		println "syllableF0Mean(0): " + featex.getSyllableF0Mean(0)
		println "syllableF0Max(0): " + featex.getSyllableF0Max(0)
		println "syllableF0Min(0): " + featex.getSyllableF0Min(0)
		println "syllableF0Range(0): " + featex.getSyllableF0Range(0)
		
		assert featex.getSyllableF0Mean(1) > 0
		assert featex.getSyllableF0Max(1) > 0
		assert featex.getSyllableF0Min(1) > 0
		assert featex.getSyllableF0Range(1) > 0
		println "syllableF0Mean(1): " + featex.getSyllableF0Mean(1)
		println "syllableF0Max(1): " + featex.getSyllableF0Max(1)
		println "syllableF0Min(1): " + featex.getSyllableF0Min(1)
		println "syllableF0Range(1): " + featex.getSyllableF0Range(1)
		
		assertNotNull(featex.getMaxF0Index())
		println "maxF0Index: " + featex.getMaxF0Index()
		assertNotNull(featex.getMinF0Index())
		println "minF0Index: " + featex.getMinF0Index()
		assertNotNull(featex.getMaxRangeF0Index())
		println "maxRangeF0Index: " + featex.getMaxRangeF0Index()
		
	}
	
	
}
