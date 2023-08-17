package com.sound.recognition.services.implementation;

import com.sound.recognition.services.AudioSystemService;
import com.sound.recognition.services.MixerService;

import javax.sound.sampled.*;

public class MixerServiceImplemementation implements MixerService {
    private Mixer mixer;

    public MixerServiceImplemementation(AudioSystemService audioSystemService) {
        Mixer.Info desiredMixerInfo = audioSystemService.getMixerInfo();
        mixer = AudioSystem.getMixer(desiredMixerInfo);
    }

    @Override
    public AudioFormat getSupportedFormat() {
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, null);
        Line.Info[] targetLineInfos = mixer.getTargetLineInfo(dataLineInfo);
        for (Line.Info targetLineInfo : targetLineInfos) {
            if (targetLineInfo instanceof DataLine.Info) {
                AudioFormat[] supportedFormats = ((DataLine.Info) targetLineInfo).getFormats();
                if (supportedFormats.length > 0) {
                    return supportedFormats[6];  // TODO Make sure you have more than 6 formats, else it'll throw an exception.
                }
            }
        }
        return null;
    }


    @Override
    public TargetDataLine getLine(DataLine.Info info) throws LineUnavailableException {
        return (TargetDataLine) mixer.getLine(info);
    }
}
