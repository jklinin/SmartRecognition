package com.sound.recognition.repository.recording.implementation;

import com.sound.recognition.entities.tensorflow.observer.SoundRecordingObserver;
import com.sound.recognition.exceptions.SoundSaveException;
import com.sound.recognition.repository.recording.SoundFileRepository;
import com.sound.recognition.services.AudioSystemService;
import com.sound.recognition.services.MixerService;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoundFileRepositoryImplementation implements SoundFileRepository {

    private final AudioFormat chosenFormat;
    private final AudioSystemService audioSystemService;
    private List<SoundRecordingObserver> soundRecordingObservers = new ArrayList<>();

    public SoundFileRepositoryImplementation(MixerService mixerService, AudioSystemService audioSystemService) {

        chosenFormat = mixerService.getSupportedFormat();
        this.audioSystemService = audioSystemService;
    }

    @Override
    public void save(byte[] audioData) throws SoundSaveException {
        if (chosenFormat == null) {
            throw new SoundSaveException("ChosenFormat is null");
        }

        String fileName = UUID.randomUUID() + ".wav";
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(audioData);
             AudioInputStream audioStream = audioSystemService.getAudioInputStream(inStream, chosenFormat, audioData.length)) {
            File audioFile = new File(fileName);
            audioSystemService.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
        } catch (IOException e) {
            throw new SoundSaveException(e);
        }
        notifyObservers();
    }



    @Override
    public void addObserver(SoundRecordingObserver o) {
        soundRecordingObservers.add(o);
    }

    @Override
    public void removeObserver(SoundRecordingObserver o) {
        soundRecordingObservers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (SoundRecordingObserver o : soundRecordingObservers) {
            o.update(null);
        }
    }
}