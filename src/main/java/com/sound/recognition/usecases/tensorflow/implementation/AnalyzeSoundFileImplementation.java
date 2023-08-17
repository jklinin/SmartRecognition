package com.sound.recognition.usecases.tensorflow.implementation;

import com.sound.recognition.adapters.tensorflow.TensorPythonScriptRunner;
import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;
import com.sound.recognition.usecases.tensorflow.AnalyzeSoundFile;

public class AnalyzeSoundFileImplementation implements AnalyzeSoundFile {
    private TensorPythonScriptRunner adapter;

    @Override
    public AnalysisResult analyze(SoundFile soundFile) {
        return adapter.runTensorFlowAnalysis(soundFile);
    }
}
