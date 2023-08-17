package com.sound.recognition.adapters;

import com.sound.recognition.exceptions.SoundRecordingException;

public interface AudioRecorder {
    void recordAndSave(String filePath, long recording_duration) throws SoundRecordingException;
}
