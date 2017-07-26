Change-Log
===============

### Release 1.2.0 ###
> 26.07.2017

- Removed components that has been **deprecated** in the previous version.
- Renamed `UiEventBus` (still available but **deprecated**) to `MainEventBus` implementation.
- Added `SimpleEndPoint` class that is implementation of `EndPoint` interface.
- Added `ServiceCallAdapterFactory` implementation.
- Updated to use **2.3.0** version of **[Retrofit](http://square.github.io/retrofit/)**.
- **Dropped support** for _Android_ versions **below** _API Level 14_.
- Other minor updates and improvements.

### [Release 1.1.2](https://github.com/universum-studios/android_officium/releases/tag/1.1.2) ###
> 12.04.2017

- **Deprecated** direct access to some of protected fields and replaced by protected/public getter
  methods instead.

### [Release 1.1.1](https://github.com/universum-studios/android_officium/releases/tag/1.1.1) ###
> 03.04.2017

- Fixed [Issue #12](https://github.com/universum-studios/android_officium/issues/12).

### [Release 1.1.0](https://github.com/universum-studios/android_officium/releases/tag/1.1.0) ###
> 16.02.2017

- For `UserAccountManager` may be specified a [`Crypto`](https://github.com/universum-studios/android_crypto)
  implementation to support encryption and decryption for account data. Also data keys may be encrypted
  by specifying an `Encrypto` implementation for the desired account manager.

### [Release 1.0.1](https://github.com/universum-studios/android_officium/releases/tag/1.0.1) ###
> 05.02.2017

- Resolved **[issue](https://github.com/universum-studios/android_officium/issues/2)** with dispatching
  of callbacks for `UserAccountManager.AccountWatcher` on the **Ui thread**.

### [Release 1.0.0](https://github.com/universum-studios/android_officium/releases/tag/1.0.0) ###
> 21.12.2016

- First production release.