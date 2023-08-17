package com.sound.recognition.entities.recording;

public class SoundFile {
    private final String filePath;

    public SoundFile(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
