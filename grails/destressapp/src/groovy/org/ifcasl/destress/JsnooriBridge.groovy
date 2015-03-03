package org.ifcasl.destress

import fr.loria.parole.jsnoori.model.teacher.feedback.*
import fr.loria.parole.jsnoori.model.audio.AudioSignal
import fr.loria.parole.jsnoori.util.lang.Language;
import fr.loria.parole.jsnoori.util.file.segmentation.TextGridSegmentationFileUtils
import fr.loria.parole.jsnoori.model.segmentation.*


static class JsnooriBridge{

    public static GERMAN = Language.getLanguage("de")


    public static FeedbackComputer getFeedbackComputer(Exercise ex, WordUtterance studUtt, WordUtterance refUtt) {
        // Get utterance files
        String trialName = //TODO
        String trialWave = //TODO
        String trialGrid = //TODO
        String exampleName = //TODO
        String exampleWave = //TODO
        String exampleGrid = //TODO

        // Get AudioSignals
        AudioSignal trialSignal = new AudioSignal(trialName, trialWave)
        AudioSignal exampleSignal = new AudioSignal(exampleName, exampleWave)

        // Set up stuff
        def trialStuff = [trialName, trialWave, trialGrid, trialSignal]
    	def exampleStuff = [exampleName, exampleWave, exampleGrid, exampleSignal]

        // Process AudioSignals & Segmentations
        for (stuff in [trialStuff, exampleStuff]) {
            String name = stuff[0]
			String wave = stuff[1]
			String grid = stuff[2]
			AudioSignal signal = stuff[3]

            // Set segmentations
            signal.openSegmentationFile(new TextGridSegmentationFileUtils(), grid)
            Segmentation syllSeg = signal.segmentationList.getSegmentation("SyllableTier")
			signal.segmentationList.setSyllablesSegmentation(syllSeg)
            Segmentation phonSeg = signal.segmentationList.getSegmentation("AlignTier")
			signal.segmentationList.setPhoneSegmentation(phonSeg)

            // Isolate word
            String word = ex.word.text
			Segment wordSeg = signal.segmentationList.getSegmentation("WordTier").getSegmentByName(word)
			signal.segmentationList.isolatePartialSegmentations(wordSeg)

        }

        // Get FeedbackComputer and run computeFeedback()
        def fbc = new FeedbackComputer(trialSignal, exampleSignal, GERMAN)
        fbc.computeFeedback()

        return fbc
    }

}
