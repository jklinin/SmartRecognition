package com.sound.recognition.di;


import com.sound.recognition.adapters.AudioRecorder;
import com.sound.recognition.adapters.implementation.JavaSoundAPIRecorder;
import com.sound.recognition.repository.AudioRepository;
import com.sound.recognition.repository.implementation.InMemoryAudioRepository;
import com.sound.recognition.usecases.RecordAudio;

public class ServiceContainer {

    private AudioRecorder audioRecorder;
    private AudioRepository audioRepository;
    private RecordAudio recordAudio;

    public ServiceContainer() {
        this.audioRecorder = new JavaSoundAPIRecorder();
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
