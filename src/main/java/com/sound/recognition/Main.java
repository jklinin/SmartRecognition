package com.sound.recognition;

import com.sound.recognition.di.ServiceContainer;
import com.sound.recognition.exceptions.SoundRecordingException;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        ServiceContainer container = new ServiceContainer();
        Scanner scanner = new Scanner(System.in);
        AtomicInteger fileCounter = new AtomicInteger(1);

        AtomicBoolean isRecording = new AtomicBoolean(true);

        System.out.println("Recording started... Type 'stop' and press Enter to stop recording.");

        Thread recordingThread = new Thread(() -> {
            try {
                while (isRecording.get()) {
                    String filePath = "aufnahme_" + fileCounter.getAndIncrement() + ".wav";
                    try {
                        container.getAudioRecorder().recordAndSave(filePath);
                        System.out.println("Recording saved at: " + filePath);
                    } catch (SoundRecordingException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ThreadDeath td) {
                throw td;  // Rethrow ThreadDeath
            }
        });

        recordingThread.start();

        // Wait for the user to type "stop"
        while (true) {
            String input = scanner.nextLine();
            if ("stop".equalsIgnoreCase(input)) {
                isRecording.set(false);
                try {
                    recordingThread.join(); // Wait for the recording thread to finish
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    break;
                }
                break;
            } else {
                System.out.println("Invalid command. Type 'stop' to end recording.");
            }
        }

        System.out.println("Recording stopped.");
    }
}
