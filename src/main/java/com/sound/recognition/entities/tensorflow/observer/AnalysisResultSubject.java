package com.sound.recognition.entities.tensorflow.observer;

import com.sound.recognition.entities.tensorflow.AnalysisResult;

public interface AnalysisResultSubject {
    void addResultObserver(AnalysisResultObserver o);
    void removeResultObserver(AnalysisResultObserver o);
    void notifyResultObservers(AnalysisResult result);
}
