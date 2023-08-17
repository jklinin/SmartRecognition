package com.sound.recognition.services;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Mixer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface AudioSystemService {

    Mixer.Info getMixerInfo();

    AudioInputStream getAudioInputStream(InputStream inputStream, AudioFormat format, long length);

    void write(AudioInputStream stream, AudioFileFormat.Type fileType, File output) throws IOException;
}
