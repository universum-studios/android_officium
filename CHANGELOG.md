Change-Log
===============

### [Release 1.1.2](https://github.com/universum-studios/android_officium/releases/tag/1.1.2) ###
> 11.04.2017

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