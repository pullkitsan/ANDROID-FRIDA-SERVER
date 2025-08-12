package com.example.autofrida;

import android.content.Context;
import android.util.Log;
import java.io.*;

import android.content.Context;
import android.util.Log;
import java.io.*;

public class FridaHelper {
    private static final String TAG = "FridaHelper";
    private static final String FRIDA_BINARY_NAME = "frida-server";

    public static void copyFridaServer(Context context) {
        File fridaFile = new File(context.getFilesDir(), FRIDA_BINARY_NAME);

        // If Frida Server already exists, do nothing
        if (fridaFile.exists()) {
            Log.d(TAG, "Frida Server already copied.");
            return;
        }

        try {
            // Copy Frida Server from assets to internal storage
            InputStream inputStream = context.getAssets().open(FRIDA_BINARY_NAME);
            OutputStream outputStream = new FileOutputStream(fridaFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            // Make Frida Server executable
            fridaFile.setExecutable(true, false);
            Log.d(TAG, "Frida Server copied and made executable.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to copy Frida Server", e);
        }
    }
}
