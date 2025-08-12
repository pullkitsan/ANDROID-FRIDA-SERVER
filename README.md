# AutoFrida

     An Android application that integrates Frida dynamic instrumentation toolkit with anti-debugging capabilities.

     ## Features

     - **Frida Server Integration**: Automatically copies and starts frida-server binary as root
     - **Anti-Debugging Protection**: Detects debugger attachment using multiple methods:
       - Android Debug API detection
       - Ptrace attachment detection via `/proc/self/status`
       - GDB/GDBServer process detection
     - **Root Execution**: Runs frida-server with root privileges using `su`

     ## Architecture

     ### Core Components

     - **MainActivity**: Main application entry point that orchestrates debugger detection and Frida server startup
     - **FridaHelper**: Utility class for copying frida-server binary from assets to internal storage
     - **DebuggerDetector**: Anti-debugging module that continuously monitors for debugging attempts

     ### File Structure

     ```
     app/src/main/
     ├── java/com/example/autofrida/
     │   ├── MainActivity.java          # Main application logic
     │   ├── FridaHelper.java          # Frida server management
     │   └── DebuggerDetector.java     # Anti-debugging detection
     ├── assets/
     │   └── frida-server              # Frida server binary
     └── res/
         └── layout/
             └── activity_main.xml     # Main activity layout
     ```

     ## Requirements

     - **Android Device**: Rooted Android device
     - **Architecture**: Compatible frida-server binary for target architecture
     - **Permissions**: Root access required for frida-server execution

     ## Installation

     1. Build the APK using Android Studio or Gradle
     2. Install on rooted Android device
     3. Grant root permissions when prompted

     ## Usage

     The application automatically:
     1. Starts continuous debugger detection (checks every 2 seconds)
     2. Copies frida-server binary to internal storage
     3. Launches frida-server with root privileges
     4. Terminates if debugger is detected

     ## Security Notes

     ⚠️ **Warning**: This application contains anti-debugging mechanisms and requires root access. Use only for legitimate security research and testing
     purposes.

     ## Build Configuration

     - **Target SDK**: Check `app/build.gradle.kts`
     - **ProGuard**: Obfuscation rules in `app/proguard-rules.pro`
     - **Signing**: Configure keystore in `keystore.properties` (not included in repo)

     ## License

     This project is for educational and security research purposes only.



Note : Add the latest version of frida-server binary in app/src/main/assets/ 
