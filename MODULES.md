Modules
===============

Library is also distributed via **separate modules** which may be downloaded as standalone parts of
the library in order to decrease dependencies count in Android projects, so only dependencies really
needed in an Android project are included. **However** some modules may depend on another modules
from this library or on modules from other libraries.

## Download ##

### Gradle ###

For **successful resolving** of artifacts for separate modules via **Gradle** add the following snippet
into **build.gradle** script of your desired Android project and use `implementation '...'` declaration
as usually.

    repositories {
        maven {
            url  "http://dl.bintray.com/universum-studios/android"
        }
    }

## Available modules ##
> Following modules are available in the [latest](https://github.com/universum-studios/android_officium/releases "Releases page") stable release.

- **[Core](https://github.com/universum-studios/android_officium/tree/master/library-core)**
- **[Account](https://github.com/universum-studios/android_officium/tree/master/library-account)**
- **[@Event](https://github.com/universum-studios/android_officium/tree/master/library-event_group)**
- **[Event-Core](https://github.com/universum-studios/android_officium/tree/master/library-event-core)**
- **[Event-Common](https://github.com/universum-studios/android_officium/tree/master/library-event-common)**
- **[Service](https://github.com/universum-studios/android_officium/tree/master/library-service)**
- **[Sync](https://github.com/universum-studios/android_officium/tree/master/library-sync)**
