# Rules only for PROGUARD set up. These should be removed for the release version.
#-renamesourcefileattribute SourceFile
# Keep source file attributes and line numbers. Can be useful when examining thrown exceptions.
#-keepattributes SourceFile, LineNumberTable
# Keep names of all classes and theirs methods. Can be useful when examining thrown exceptions.
#-keepnames class ** { *; }

# BASE SETUP =======================================================================================
-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod, *Annotation*
# Remove all none release loggs.
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
# Keep all annotations.
-keep public @interface * { *; }

# LIBRARY SPECIFIC RULES ===========================================================================
# No rules required.

# SAMPLES SPECIFIC RULES ===========================================================================
# No rules required.