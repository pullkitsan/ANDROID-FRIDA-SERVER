package com.example.autofrida;

import android.os.Debug;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DebuggerDetector {
    
    public static boolean isDebuggerConnected() {
        return Debug.isDebuggerConnected() || isPtraceAttached() || isGdbAttached();
    }
    
    // Check for ptrace attachment by examining /proc/self/status
    private static boolean isPtraceAttached() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/self/status"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    String tracerPid = line.substring(10).trim();
                    reader.close();
                    return !tracerPid.equals("0");
                }
            }
            reader.close();
        } catch (IOException e) {
            // If we can't read the file, assume no debugger
        }
        return false;
    }
    
    // Check for GDB by looking at process list
    private static boolean isGdbAttached() {
        try {
            Process proc = Runtime.getRuntime().exec("ps");
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("gdb") || line.contains("gdbserver")) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            // If we can't execute ps, assume no debugger
        }
        return false;
    }
}