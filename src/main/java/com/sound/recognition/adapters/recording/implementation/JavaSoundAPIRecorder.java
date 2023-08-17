package com.sound.recognition.adapters.recording.implementation;

import com.sound.recognition.adapters.recording.AudioRecorder;
import com.sound.recognition.exceptions.SoundRecordingException;
import com.sound.recognition.exceptions.SoundSaveException;
import com.sound.recognition.repository.recording.SoundFileRepository;
import com.sound.recognition.services.AudioSystemService;
import com.sound.recognition.services.MixerService;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JavaSoundAPIRecorder implements AudioRecorder {
    private final SoundFileRepository soundFileRepository;
    private AudioSystemService audioSystemService;
    private MixerService mixerService;

    public JavaSoundAPIRecorder(AudioSystemService audioSystemService, MixerService mixerService, SoundFileRepository soundFileRepository) {
        this.audioSystemService = audioSystemService;
        this.mixerService = mixerService;
        this.soundFileRepository = soundFileRepository;
    }

    @Override
    public void recordAndSave(long recordingDuration) throws SoundRecordingException {

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
            long endTimeMillis = System.currentTimeMillis() + recordingDuration;

            while (System.currentTimeMillis() < endTimeMillis) {
                bytesRead = targetDataLine.read(buffer, 0, buffer.length);
                outStream.write(buffer, 0, bytesRead);
            }

            byte[] audioBytes = outStream.toByteArray();

            soundFileRepository.save(audioBytes);

            System.out.println("Recording stopped. File saved.");

        } catch (LineUnavailableException e) {
            throw new SoundRecordingException("Line is unavailable.", e);
        } catch (IOException e) {
            throw new SoundRecordingException("Error saving the audio file.", e);
        } catch (SoundSaveException e) {
            throw new SoundRecordingException("Can't save sound file", e);
        }
    }


}
