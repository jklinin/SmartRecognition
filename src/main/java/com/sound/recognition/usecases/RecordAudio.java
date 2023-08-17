package com.sound.recognition.usecases;

import com.sound.recognition.entities.AudioRecording;
import com.sound.recognition.repository.AudioRepository;

public class RecordAudio {

    private final AudioRepository audioRepository;

    public RecordAudio(AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }

    public AudioRecording execute(String filePath) {
        AudioRecording recording = new AudioRecording(filePath);
        audioRepository.save(recording);
        return recording;
    }
}
