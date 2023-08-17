package com.sound.recognition.entities.tensorflow;

public class AnalysisResult {
    private final String resultFromPython;

    //TODO implements
    public AnalysisResult(String resultFromPython) {
        this.resultFromPython=resultFromPython;
    }

    public String getResult() {
        return resultFromPython;
    }
}
