package com.sound.recognition.adapters.implementation;

import com.sound.recognition.adapters.AudioRecorder;
import com.sound.recognition.exceptions.SoundRecordingException;
import com.sound.recognition.services.AudioSystemService;
import com.sound.recognition.services.MixerService;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFileFormat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class JavaSoundAPIRecorder implements AudioRecorder {
    private static final long RECORDING_DURATION_MILLIS = 10_000;  // 10 seconds
    private AudioSystemService audioSystemService;
    private MixerService mixerService;

    public JavaSoundAPIRecorder(AudioSystemService audioSystemService, MixerService mixerService) {
        this.audioSystemService = audioSystemService;
        this.mixerService = mixerService;
    }

    @Override
    public void recordAndSave(String filePath) throws SoundRecordingException {

        Mixer.Info desiredMixerInfo = audioSystemService.getMixerInfo();
        if (desiredMixerInfo == null) {
            throw new SoundRecordingException("Device not found");
        }
        
        AudioFormat chosenFormat = mixerService.getSupportedFormat();
        if (chosenFormat == null) {
            throw new SoundRecordingException("No supported formats found");
        }

        DataLine.Info newDataLineInfo = new DataLine.Info(TargetDataLine.class, chosenFormat);

        try (
                TargetDataLine targetDataLine = mixerService.getLine(newDataLineInfo);
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
                 AudioInputStream audioStream = audioSystemService.getAudioInputStream(inStream, chosenFormat, audioBytes.length)) {
                File audioFile = new File(filePath);
                audioSystemService.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
            }

            System.out.println("Recording stopped. File saved as " + filePath);

        } catch (LineUnavailableException e) {
            throw new SoundRecordingException("Line is unavailable.", e);
        } catch (
                IOException e) {
            throw new SoundRecordingException("Error saving the audio file.", e);
        }
    }


}
