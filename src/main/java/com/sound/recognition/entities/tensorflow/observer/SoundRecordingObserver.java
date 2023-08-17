package com.sound.recognition.entities.tensorflow.observer;

import com.sound.recognition.entities.recording.SoundFile;

public interface SoundRecordingObserver {
    void update(SoundFile soundFile);
}
