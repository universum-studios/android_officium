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

    compile 'universum.studios.android:intents-core:1.0.0@aar'

**[Calendar](https://github.com/universum-studios/android_officium/tree/master/library/src/calendar)**

    compile 'universum.studios.android:intents-calendar:1.0.0@aar'

_depends on:_
[intents-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Contact](https://github.com/universum-studios/android_officium/tree/master/library/src/contact)**

    compile 'universum.studios.android:intents-contact:1.0.0@aar'

_depends on:_
[intents-contact](https://github.com/universum-studios/android_officium/tree/master/library/src/contact)

**[Content](https://github.com/universum-studios/android_officium/tree/master/library/src/content)**

    compile 'universum.studios.android:intents-content:1.0.0@aar'

_depends on:_
[intents-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main),
[intents-mime-type](https://github.com/universum-studios/android_officium/tree/master/library/src/mime-type)

**[Map](https://github.com/universum-studios/android_officium/tree/master/library/src/map)**

    compile 'universum.studios.android:intents-map:1.0.0@aar'

_depends on:_
[intents-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Play](https://github.com/universum-studios/android_officium/tree/master/library/src/play)**

    compile 'universum.studios.android:intents-play:1.0.0@aar'

_depends on:_
[intents-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Web](https://github.com/universum-studios/android_officium/tree/master/library/src/web)**

    compile 'universum.studios.android:intents-web:1.0.0@aar'

_depends on:_
[intents-core](https://github.com/universum-studios/android_officium/tree/master/library/src/main)

**[Mime-Type](https://github.com/universum-studios/android_officium/tree/master/library/src/mime-type)**

    compile 'universum.studios.android:intents-mime-type:1.0.0@aar'