package com.sound.recognition;

import com.sound.recognition.di.ServiceContainer;
import com.sound.recognition.entities.AudioRecording;
import com.sound.recognition.exceptions.SoundRecordingException;

public class Main {
    public static void main(String[] args) {
        ServiceContainer container = new ServiceContainer();

        String filePath = "aufnahme.wav";

        // Use the audio recorder to record and save the audio
        try {
            container.getAudioRecorder().recordAndSave(filePath);
        } catch (SoundRecordingException e) {
           e.printStackTrace();
        }

        // Use the use case to handle business logic and persist the audio data
        AudioRecording recording = container.getRecordAudio().execute(filePath);
        System.out.println("Recording saved at: " + recording.getFilePath());
    }
}
