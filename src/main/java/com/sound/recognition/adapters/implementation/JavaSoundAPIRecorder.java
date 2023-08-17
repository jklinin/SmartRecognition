package com.sound.recognition.adapters.implementation;

import com.sound.recognition.adapters.AudioRecorder;
import com.sound.recognition.exceptions.SoundRecordingException;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class JavaSoundAPIRecorder implements AudioRecorder {

    public static final String SOUND_DEVICE_NAME = "USB Audio Device";
    private static final long RECORDING_DURATION_MILLIS = 10_000;  // 10 seconds


    @Override
    public void recordAndSave(String filePath) throws SoundRecordingException {

        Mixer.Info desiredMixerInfo = getDesiredMixerInfo();
        if (desiredMixerInfo == null) {
            throw new SoundRecordingException("Device not found");
        }

        Mixer mixer = AudioSystem.getMixer(desiredMixerInfo);
        AudioFormat chosenFormat = getSupportedFormat(mixer);
        if (chosenFormat == null) {
            throw new SoundRecordingException("No supported formats found");
        }

        DataLine.Info newDataLineInfo = new DataLine.Info(TargetDataLine.class, chosenFormat);

        try (
                TargetDataLine targetDataLine = (TargetDataLine) mixer.getLine(newDataLineInfo);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream()
        ) {
            targetDataLine.open(chosenFormat);
            System.out.println("Start recording...");
            targetDataLine.start();

            byte[] buffer = new byte[chosenFormat.getSampleSizeInBits() * 1024];
            int bytesRead;
            long endTimeMillis = System.currentTimeMillis() + RECORDING_DURATION_MILLIS;

            while (System.currentTimeMillis() < endTimeMillis) {
                bytesRead = targetDataLine.read(buffer, 0, buffer.length);
                outStream.write(buffer, 0, bytesRead);
            }

            byte[] audioBytes = outStream.toByteArray();
            try (ByteArrayInputStream inStream = new ByteArrayInputStream(audioBytes);
                 AudioInputStream audioStream = new AudioInputStream(inStream, chosenFormat, audioBytes.length / chosenFormat.getFrameSize())) {
                File audioFile = new File(filePath);
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
            }

            System.out.println("Recording stopped. File saved as " + filePath);

        } catch (LineUnavailableException e) {
            throw new SoundRecordingException("Line is unavailable.", e);
        } catch (IOException e) {
            throw new SoundRecordingException("Error saving the audio file.", e);
        }
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
