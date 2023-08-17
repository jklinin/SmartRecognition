package com.sound.recognition.adapters.tensorflow.implementation;

import com.sound.recognition.adapters.tensorflow.TensorPythonScriptRunner;
import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;
import com.sound.recognition.entities.tensorflow.observer.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TensorPythonScriptRunnerImplementation implements TensorPythonScriptRunner, Observer {

    @Override
    public AnalysisResult runTensorFlowAnalysis(SoundFile soundFile) {
        String resultFromPython = "";

        try {
            // Construct the command
            String[] cmd = {
                    "python",
                    "path_to_script/analyze_sound.py",
                    soundFile.getFilePath()
            };

            // Run the Python script
            Process p = Runtime.getRuntime().exec(cmd);

            // Read the output from the script
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            resultFromPython = in.readLine();
            in.close();
            return new AnalysisResult(resultFromPython);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately. Maybe return a default AnalysisResult or throw a custom exception.
            return new AnalysisResult("Error: Unable to analyze sound file.");
        }

    }


    @Override
    public void update(SoundFile soundFile) {
        // Code to start the TensorFlow analysis with the provided sound file.
        runTensorFlowAnalysis(soundFile);
    }
}
