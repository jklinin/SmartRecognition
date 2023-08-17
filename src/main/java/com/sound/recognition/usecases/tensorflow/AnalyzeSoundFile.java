package com.sound.recognition.usecases.tensorflow;

import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;

public interface AnalyzeSoundFile {
    AnalysisResult analyze(SoundFile soundFile);
}
