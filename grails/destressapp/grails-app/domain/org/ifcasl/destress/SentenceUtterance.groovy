package org.ifcasl.destress

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.FileUtils

class SentenceUtterance {

    static belongsTo = [speaker:Speaker]

    String sentence

    String sampleName

    String waveFile
    String gridFile

    Date dateCreated

    // features like overall duration, f0/intensity mean/max/min?
    // duration
    Float totalDuration
    Float speakingDuration
    Float speakingRate      //syllables/second?

    // f0
    Float f0Mean
    Float f0Range
    //Float f0Max
    //Float f0Min

    // intensity
    Float intensityMean
    Float intensityMax

    static constraints = {
        sentence()
        speaker()
        sampleName()
        waveFile()
        gridFile()
        dateCreated()
        totalDuration(blank:true,nullable:true)
        speakingDuration(blank:true,nullable:true)
        speakingRate(blank:true,nullable:true)
        f0Mean(blank:true,nullable:true)
        f0Range(blank:true,nullable:true)
        //f0Max(blank:true,nullable:true)
        //f0Min(blank:true,nullable:true)
        intensityMean(blank:true,nullable:true)
        intensityMax(blank:true,nullable:true)

    }

    String toString() {
        //return sentence + "_" + speaker.speakerNumber
        return sampleName
    }


    // // Handle files after saving/updating
    // def beforeInsert() {
    //     def grailsApplication = new SentenceUtterance().domainClass.grailsApplication
    //
    //     File inputFile = new File(waveFile)
    //     String wavName = FilenameUtils.getBaseName(waveFile) + FilenameUtils.getExtension(waveFile)
    //     //String savePath = grailsApplication.config.audioFolder + baseName
    //     String savePath = grailsApplication.mainContext.servletContext.getRealPath("/") + "audio/" + wavName
    //
    //     println "################# TESTING ###################"
    //     println "waveFile: " + waveFile
    //     println "baseName: " + baseName
    //     println "savePath: " + savePath
    //
    //     File outputFile = new File(savePath)
    //     if (!outputFile.exists()) FileUtils.copyFile(inputFile, outputFile)
    //     assert new File(waveFile).exists()
    //     assert outputFile.exists()
    //     println "FILE SAVED"
    // }
}
