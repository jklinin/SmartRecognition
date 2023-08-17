package com.sound.recognition.adapters.implementation;

import com.sound.recognition.adapters.AudioRecorder;
import com.sound.recognition.exceptions.SoundRecordingException;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class JavaSoundAPIRecorder implements AudioRecorder {

    public static final String SOUND_DEVICE_NAME = "USB Audio Device";

    @Override
    public void recordAndSave(String filePath) throws SoundRecordingException {

        Mixer.Info desiredMixerInfo = getDesiredMixerInfo();
        if (desiredMixerInfo == null) {
            throw new SoundRecordingException("Device not founded");
        }

        Mixer mixer = AudioSystem.getMixer(desiredMixerInfo);

        AudioFormat chosenFormat = getSupportedFormat(mixer);
        if (chosenFormat == null) {
            throw new SoundRecordingException("No formats founded");
        }

        DataLine.Info newDataLineInfo = new DataLine.Info(TargetDataLine.class, chosenFormat);
        TargetDataLine targetDataLine;
        try {
            targetDataLine = (TargetDataLine) mixer.getLine(newDataLineInfo);
            targetDataLine.open(chosenFormat);
        } catch (LineUnavailableException e) {
            throw new SoundRecordingException("Line is unavailable.");
        }

        System.out.println("Start recording...");
        targetDataLine.start();

        Thread stopper = new Thread(() -> {
            try (AudioInputStream audioStream = new AudioInputStream(targetDataLine)) {
                File audioFile = new File(filePath);
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stopper.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        targetDataLine.stop();

        try {
            stopper.join();  // TODO Ensure the saving process completes before closing the line.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        targetDataLine.close();
        System.out.println("Recording stopped. File saved as " + filePath);
    }

    private Mixer.Info getDesiredMixerInfo() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfos) {
            if (info.getName().equals(SOUND_DEVICE_NAME)) {
                return info;
            }
        }
        return null;
    }

    private AudioFormat getSupportedFormat(Mixer mixer) {
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, null);
        Line.Info[] targetLineInfos = mixer.getTargetLineInfo(dataLineInfo);
        for (Line.Info targetLineInfo : targetLineInfos) {
            if (targetLineInfo instanceof DataLine.Info) {
                AudioFormat[] supportedFormats = ((DataLine.Info) targetLineInfo).getFormats();
                if (supportedFormats.length > 0) {
                    return supportedFormats[6];  // TODO Make sure you have more than 6 formats, else it'll throw an exception.
                }
            }
        }
        return null;
    }
}
