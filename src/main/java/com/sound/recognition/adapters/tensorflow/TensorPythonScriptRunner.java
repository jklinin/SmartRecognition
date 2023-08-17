package com.sound.recognition.adapters.tensorflow;

import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;

public interface TensorPythonScriptRunner {
    AnalysisResult runTensorFlowAnalysis(SoundFile soundFile);
}
