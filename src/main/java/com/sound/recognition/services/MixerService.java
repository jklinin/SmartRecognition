package com.sound.recognition.services;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public interface MixerService {
    AudioFormat getSupportedFormat();
    TargetDataLine getLine(DataLine.Info info) throws LineUnavailableException;
}
