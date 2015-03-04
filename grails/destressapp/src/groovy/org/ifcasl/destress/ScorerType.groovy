package org.ifcasl.destress

enum ScorerType {
    JSNOORI('JSNOORI'),
    WEKA('WEKA'),
    CUSTOM('CUSTOM')

    // JSNOORI > Uses scores from Time/Pitch/EnergyFeedback (enables resynthesis)
    // WEKA    > Classifies utterance as Weka instance
    // CUSTOM  > Uses FeatureExtractor and Duration/F0/IntensityScorers

    String value

    ScorerType(value) {
        this.value = value
    }
}
