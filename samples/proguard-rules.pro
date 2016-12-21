##
# ==================================================================================================
#                             Copyright (C) 2016 Universum Studios
# ==================================================================================================
#         Licensed under the Apache License, Version 2.0 or later (further "License" only).
# --------------------------------------------------------------------------------------------------
# You may use this file only in compliance with the License. More details and copy of this License
# you may obtain at
#
# 		http://www.apache.org/licenses/LICENSE-2.0
#
# You can redistribute, modify or publish any part of the code written within this file but as it
# is described in the License, the software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
#
# See the License for the specific language governing permissions and limitations under the License.
# ==================================================================================================
##
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
