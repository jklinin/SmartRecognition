package com.sound.recognition;

import com.sound.recognition.di.ServiceContainer;
import com.sound.recognition.exceptions.SoundRecordingException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * The <code>Main</code> class serves as the entry point for the Sound Recognition System.
 * <p>
 * This class initializes the system, allowing users to define the duration of audio recording
 * either through command line arguments or user input. If no duration is provided through these methods,
 * a default recording duration is used. The class supports continuous recording, creating new files
 * with unique names for each iteration. The recording process can be stopped by the user by typing
 * 'stop' into the console.
 * </p>
 *
 * <h2>Usage</h2>
 * <pre>
 * java Main [recording_duration]
 * </pre>
 * <p>
 * where <code>[recording_duration]</code> is an optional argument specifying the length of each audio recording
 * in milliseconds. If not provided, the system will prompt the user for input or use the default duration.
 * </p>
 *
 */
public class Main {
    private static final long DEFAULT_DURATION_MILLIS = 10 * 1000; // 10 seconds
    private static final long MIN_DURATION_MILLIS = 10; // 10 seconds
    private static final long MAX_DURATION_MILLIS = 60; // 60 seconds
    private static long recording_duration;

    public static void main(String[] args) {
        ServiceContainer container = new ServiceContainer();
        AtomicInteger fileCounter = new AtomicInteger(1);
        AtomicBoolean isRecording = new AtomicBoolean(true);

        if (args.length > 0) {
            try {
                recording_duration = Long.parseLong(args[0]);

                if (recording_duration < MIN_DURATION_MILLIS || recording_duration > MAX_DURATION_MILLIS) {
                    throw new NumberFormatException();
                }
                recording_duration = recording_duration * 1000;
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument. Using default duration.");
                recording_duration = DEFAULT_DURATION_MILLIS;
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please set recording duration in seconds (between 10 and 60): ");
            while (true) {
                try {
                    recording_duration = Long.parseLong(scanner.nextLine());

                    if (recording_duration < MIN_DURATION_MILLIS || recording_duration > MAX_DURATION_MILLIS) {
                        throw new NumberFormatException();
                    }
                    recording_duration = recording_duration * 1000;
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please set recording duration between 10 seconds and 60 seconds: ");
                }
            }
        }

        System.out.println("Recording started with duration: " + recording_duration + "ms. Type 'stop' and press Enter to stop recording.");

        Thread recordingThread = new Thread(() -> {
            try {
                while (isRecording.get()) {
                    String filePath = "aufnahme_" + fileCounter.getAndIncrement() + ".wav";
                    try {
                        container.getAudioRecorder().recordAndSave(filePath, recording_duration);
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
        Scanner stopScanner = new Scanner(System.in);
        while (true) {
            String input = stopScanner.nextLine();
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
