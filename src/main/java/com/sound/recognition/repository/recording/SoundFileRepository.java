package com.sound.recognition.repository.recording;

import com.sound.recognition.entities.tensorflow.observer.Observer;
import com.sound.recognition.entities.tensorflow.observer.Subject;
import com.sound.recognition.exceptions.SoundSaveException;

public interface SoundFileRepository extends Subject {
    void save(byte[] audioData) throws SoundSaveException;


    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();
}