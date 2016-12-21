Proguard
===============

This file describes which proguard rules **should** be used to preserve *proper working* of the
source code provided by this library when the **Proguard's obfuscation** process is applied to a
project that depends on this library.

> **Note, that the proguard rules listed below are not guarantied to ensure obfuscation that will
not affect the proper working of your Android application. Each Android application has its specific
structure, so it is hard to find rules that will fit needs of all of them. New general rules may be
added in the future.**

### Proguard-Rules ###

> Use below rules to keep **"sensitive"** source code of the library.

    # Keep names of ErrorResponse along with its Error object that are parsed from JSON data.
    -keepnames class universum.studios.android.officium.service.ErrorResponse { *; }
    -keepnames class universum.studios.android.officium.service.ErrorResponse$Error { *; }

> Use below rules to keep **entire** source code of the library.

    # Keep all classes within library package.
    -keep class universum.studios.android.officium.** { *; }
    