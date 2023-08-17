package com.sound.recognition.exceptions;

import java.io.IOException;

public class SoundRecordingException extends Exception {
    public SoundRecordingException(String message) {
        super(message);
    }

    public SoundRecordingException(String message, Exception e) {
        super(message, e);
    }
}
