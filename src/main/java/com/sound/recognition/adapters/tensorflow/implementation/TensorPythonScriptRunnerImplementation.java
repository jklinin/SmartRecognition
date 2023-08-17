package com.sound.recognition.adapters.tensorflow.implementation;

import com.sound.recognition.adapters.tensorflow.TensorPythonScriptRunner;
import com.sound.recognition.entities.recording.SoundFile;
import com.sound.recognition.entities.tensorflow.AnalysisResult;
import com.sound.recognition.entities.tensorflow.observer.AnalysisResultObserver;
import com.sound.recognition.entities.tensorflow.observer.AnalysisResultSubject;
import com.sound.recognition.entities.tensorflow.observer.SoundRecordingObserver;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TensorPythonScriptRunnerImplementation implements TensorPythonScriptRunner, SoundRecordingObserver, AnalysisResultSubject {
    private final ExecutorService executorService;
    private final List<AnalysisResultObserver> resultObservers = new ArrayList<>();

    public TensorPythonScriptRunnerImplementation() {
        // Initializing ExecutorService with 5 threads
        this.executorService = Executors.newFixedThreadPool(5);
    }


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
        // Start the TensorFlow analysis in a new thread
        executorService.submit(() -> {
            try {
                AnalysisResult result = runTensorFlowAnalysis(soundFile);
                notifyResultObservers(result);
            } catch (Exception e) {
                // handle exceptions or log them
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addResultObserver(AnalysisResultObserver o) {
        resultObservers.add(o);
    }

    @Override
    public void removeResultObserver(AnalysisResultObserver o) {
        resultObservers.remove(o);
    }

    @Override
    public void notifyResultObservers(AnalysisResult result) {
        for (AnalysisResultObserver observer : resultObservers) {
            observer.onAnalysisComplete(result);
        }
    }
}
