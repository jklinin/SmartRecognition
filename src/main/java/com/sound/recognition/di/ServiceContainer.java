package com.sound.recognition.di;


import com.sound.recognition.adapters.AudioRecorder;
import com.sound.recognition.adapters.implementation.JavaSoundAPIRecorder;
import com.sound.recognition.repository.AudioRepository;
import com.sound.recognition.repository.implementation.InMemoryAudioRepository;
import com.sound.recognition.services.implementation.AudioSystemServiceImplementation;
import com.sound.recognition.services.implementation.MixerServiceImplemementation;
import com.sound.recognition.usecases.RecordAudio;

public class ServiceContainer {

    private AudioRecorder audioRecorder;
    private AudioRepository audioRepository;
    private RecordAudio recordAudio;

    public ServiceContainer() {
        AudioSystemServiceImplementation audioSystemService = new AudioSystemServiceImplementation();
        MixerServiceImplemementation mixerService = new MixerServiceImplemementation(audioSystemService);

        this.audioRecorder = new JavaSoundAPIRecorder(audioSystemService, mixerService);
        this.audioRepository = new InMemoryAudioRepository();
        this.recordAudio = new RecordAudio(audioRepository);
    }

    public AudioRecorder getAudioRecorder() {
        return audioRecorder;
    }

    public RecordAudio getRecordAudio() {
        return recordAudio;
    }
}
