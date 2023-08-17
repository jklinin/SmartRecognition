package com.sound.recognition.adapters.tensorflow.implementation;

import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;
import com.sound.recognition.entities.tensorflow.observer.AnalysisResultObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TensorPythonScriptRunnerImplementationTest {

    private TensorPythonScriptRunnerImplementation runner;
    private SoundFile soundFile;

    @BeforeEach
    void setUp() {
        runner = new TensorPythonScriptRunnerImplementation();
        soundFile = new SoundFile("path/to/test/file.wav");
    }

    @Test
    void testRunTensorFlowAnalysis() {
        AnalysisResult result = runner.runTensorFlowAnalysis(soundFile);
        assertNotNull(result);
        // Assume that a correct implementation will not return the error message. Adjust this as necessary.
        assertNotEquals("Error: Unable to analyze sound file.", result.getResult());
    }

    @Test
    void testObserverNotification() {
        AnalysisResultObserver mockObserver = mock(AnalysisResultObserver.class);
        runner.addResultObserver(mockObserver);

        runner.update(soundFile);

        // Wait for potential thread operations. Adjust the waiting time as necessary.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if observer's method was called
        verify(mockObserver, times(1)).onAnalysisComplete(any(AnalysisResult.class));
    }

    @Test
    void testObserverAdditionAndRemoval() {
        AnalysisResultObserver mockObserver = mock(AnalysisResultObserver.class);
        runner.addResultObserver(mockObserver);
        runner.removeResultObserver(mockObserver);

        runner.update(soundFile);

        // Wait for potential thread operations. Adjust the waiting time as necessary.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if observer's method was NOT called
        verify(mockObserver, times(0)).onAnalysisComplete(any(AnalysisResult.class));
    }
}
