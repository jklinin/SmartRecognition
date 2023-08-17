package com.sound.recognition.repository.implementation;

import com.sound.recognition.entities.AudioRecording;
import com.sound.recognition.repository.AudioRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAudioRepository implements AudioRepository {

    private final List<AudioRecording> audioRecordings = new ArrayList<>();

    @Override
    public void save(AudioRecording audioRecording) {
        audioRecordings.add(audioRecording);
    }
}
