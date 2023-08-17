package com.sound.recognition.adapters.recording;

import com.sound.recognition.exceptions.SoundRecordingException;

public interface AudioRecorder {
    void recordAndSave(long recordingDuration) throws SoundRecordingException;
}
