# Add project specific ProGuard rules here.
# Configuration with NO OBFUSCATION

# Disable all obfuscation
-dontobfuscate

# Keep all names intact
-keepnames class ** { *; }

# Optional: Keep optimization (comment out if you don't want any processing)
# -dontoptimize

# Keep source file names and line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Keep annotations
-keepattributes *Annotation*
