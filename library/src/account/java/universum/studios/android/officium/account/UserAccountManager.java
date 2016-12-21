/*
 * =================================================================================================
 *                             Copyright (C) 2015 Martin Albedinsky
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License
 * you may obtain at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * You can redistribute, modify or publish any part of the code written within this file but as it
 * is described in the License, the software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.officium.account;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import universum.studios.android.util.ErrorException;

/**
 * Wrapper for {@link AccountManager} that can be used to simplify management of an Android application
 * accounts. Each instance of UserAccountManager can manage accounts only of a single type that is
 * specified during initialization via {@link #UserAccountManager(Context, String)} constructor.
 * UserAccountManager can be used for both creation and deletion of Android accounts via
 * {@link #createAccount(UserAccount)} and {@link #deleteAccount(UserAccount)} or via theirs asynchronous
 * relatives {@link #createAccountAsync(UserAccount)} and {@link #deleteAccountAsync(UserAccount)}.
 * <p>
 * This manager also provides API methods to store and peek authentication tokens for a specific
 * account via {@link #setAccountAuthToken(Account, String, String)} and {@link #peekAccountAuthToken(Account, String)}
 * along with account's password management via {@link #setAccountPassword(Account, String)} and
 * {@link #getAccountPassword(Account)}. Data for a specific account can be stored either as single
 * values via {@link #setAccountData(Account, String, String)} or as data {@link Bundle} via
 * {@link #setAccountDataBundle(Account, Bundle)} which is basically bulk method for the single
 * value storing method. Stored account data can be than obtained via {@link #getAccountData(Account, String)}
 * or via {@link #getAccountDataBundle(Account, String...)}.
 *
 * @param <A> Type of the user account that will be managed by the UserAccountManager subclass.
 * @author Martin Albedinsky
 */
public abstract class UserAccountManager<A extends UserAccount> {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Watcher that may be used to listen for callbacks fired whenever a new Android {@link Account}
	 * is <b>created</b> or <b>deleted</b> asynchronously for its associated {@link UserAccount}.
	 *
	 * @param <A> Type of the user account managed by an UserAccountManager implementation to which
	 *            will be this watcher attached.
	 * @author Martin Albedinsky
	 * @see #createAccountAsync(UserAccount)
	 * @see #deleteAccountAsync(UserAccount)
	 */
	public interface AccountWatcher<A extends UserAccount> {

		/**
		 * Invoked whenever a new Android {@link Account} has been created for the specified
		 * <var>userAccount</var> after call to {@link #createAccountAsync(UserAccount)}.
		 *
		 * @param userAccount The user account for which has been the corresponding Android account
		 *                    created.
		 * @see #onAccountError(UserAccount, ErrorException)
		 */
		void onAccountCreated(@NonNull A userAccount);

		/**
		 * Invoked whenever an old Android {@link Account} has been deleted for the specified
		 * <var>userAccount</var> after call to {@link #deleteAccountAsync(UserAccount)}.
		 *
		 * @param userAccount The user account for which has been the corresponding Android account
		 *                    deleted.
		 * @see #onAccountError(UserAccount, ErrorException)
		 */
		void onAccountDeleted(@NonNull A userAccount);

		/**
		 * Invoked whenever an error occurs during execution of one of account management related
		 * tasks.
		 *
		 * @param userAccount The user account for which has the error occurred.
		 * @param error       The occurred error. The error's code that may be obtained via
		 *                    {@link ErrorException#getCode()} describes the error. Will be one
		 *                    of {@link #ERROR_CREATE_ACCOUNT} or {@link #ERROR_DELETE_ACCOUNT}.
		 */
		void onAccountError(@NonNull A userAccount, @NonNull ErrorException error);
	}

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "UserAccountManager";

	/**
	 * Error code indicating that an error occurred during asynchronous execution of
	 * {@link #onCreateAccount(UserAccount)} requested via {@link #createAccountAsync(UserAccount)}.
	 */
	public static final int ERROR_CREATE_ACCOUNT = -0x01;

	/**
	 * Error code indicating that an error occurred during asynchronous execution of
	 * {@link #onDeleteAccount(UserAccount)} requested via {@link #deleteAccountAsync(UserAccount)}.
	 */
	public static final int ERROR_DELETE_ACCOUNT = -0x02;

	/**
	 * Value for Android permission to <b>GET</b> accounts.
	 */
	protected static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;

	/**
	 * Value for Android permission to <b>MANAGE</b> accounts.
	 */
	protected static final String PERMISSION_MANAGE_ACCOUNTS = "android.permission.MANAGE_ACCOUNTS";

	/**
	 * Value for Android permission to <b>AUTHENTICATE</b> accounts.
	 */
	protected static final String PERMISSION_AUTHENTICATE_ACCOUNTS = "android.permission.AUTHENTICATE_ACCOUNTS";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Context used to access {@link #mManager} and other needed application data about accounts.
	 */
	protected final Context mContext;

	/**
	 * Account manager used to create/update/delete accounts of the type specified for this manager.
	 */
	protected final AccountManager mManager;

	/**
	 * Type of accounts that can be managed by this manager.
	 */
	protected final String mAccountType;

	/**
	 * List of watchers that will be notified whenever a new account is created <b>asynchronously</b>
	 * via {@link #createAccountAsync(UserAccount)} or an old one deleted <b>asynchronously</b> via
	 * {@link #deleteAccountAsync(UserAccount)}.
	 */
	private final List<AccountWatcher<A>> mWatchers = new ArrayList<>(5);

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of UserAccountManager for the specified <var>accountType</var>.
	 *
	 * @param context     Context used to access framework's {@link AccountManager}.
	 * @param accountType The desired type of accounts that can be managed by the new manager.
	 */
	public UserAccountManager(@NonNull Context context, @NonNull String accountType) {
		this.mContext = context;
		this.mManager = AccountManager.get(mContext);
		this.mAccountType = accountType;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Registers a watcher to be notified whenever a new user account is created or an old one
	 * deleted.
	 *
	 * @param watcher The desired watcher to register.
	 * @see #unregisterWatcher(AccountWatcher)
	 */
	public void registerWatcher(@NonNull AccountWatcher<A> watcher) {
		if (!mWatchers.contains(watcher)) mWatchers.add(watcher);
	}

	/**
	 * Unregisters the given <var>watcher</var> from the registered ones.
	 *
	 * @param watcher The desired watcher to unregister.
	 * @see #registerWatcher(AccountWatcher)
	 */
	public void unregisterWatcher(@NonNull AccountWatcher<A> watcher) {
		mWatchers.remove(watcher);
	}

	/**
	 * Same as {@link #createAccount(UserAccount)} where creation of the given <var>userAccount</var>
	 * will be executed <b>asynchronously</b> using {@link AsyncTask}. When the creation process is
	 * finished the current {@link AccountWatcher AccountWatchers} (if any) will be notified through
	 * {@link AccountWatcher#onAccountCreated(UserAccount)} callback.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param userAccount The desired user account for which to create a corresponding Android {@link Account}.
	 * @see #registerWatcher(AccountWatcher)
	 * @see #deleteAccountAsync(UserAccount)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	@SuppressWarnings("unchecked")
	public void createAccountAsync(@NonNull A userAccount) {
		new CreateAccountTask().execute(userAccount);
	}

	/**
	 * Creates a new Android {@link Account} for the given <var>userAccount</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param userAccount The desired user account for which to create the corresponding Android {@link Account}.
	 * @return {@code True} if the account has been created, {@code false} otherwise.
	 * @see #createAccountAsync(UserAccount)
	 * @see #deleteAccount(UserAccount)
	 * @see AccountManager#addAccountExplicitly(Account, String, Bundle)
	 * @see AccountManager#setAuthToken(Account, String, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean createAccount(@NonNull A userAccount) {
		if (onCreateAccount(userAccount)) {
			notifyAccountCreated(userAccount);
			return true;
		}
		return false;
	}

	/**
	 * Invoked whenever {@link #createAccount(UserAccount)} or {@link #createAccountAsync(UserAccount)}
	 * is called to create new Android {@link Account}.
	 * <p>
	 * <b>Note</b>, that this method can be invoked on a background thread.
	 * <p>
	 * Current implementation always returns {@code true}.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param userAccount The desired user account for which to create the corresponding Android
	 *                    {@link Account}.
	 * @return {@code True} if account has been created, {@code false} otherwise.
	 * @see #onDeleteAccount(UserAccount)
	 * @see AccountManager#addAccountExplicitly(Account, String, Bundle)
	 * @see AccountManager#setAuthToken(Account, String, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	protected boolean onCreateAccount(@NonNull A userAccount) {
		final Account account = new Account(userAccount.getName(), mAccountType);
		onDeleteAccount(userAccount);
		if (mManager.addAccountExplicitly(account, userAccount.getPassword(), userAccount.getDataBundle())) {
			final String[] authTokenTypes = userAccount.getAuthTokenTypes();
			final Map<String, String> authTokens = userAccount.getAuthTokens();
			if (authTokenTypes != null && authTokenTypes.length > 0 && authTokens != null && !authTokens.isEmpty()) {
				for (String authTokenType : authTokenTypes) {
					mManager.setAuthToken(account, authTokenType, authTokens.get(authTokenType));
				}
			}
		}
		return true;
	}

	/**
	 * Sets an authentication token for the given <var>account</var> with the specified <var>authTokenType</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account       The account for which to update its authentication token.
	 * @param authTokenType Type of the authentication token to be stored used as key.
	 * @param authToken     The desired authentication token to be stored.
	 * @see #peekAccountAuthToken(Account, String)
	 * @see #isAccountAuthenticated(Account, String)
	 * @see AccountManager#setAuthToken(Account, String, String)
	 */
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public void setAccountAuthToken(@NonNull Account account, @NonNull String authTokenType, @Nullable String authToken) {
		mManager.setAuthToken(account, authTokenType, authToken);
	}

	/**
	 * Checks whether the given <var>account</var> is authenticated or not.
	 * <p>
	 * By default the account is considered authenticated if there is stored authentication token for
	 * that account with the specified <var>authTokenType</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account       The account for which to check its authentication state.
	 * @param authTokenType Type of the authentication token used to resolve whether the account is
	 *                      authenticated or not.
	 * @return {@code True} if there is stored valid authentication token for the account with the
	 * specified token type, {@code false} otherwise.
	 * @see #setAccountAuthToken(Account, String, String)
	 * @see #peekAccountAuthToken(Account, String)
	 */
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public boolean isAccountAuthenticated(@NonNull Account account, @NonNull String authTokenType) {
		return !TextUtils.isEmpty(peekAccountAuthToken(account, authTokenType));
	}

	/**
	 * Returns the authentication token for the given <var>account</var> stored for the specified
	 * <var>authTokenType</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account       The account for which to peek the requested authentication token.
	 * @param authTokenType Type of the requested authentication token to peek.
	 * @return Authentication token or {@code null} if there has not been stored token for the requested
	 * type yet or the token has been invalidated.
	 * @see #setAccountAuthToken(Account, String, String)
	 * @see #invalidateAccountAuthToken(Account, String)
	 * @see AccountManager#peekAuthToken(Account, String)
	 */
	@Nullable
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public String peekAccountAuthToken(@NonNull Account account, @NonNull String authTokenType) {
		return mManager.peekAuthToken(account, authTokenType);
	}

	/**
	 * Invalidates the specified <var>authToken</var> for the given <var>account</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_MANAGE_ACCOUNTS}</b> permission.
	 *
	 * @param account   The account for which to invalidate the authentication token.
	 * @param authToken The token that should be invalidated.
	 * @see #peekAccountAuthToken(Account, String)
	 * @see AccountManager#invalidateAuthToken(String, String)
	 */
	@RequiresPermission(PERMISSION_MANAGE_ACCOUNTS)
	public void invalidateAccountAuthToken(@NonNull Account account, @NonNull String authToken) {
		mManager.invalidateAuthToken(account.type, authToken);
	}

	/**
	 * Sets a password for the given <var>account</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account  The account for which to update its password.
	 * @param password The desired password to be stored.
	 * @see #getAccountPassword(Account)
	 * @see #clearAccountPassword(Account)
	 * @see AccountManager#setPassword(Account, String)
	 */
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public void setAccountPassword(@NonNull Account account, @Nullable String password) {
		mManager.setPassword(account, password);
	}

	/**
	 * Returns the password for the given <var>account</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account The account for which to obtain the requested password.
	 * @return Requested password or {@code null} if there has not been stored any password for the
	 * account yet.
	 * @see #setAccountPassword(Account, String)
	 * @see #clearAccountPassword(Account)
	 * @see AccountManager#getPassword(Account)
	 */
	@Nullable
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public String getAccountPassword(@NonNull Account account) {
		return mManager.getPassword(account);
	}

	/**
	 * Clears the password for the given <var>account</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_MANAGE_ACCOUNTS}</b> permission.
	 *
	 * @param account The account for which to clear its password.
	 * @see AccountManager#clearPassword(Account)
	 */
	@RequiresPermission(PERMISSION_MANAGE_ACCOUNTS)
	public void clearAccountPassword(@NonNull Account account) {
		mManager.clearPassword(account);
	}

	/**
	 * Sets a single data <var>value</var> for the given <var>account</var> with the specified <var>key</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account The account for which to update its data.
	 * @param key     The key under which will be the desired value stored.
	 * @param value   The desired data value to be stored.
	 * @see #getAccountData(Account, String)
	 * @see #setAccountDataBundle(Account, Bundle)
	 * @see AccountManager#setUserData(Account, String, String)
	 */
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public void setAccountData(@NonNull Account account, @NonNull String key, @Nullable String value) {
		mManager.setUserData(account, key, value);
	}

	/**
	 * Returns the single data for the given <var>account</var> stored under the specified <var>key</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account The account for which to obtain its data.
	 * @param key     The key for which to obtain the requested account data.
	 * @return Requested account data or {@code null} if there are no data stored for the requested
	 * key.
	 * @see #setAccountData(Account, String, String)
	 * @see AccountManager#getUserData(Account, String)
	 */
	@Nullable
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public String getAccountData(@NonNull Account account, @NonNull String key) {
		return mManager.getUserData(account, key);
	}

	/**
	 * Sets a bundle with data for the given <var>account</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account    The account for which to update its data bundle.
	 * @param dataBundle Bundle with the desired data.
	 * @see #getAccountDataBundle(Account, String...)
	 * @see #setAccountData(Account, String, String)
	 */
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public void setAccountDataBundle(@NonNull Account account, @NonNull Bundle dataBundle) {
		if (dataBundle.isEmpty()) return;
		for (String key : dataBundle.keySet()) {
			mManager.setUserData(account, key, dataBundle.getString(key));
		}
	}

	/**
	 * Returns the bundle with data for the given <var>account</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permission.
	 *
	 * @param account The account for which to obtain its data bundle.
	 * @param keys    Set of keys for which to obtain the desired data.
	 * @return Bundle with account data for the requested keys. May be empty if the specified keys
	 * are also empty.
	 * @see #setAccountDataBundle(Account, Bundle)
	 * @see #getAccountData(Account, String)
	 */
	@NonNull
	@RequiresPermission(PERMISSION_AUTHENTICATE_ACCOUNTS)
	public Bundle getAccountDataBundle(@NonNull Account account, @NonNull String... keys) {
		final Bundle bundle = new Bundle();
		if (keys.length > 0) {
			for (String key : keys) {
				bundle.putString(key, mManager.getUserData(account, key));
			}
		}
		return bundle;
	}

	/**
	 * Same as {@link #deleteAccount(UserAccount)} where deletion of the given <var>userAccount</var>
	 * will be executed <b>asynchronously</b> using {@link AsyncTask}. When the deletion process is
	 * finished the current {@link AccountWatcher AccountWatchers} (if any) will be notified through
	 * {@link AccountWatcher#onAccountDeleted(UserAccount)} callback.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param userAccount The desired user account for which to delete the corresponding Android
	 *                    {@link Account}.
	 * @see #registerWatcher(AccountWatcher)
	 * @see #deleteAccount(UserAccount)
	 * @see #createAccountAsync(UserAccount)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	@SuppressWarnings("unchecked")
	public void deleteAccountAsync(@NonNull A userAccount) {
		new DeleteAccountTask().execute(userAccount);
	}

	/**
	 * Deletes an existing Android {@link Account} for the given <var>userAccount</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param userAccount The desired user account for which to delete the corresponding Android {@link Account}.
	 * @return {@code True} if the account has been deleted, {@code false} otherwise.
	 * @see #deleteAccountAsync(UserAccount)
	 * @see #createAccount(UserAccount)
	 * @see AccountManager#removeAccount(Account, AccountManagerCallback, Handler)
	 * @see AccountManager#invalidateAuthToken(String, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean deleteAccount(@NonNull A userAccount) {
		if (onDeleteAccount(userAccount)) {
			notifyAccountDeleted(userAccount);
			return true;
		}
		return false;
	}

	/**
	 * Invoked whenever {@link #createAccount(UserAccount)} or {@link #createAccountAsync(UserAccount)}
	 * is called to create new Android {@link Account}.
	 * <p>
	 * <b>Note</b>, that this method can be invoked on a background thread.
	 * <p>
	 * Current implementation returns {@code true} whenever there has been found Android account to
	 * be deleted for the given user account, {@code false} otherwise.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param userAccount The desired user account for which to delete the corresponding Android
	 *                    {@link Account}.
	 * @return {@code True} if account has been deleted, {@code false} otherwise.
	 * @see #onCreateAccount(UserAccount)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	@SuppressWarnings({"MissingPermission", "deprecation"})
	protected boolean onDeleteAccount(@NonNull A userAccount) {
		final Account account = findAccountForUser(userAccount);
		if (account != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
				mManager.removeAccount(account, null, null, null);
			} else {
				mManager.removeAccount(account, null, null);
			}
			mManager.setPassword(account, null);
			final String[] authTokenTypes = userAccount.getAuthTokenTypes();
			if (authTokenTypes != null && authTokenTypes.length > 0) {
				for (String authTokenType : authTokenTypes) {
					mManager.invalidateAuthToken(account.type, mManager.peekAuthToken(account, authTokenType));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Called to find the Android {@link Account} associated with the given <var>userAccount</var>.
	 * <p>
	 * Default implementation retrieves all current accounts by the account type specified for this
	 * manager and searches for one that has same name specified as the given user account. See
	 * {@link UserAccount#getName()} and {@link Account#name} for additional information.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> permission.
	 *
	 * @param userAccount The user account for which to find the corresponding Android account.
	 * @return Android account that has been previously created for the given user account or
	 * {@code null} if there is no account created for the requested user account.
	 */
	@Nullable
	@RequiresPermission(PERMISSION_GET_ACCOUNTS)
	protected Account findAccountForUser(@NonNull A userAccount) {
		final Account[] accounts = mManager.getAccountsByType(mAccountType);
		if (accounts.length > 0) {
			final String accountName = userAccount.getName();
			for (Account account : accounts) {
				if (account.name.equals(accountName)) return account;
			}
		}
		return null;
	}

	/**
	 * Notifies the current {@link AccountWatcher AccountWatchers} (if any) that the given
	 * <var>userAccount</var> has been just created.
	 *
	 * @param userAccount The created account.
	 */
	@SuppressWarnings("WeakerAccess")
	final void notifyAccountCreated(@NonNull A userAccount) {
		synchronized (mWatchers) {
			if (!mWatchers.isEmpty()) {
				for (AccountWatcher<A> watcher : mWatchers) {
					watcher.onAccountCreated(userAccount);
				}
			}
		}
	}

	/**
	 * Notifies the current {@link AccountWatcher AccountWatchers} (if any) that the given
	 * <var>userAccount</var> has been just deleted.
	 *
	 * @param userAccount The deleted account.
	 */
	@SuppressWarnings("WeakerAccess")
	final void notifyAccountDeleted(@NonNull A userAccount) {
		synchronized (mWatchers) {
			if (!mWatchers.isEmpty()) {
				for (AccountWatcher<A> watcher : mWatchers) {
					watcher.onAccountDeleted(userAccount);
				}
			}
		}
	}

	/**
	 * Notifies the current {@link AccountWatcher AccountWatchers} (if any) that the specified
	 * <var>error</var> has occurred for the given <var>userAccount</var>.
	 *
	 * @param userAccount The account for which error has occurred.
	 * @param error       The occurred error.
	 */
	@SuppressWarnings("WeakerAccess")
	final void notifyAccountError(@NonNull A userAccount, @NonNull ErrorException error) {
		synchronized (mWatchers) {
			if (!mWatchers.isEmpty()) {
				for (AccountWatcher<A> watcher : mWatchers) {
					watcher.onAccountError(userAccount, error);
				}
			}
		}
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * An {@link AsyncTask} implementation used to perform execution of {@link #onCreateAccount(UserAccount)}
	 * <b>asynchronously</b> used whenever {@link #createAccountAsync(UserAccount)} is called.
	 */
	private final class CreateAccountTask extends AsyncTask<A, Void, TaskResult<A>> {

		/**
		 */
		@Override
		@SafeVarargs
		@SuppressWarnings("MissingPermission")
		protected final TaskResult<A> doInBackground(A... accounts) {
			final A account = accounts[0];
			ErrorException error = null;
			try {
				if (!onCreateAccount(account)) {
					error = ErrorException.withCode(ERROR_CREATE_ACCOUNT);
				}
			} catch (Exception e) {
				error = new ErrorException(ERROR_CREATE_ACCOUNT, e);
			}
			return new TaskResult<>(account, error);
		}

		/**
		 */
		@Override
		protected final void onPostExecute(@NonNull TaskResult<A> result) {
			if (result.error == null) notifyAccountCreated(result.account);
			else notifyAccountError(result.account, result.error);
		}
	}

	/**
	 * An {@link AsyncTask} implementation used to perform execution of {@link #onDeleteAccount(UserAccount)}
	 * <b>asynchronously</b> used whenever {@link #deleteAccountAsync(UserAccount)} is called.
	 */
	private final class DeleteAccountTask extends AsyncTask<A, Void, TaskResult<A>> {

		/**
		 */
		@Override
		@SafeVarargs
		@SuppressWarnings("MissingPermission")
		protected final TaskResult<A> doInBackground(A... accounts) {
			final A account = accounts[0];
			ErrorException error = null;
			try {
				if (!onDeleteAccount(account)) {
					error = ErrorException.withCode(ERROR_DELETE_ACCOUNT);
				}
			} catch (Exception e) {
				error = new ErrorException(ERROR_DELETE_ACCOUNT, e);
			}
			return new TaskResult<>(account, error);
		}

		/**
		 */
		@Override
		protected final void onPostExecute(@NonNull TaskResult<A> result) {
			if (result.error == null) notifyAccountDeleted(result.account);
			else notifyAccountError(result.account, result.error);
		}
	}

	/**
	 * Class that may hold result of either {@link UserAccountManager.CreateAccountTask} or
	 * {@link UserAccountManager.DeleteAccountTask}.
	 */
	private static final class TaskResult<A extends UserAccount> {

		/**
		 * Account for which has been background task executed.
		 */
		final A account;

		/**
		 * Error occurred during execution of background task. May be {@code null}.
		 */
		final ErrorException error;

		/**
		 * Creates a new instance of TaskResult with the given <var>account</var> and <var>error</var>.
		 *
		 * @param account The account for which has been background task executed.
		 * @param error   The optional error if occurred during execution of background task.
		 */
		TaskResult(A account, ErrorException error) {
			this.account = account;
			this.error = error;
		}
	}
}
