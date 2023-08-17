package com.sound.recognition.services.implementation;

import com.sound.recognition.services.AudioSystemService;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioSystemServiceImplementation implements AudioSystemService {
    public static final String SOUND_DEVICE_NAME = "USB Audio Device";

    @Override
    public Mixer.Info getMixerInfo() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfos) {
            if (info.getName().equals(SOUND_DEVICE_NAME)) {
                return info;
            }
        }
        return null;
    }


    @Override
    public AudioInputStream getAudioInputStream(InputStream inputStream, AudioFormat format, long length) {
        return new AudioInputStream(inputStream, format, length / format.getFrameSize());

    }

    @Override
    public void write(AudioInputStream stream, AudioFileFormat.Type fileType, File output) throws IOException {
        AudioSystem.write(stream, fileType, output);
    }
}
