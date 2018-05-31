Android Officium
===============

[![CircleCI](https://circleci.com/gh/universum-studios/android_officium.svg?style=shield)](https://circleci.com/gh/universum-studios/android_officium)
[![Codecov](https://codecov.io/gh/universum-studios/android_officium/branch/master/graph/badge.svg)](https://codecov.io/gh/universum-studios/android_officium)
[![Codacy](https://api.codacy.com/project/badge/Grade/b46e219849d04733ab3cb29beeb0cfaf)](https://www.codacy.com/app/universum-studios/android_officium?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=universum-studios/android_officium&amp;utm_campaign=Badge_Grade)
[![Android](https://img.shields.io/badge/android-8.1-blue.svg)](https://developer.android.com/about/versions/oreo/android-8.1.html)

Account, synchronization, services and events management for the Android platform.

For more information please visit the **[Wiki](https://github.com/universum-studios/android_officium/wiki)**.

## Download ##
[![Bintray](https://api.bintray.com/packages/universum-studios/android/universum.studios.android%3Aofficium/images/download.svg)](https://bintray.com/universum-studios/android/universum.studios.android%3Aofficium/_latestVersion)

Download the latest **[release](https://github.com/universum-studios/android_officium/releases "Releases page")** or **add as dependency** in your project via:

### Gradle ###

    implementation "universum.studios.android:officium:${DESIRED_VERSION}@aar"

## Modules ##

This library may be used via **separate [modules](https://github.com/universum-studios/android_officium/blob/master/MODULES.md)**
in order to depend only on desired _parts of the library's code base_ what ultimately results in **fewer dependencies**.

## Compatibility ##

Supported down to the **Android [API Level 14](http://developer.android.com/about/versions/android-4.0.html "See API highlights")**.

### Dependencies ###

- [`com.android.support:support-annotations`](https://developer.android.com/topic/libraries/support-library/packages.html#annotations)
- [`com.squareup:otto`](http://square.github.io/otto/)
- [`com.squareup.okio:okio`](https://github.com/square/okio)
- [`com.squareup.okhttp3:okhttp`](http://square.github.io/okhttp/)
- [`com.squareup.retrofit2:retrofit`](http://square.github.io/retrofit/)
- [`com.google.code.gson:gson`](https://github.com/google/gson)
- [`universum.studios.android:logger`](https://github.com/universum-studios/android_logger)
- [`universum.studios.android:crypto`](https://github.com/universum-studios/android_crypto)

## [License](https://github.com/universum-studios/android_officium/blob/master/LICENSE.md) ##

**Copyright 2018 Universum Studios**

_Licensed under the Apache License, Version 2.0 (the "License");_

You may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied.
     
See the License for the specific language governing permissions and limitations under the License.