package com.example.autofrida;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String FRIDA_BINARY_NAME = "frida-server";
    private Thread debuggerDetectionThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start routine debugger detection
        startRoutineDebuggerDetection();

        // Copy Frida Server to internal storage
        FridaHelper.copyFridaServer(this);

        // Run Frida Server
        startFridaServer();
    }

    private void startRoutineDebuggerDetection() {
        debuggerDetectionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                boolean debugDetected = DebuggerDetector.isDebuggerConnected();
                
                if (debugDetected) {
                    Log.w(TAG, "WARNING: Debugger connected! Terminating app...");
                    System.exit(1);
                } else {
                    Log.d(TAG, "No debugger detected.");
                }
                
                try {
                    Thread.sleep(2000); // Check every 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        debuggerDetectionThread.start();
    }

    private void startFridaServer() {
        File fridaFile = new File(getFilesDir(), FRIDA_BINARY_NAME);

        if (!fridaFile.exists()) {
            Log.e(TAG, "Frida Server not found!");
            return;
        }

        try {
            Log.d(TAG, "Attempting to start Frida Server as root...");

            new Thread(() -> {
                try {
                    // Run Frida Server using "su -c" to start as root
                    Process process = Runtime.getRuntime().exec(new String[]{
                            "su", "-c", fridaFile.getAbsolutePath()
                    });

                    Log.d(TAG, "Frida Server started as root.");

                    process.waitFor(); // Keep Frida running
                } catch (IOException | InterruptedException e) {
                    Log.e(TAG, "Failed to start Frida Server", e);
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "Exception in starting Frida Server", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (debuggerDetectionThread != null && debuggerDetectionThread.isAlive()) {
            debuggerDetectionThread.interrupt();
        }
    }
}
