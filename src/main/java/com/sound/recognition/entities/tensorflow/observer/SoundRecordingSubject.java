package com.sound.recognition.entities.tensorflow.observer;

public interface SoundRecordingSubject {
    void addObserver(SoundRecordingObserver o);
    void removeObserver(SoundRecordingObserver o);
    void notifyObservers();
}
