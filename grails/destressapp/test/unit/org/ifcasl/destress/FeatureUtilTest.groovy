package org.ifcasl.destress;

import groovy.util.GroovyTestCase
import static org.junit.Assert.*


class FeatureExtractorTest extends GroovyTestCase {

    WordUtterance w1 = Word.get(1)

    public void setUp() throws Exception {

	}


    public void testWordExtraction() throws Exception {
        FeatureUtil.extractWordUtteranceFeatures(w1)
        assertNotNull(wordUtt.WORD_DUR)
        assertNotNull(wordUtt.SYLL0_DUR)
        assertNotNull(wordUtt.SYLL1_DUR)
        assertNotNull(wordUtt.SYLL0_F0_MEAN)
        assertNotNull(wordUtt.SYLL1_F0_MEAN)
        assertNotNull(wordUtt.SYLL0_F0_RANGE)
        assertNotNull(wordUtt.SYLL1_F0_RANGE)
    }

}
