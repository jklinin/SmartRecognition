package com.sound.recognition.repository;

import com.sound.recognition.entities.AudioRecording;

public interface AudioRepository {
    void save(AudioRecording audioRecording);
}