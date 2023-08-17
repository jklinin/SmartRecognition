package com.sound.recognition.di;


import com.sound.recognition.adapters.recording.AudioRecorder;
import com.sound.recognition.adapters.recording.implementation.JavaSoundAPIRecorder;
import com.sound.recognition.adapters.tensorflow.implementation.TensorPythonScriptRunnerImplementation;
import com.sound.recognition.repository.recording.SoundFileRepository;
import com.sound.recognition.repository.recording.implementation.SoundFileRepositoryImplementation;
import com.sound.recognition.services.implementation.AudioSystemServiceImplementation;
import com.sound.recognition.services.implementation.MixerServiceImplemementation;


public class ServiceContainer {


    private final TensorPythonScriptRunnerImplementation tensorPythonScriptRunner;
    private AudioRecorder audioRecorder;

    private SoundFileRepository soundFileRepository;


    public ServiceContainer() {
        AudioSystemServiceImplementation audioSystemService = new AudioSystemServiceImplementation();
        MixerServiceImplemementation mixerService = new MixerServiceImplemementation(audioSystemService);

        this.soundFileRepository = new SoundFileRepositoryImplementation(mixerService, audioSystemService);

        this.audioRecorder = new JavaSoundAPIRecorder(audioSystemService, mixerService, soundFileRepository);


         tensorPythonScriptRunner = new TensorPythonScriptRunnerImplementation();
    }

    public AudioRecorder getAudioRecorder() {
        return audioRecorder;
    }


    public SoundFileRepository getSoundFileRepository() {
        return soundFileRepository;
    }

    public TensorPythonScriptRunnerImplementation getTensorPythonScriptRunner() {
        return tensorPythonScriptRunner;
    }
}
