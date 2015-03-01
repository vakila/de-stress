package org.ifcasl.destress

class SentenceUtterance {

    Speaker speaker

    String sentence

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
    Float f0Max
    Float f0Min

    // intensity
    Float intensityMean
    Float intensityMax

    static constraints = {
        sentence()
        speaker()
        waveFile()
        gridFile()
        dateCreated()
        totalDuration(blank:true,nullable:true)
        speakingDuration(blank:true,nullable:true)
        speakingRate(blank:true,nullable:true)
        f0Mean(blank:true,nullable:true)
        f0Max(blank:true,nullable:true)
        f0Min(blank:true,nullable:true)
        intensityMean(blank:true,nullable:true)
        intensityMax(blank:true,nullable:true)

    }

    String toString() {
        return sentence + "_" + speaker.speakerNumber
    }
}
