package com.sound.recognition.entities.tensorflow.observer;

import com.sound.recognition.entities.tensorflow.AnalysisResult;

public interface AnalysisResultObserver {
    void onAnalysisComplete(AnalysisResult result);
}
