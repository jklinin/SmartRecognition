package com.sound.recognition.adapters.implementation;

import com.sound.recognition.exceptions.SoundRecordingException;
import com.sound.recognition.services.AudioSystemService;
import com.sound.recognition.services.MixerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JavaSoundAPIRecorderTest {

    public static final int RECORDING_DURATION = 10_000;
    private JavaSoundAPIRecorder recorder;
    private AudioSystemService audioSystemMock;
    private MixerService mixerMock;

    @BeforeEach
    public void setUp() {
        audioSystemMock = mock(AudioSystemService.class);
        mixerMock = mock(MixerService.class);
        recorder = new JavaSoundAPIRecorder(audioSystemMock, mixerMock);
    }

    @Test
    void testRecordAndSave_deviceNotFound() {
        when(audioSystemMock.getMixerInfo()).thenReturn(null);

        assertThrows(SoundRecordingException.class, () -> recorder.recordAndSave("path/to/save", RECORDING_DURATION));
    }

    @Test
    void testRecordAndSave_noSupportedFormats() {
        when(audioSystemMock.getMixerInfo()).thenReturn(mock(Mixer.Info.class));
        when(mixerMock.getSupportedFormat()).thenReturn(null);

        assertThrows(SoundRecordingException.class, () -> recorder.recordAndSave("path/to/save", RECORDING_DURATION));
    }

    @Test
    void testRecordAndSave_lineUnavailable() throws Exception {
        setupMocksForSuccessfulRecording();
        when(mixerMock.getLine(any())).thenThrow(new LineUnavailableException());

        assertThrows(SoundRecordingException.class, () -> recorder.recordAndSave("path/to/save", RECORDING_DURATION));
    }

    @Test
    void testRecordAndSave_successfulRecording() throws Exception {
        setupMocksForSuccessfulRecording();

        recorder.recordAndSave("path/to/save", RECORDING_DURATION);

        // Verify that certain methods on your mocks were called, for example:
        verify(mixerMock, times(1)).getSupportedFormat();
        verify(audioSystemMock, times(1)).getMixerInfo();
        verify(mixerMock, times(1)).getSupportedFormat();
        verify(mixerMock, times(1)).getLine(any(DataLine.Info.class));

    }


    private void setupMocksForSuccessfulRecording() throws Exception {
        when(audioSystemMock.getMixerInfo()).thenReturn(mock(Mixer.Info.class));

        AudioFormat mockFormat = new AudioFormat(44100, 16, 1, true, true);
        when(mixerMock.getSupportedFormat()).thenReturn(mockFormat);

        TargetDataLine mockDataLine = mock(TargetDataLine.class);
        when(mixerMock.getLine(any())).thenReturn(mockDataLine);

        AudioInputStream mockAudioStream = mock(AudioInputStream.class);
        when(audioSystemMock.getAudioInputStream(any(), eq(mockFormat), anyInt())).thenReturn(mockAudioStream);
    }
}
