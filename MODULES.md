Modules
===============

Library is also distributed via **separate modules** which may be downloaded as standalone parts of
the library in order to decrease dependencies count in Android projects, so only dependencies really
need in an Android project are included. **However** some modules may depend on another modules from
this library or on modules from other libraries.

Below are listed modules that are available for download also with theirs dependencies.

## Download ##

### Gradle ###

**[Core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)**

    compile 'universum.studios.android:officium-core:1.0.0@aar'

**[Account](https://github.com/universum-studios/android_officium/tree/master/library/src/account)**

    compile 'universum.studios.android:officium-account:1.0.0@aar'

_depends on:_
[`universum.studios.android:utils`](https://github.com/universum-studios/android_utils)

**[Event](https://github.com/universum-studios/android_officium/tree/master/library/src/event)**

    compile 'universum.studios.android:officium-event:1.0.0@aar'

_depends on:_
[`com.squareup:otto`](http://square.github.io/otto/)

**[Event-Core](https://github.com/universum-studios/android_officium/tree/master/library/src/event/core)**

    compile 'universum.studios.android:officium-event-core:1.0.0@aar'

_depends on:_
[`com.squareup:otto`](http://square.github.io/otto/)

**[Event-Common](https://github.com/universum-studios/android_officium/tree/master/library/src/event/common)**

    compile 'universum.studios.android:officium-event-common:1.0.0@aar'

_depends on:_
[officium-event-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Service](https://github.com/universum-studios/android_officium/tree/master/library/src/service)**

    compile 'universum.studios.android:officium-service:1.0.0@aar'

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main),
[`com.squareup.okio:okio`](https://github.com/square/okio),
[`com.squareup.okhttp3:okhttp`](http://square.github.io/okhttp/),
[`com.squareup.retrofit2:retrofit`](http://square.github.io/retrofit/)

**[Sync](https://github.com/universum-studios/android_officium/tree/master/library/src/sync)**

    compile 'universum.studios.android:officium-sync:1.0.0@aar'

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main),
[`com.google.code.gson:gson`](https://github.com/google/gson)
