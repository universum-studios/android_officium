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

**[Event](https://github.com/universum-studios/android_officium/tree/master/library/src/contact)**

    compile 'universum.studios.android:officium-event:1.0.0@aar'

_depends on:_
[`com.squareup:otto`](http://square.github.io/otto/)

**[Event-Core](https://github.com/universum-studios/android_officium/tree/master/library/src/contact)**

    compile 'universum.studios.android:officium-event-core:1.0.0@aar'

_depends on:_
[`com.squareup:otto`](http://square.github.io/otto/)

**[Event-Common](https://github.com/universum-studios/android_officium/tree/master/library/src/contact)**

    compile 'universum.studios.android:officium-event-common:1.0.0@aar'

_depends on:_
[`event-core`](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Content](https://github.com/universum-studios/android_officium/tree/master/library/src/content)**

    compile 'universum.studios.android:officium-content:1.0.0@aar'

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main),
[officium-mime-type](https://github.com/universum-studios/android_officium/tree/master/library/src/mime-type)

**[Map](https://github.com/universum-studios/android_officium/tree/master/library/src/map)**

    compile 'universum.studios.android:officium-map:1.0.0@aar'

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Play](https://github.com/universum-studios/android_officium/tree/master/library/src/play)**

    compile 'universum.studios.android:officium-play:1.0.0@aar'

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Web](https://github.com/universum-studios/android_officium/tree/master/library/src/web)**

    compile 'universum.studios.android:officium-web:1.0.0@aar'

_depends on:_
[officium-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Mime-Type](https://github.com/universum-studios/android_officium/tree/master/library/src/mime-type)**

    compile 'universum.studios.android:officium-mime-type:1.0.0@aar'