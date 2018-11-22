Change-Log
===============
> Regular configuration update: _01.11.2018_

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 2.x ##

### 2.0.0-beta3 ###
> upcoming

### [2.0.0-beta2](https://github.com/universum-studios/android_officium/releases/tag/2.0.0-beta2) ###
> 22.11.2018

- Regular **dependencies update** (mainly to use new artifacts from **Android Jetpack**).

### [2.0.0-beta1](https://github.com/universum-studios/android_officium/releases/tag/2.0.0-beta1) ###
> 31.05.2018

- Resolved [Issue #33](https://github.com/universum-studios/android_officium/issues/33),
  [Issue #34](https://github.com/universum-studios/android_officium/issues/34),
  [Issue #35](https://github.com/universum-studios/android_officium/issues/35).
- Deprecated some of delegate methods of `UserAccountManager` and `SingleUserAccountManager` which
  only delegated to the system `AccountManager`.

## Version 1.x ##

### [1.2.1](https://github.com/universum-studios/android_officium/releases/tag/v1.2.1) ###
> 15.12.2017

- Removed deprecated `UiEventBus` class.
- Updated **dependencies** versions and _Gradle_ configuration.

### [1.2.0](https://github.com/universum-studios/android_officium/releases/tag/v1.2.0) ###
> 26.07.2017

- Removed elements that has been **deprecated** in the previous version.
- Added `SimpleEndPoint` class that is implementation of `EndPoint` interface.
- Added `ServiceCallAdapterFactory` implementation.
- **Dropped support** for _Android_ versions **below** _API Level 14_.

### [1.1.2](https://github.com/universum-studios/android_officium/releases/tag/v1.1.2) ###
> 12.04.2017

- **Deprecated** direct access to some of protected fields and replaced by protected/public getter
  methods instead.

### [1.1.1](https://github.com/universum-studios/android_officium/releases/tag/v1.1.1) ###
> 03.04.2017

- Fixed [Issue #12](https://github.com/universum-studios/android_officium/issues/12).

### [1.1.0](https://github.com/universum-studios/android_officium/releases/tag/v1.1.0) ###
> 16.02.2017

- Added **support** for account **data encryption** and **decryption**.

### [1.0.1](https://github.com/universum-studios/android_officium/releases/tag/v1.0.1) ###
> 05.02.2017

- Resolved **[Issue #2](https://github.com/universum-studios/android_officium/issues/2)** with dispatching
  of callbacks for `UserAccountManager.AccountWatcher` on the **Ui thread**.

### [1.0.0](https://github.com/universum-studios/android_officium/releases/tag/v1.0.0) ###
> 21.12.2016

- First production release.