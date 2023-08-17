package com.sound.recognition.exceptions;


public class SoundSaveException extends Exception {
    public SoundSaveException(Exception e) {
        super(e);
    }

    public SoundSaveException(String message) {
        super(message);
    }
}
