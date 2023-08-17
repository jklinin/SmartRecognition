package com.sound.recognition.repository.recording;

import com.sound.recognition.entities.tensorflow.observer.SoundRecordingObserver;
import com.sound.recognition.entities.tensorflow.observer.SoundRecordingSubject;
import com.sound.recognition.exceptions.SoundSaveException;

public interface SoundFileRepository extends SoundRecordingSubject {
    void save(byte[] audioData) throws SoundSaveException;


    void addObserver(SoundRecordingObserver o);

    void removeObserver(SoundRecordingObserver o);

    void notifyObservers();
}