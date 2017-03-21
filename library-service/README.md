Officium-Service
===============

This module contains components that may be used to build a **service API layer** for an **Android**
application backed by **[Retrofit](http://square.github.io/retrofit/)** and **[OkHttp](http://square.github.io/okhttp/)**
in a simple and consistent way.

## Download ##
[![Download](https://api.bintray.com/packages/universum-studios/android/universum.studios.android%3Aofficium/images/download.svg)](https://bintray.com/universum-studios/android/universum.studios.android%3Aofficium/_latestVersion)

### Gradle ###

    compile "universum.studios.android:officium-service:${DESIRED_VERSION}@aar"

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library-core),
[`com.squareup.okio:okio`](https://github.com/square/okio),
[`com.squareup.okhttp3:okhttp`](http://square.github.io/okhttp/),
[`com.squareup.retrofit2:retrofit`](http://square.github.io/retrofit/)

## Components ##

Below are listed some of **primary components** that are available in this module:

- [ServiceManager](https://github.com/universum-studios/android_officium/blob/master/library-service/src/main/java/universum/studios/android/officium/service/ServiceManager.java)
- [ServiceApi](https://github.com/universum-studios/android_officium/blob/master/library-service/src/main/java/universum/studios/android/officium/service/ServiceApi.java)
- [ServiceApiProvider](https://github.com/universum-studios/android_officium/blob/master/library-service/src/main/java/universum/studios/android/officium/service/ServiceApiProvider.java)
