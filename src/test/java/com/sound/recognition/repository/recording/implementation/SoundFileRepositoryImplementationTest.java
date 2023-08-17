package com.sound.recognition.repository.recording.implementation;

import com.sound.recognition.entities.tensorflow.observer.SoundRecordingObserver;
import com.sound.recognition.exceptions.SoundSaveException;
import com.sound.recognition.services.AudioSystemService;
import com.sound.recognition.services.MixerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SoundFileRepositoryImplementationTest {

    private SoundFileRepositoryImplementation repository;
    private MixerService mixerService;
    private AudioSystemService audioSystemService;
    private SoundRecordingObserver soundRecordingObserver;

    @BeforeEach
    void setUp() {
        mixerService = mock(MixerService.class);
        audioSystemService = mock(AudioSystemService.class);
        repository = new SoundFileRepositoryImplementation(mixerService, audioSystemService);
        soundRecordingObserver = mock(SoundRecordingObserver.class);
    }

    @Test
    void testSave() throws IOException, SoundSaveException {
        byte[] mockAudioData = new byte[]{0, 1, 2};
        AudioFormat mockFormat = new AudioFormat(44100, 16, 2, true, false);
        ByteArrayInputStream mockByteStream = new ByteArrayInputStream(mockAudioData);
        AudioInputStream mockAudioStream = new AudioInputStream(mockByteStream, mockFormat, mockAudioData.length);

        when(mixerService.getSupportedFormat()).thenReturn(mockFormat);
        when(audioSystemService.getAudioInputStream(any(), any(), anyLong())).thenReturn(mockAudioStream);

        repository.save(mockAudioData);

        verify(audioSystemService, times(1)).write(any(), any(), any());
    }

    @Test
    void testObserver() {
        repository.addObserver(soundRecordingObserver);
        repository.notifyObservers();

        verify(soundRecordingObserver, times(1)).update(null);
    }

    // ... (previous imports and setup)

    @Test
    void testObserverRemoval() {
        repository.addObserver(soundRecordingObserver);
        repository.removeObserver(soundRecordingObserver);
        repository.notifyObservers();

        // SoundRecordingObserver should not be notified after removal
        verify(soundRecordingObserver, never()).update(null);
    }

    @Test
    void testMultipleObservers() {
        SoundRecordingObserver secondSoundRecordingObserver = mock(SoundRecordingObserver.class);

        repository.addObserver(soundRecordingObserver);
        repository.addObserver(secondSoundRecordingObserver);
        repository.notifyObservers();

        // Both observers should be notified
        verify(soundRecordingObserver, times(1)).update(null);
        verify(secondSoundRecordingObserver, times(1)).update(null);
    }

    @Test
    void testIOExceptionWhileSaving() throws IOException {
        byte[] mockAudioData = new byte[]{0, 1, 2};
        when(mixerService.getSupportedFormat()).thenReturn(null);

        assertThrows(SoundSaveException.class, () -> {
            repository.save(mockAudioData);
        });
    }


}
