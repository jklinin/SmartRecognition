package com.sound.recognition.entities;

public class AudioRecording {
    private final String filePath;

    public AudioRecording(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
