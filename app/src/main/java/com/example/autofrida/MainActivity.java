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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Copy Frida Server to internal storage
        FridaHelper.copyFridaServer(this);

        // Run Frida Server
        startFridaServer();
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
}