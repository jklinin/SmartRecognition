package com.sound.recognition.adapters.tensorflow.implementation;

import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TensorPythonScriptRunnerImplementationTest {

    private TensorPythonScriptRunnerImplementation runner;
    private SoundFile mockSoundFile;

    @BeforeEach
    void setUp() {
        runner = new TensorPythonScriptRunnerImplementation();
        mockSoundFile = mock(SoundFile.class);
        when(mockSoundFile.getFilePath()).thenReturn("dummy_path");
    }

    @Test
    void testRunTensorFlowAnalysisSuccess() {
        AnalysisResult result = runner.runTensorFlowAnalysis(mockSoundFile);
        assertNotNull(result);
        // TODO Assuming your Python script for a successful case returns "Success", adjust as needed.
        assertEquals("Success", result.getResult());
    }

    @Test
    void testRunTensorFlowAnalysisFailure() {
        // Here, you might need a way to induce an error. One way could be to mock the SoundFile to return an invalid path.
        when(mockSoundFile.getFilePath()).thenReturn("invalid_path");

        AnalysisResult result = runner.runTensorFlowAnalysis(mockSoundFile);
        assertNotNull(result);
        assertEquals("Error: Unable to analyze sound file.", result.getResult());
    }
}
