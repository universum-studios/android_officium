/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

/**
 * A {@link UserAccountManager} implementation that can be used for Android applications that allow
 * only one account to be created for a user.
 *
 * @author Martin Albedinsky
 */
public class SingleUserAccountManager<A extends UserAccount> extends UserAccountManager<A> {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SingleUserAccountManager";

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Creates a new instance of SingleUserAccountManager for the specified <var>accountType</var>.
	 *
	 * @param context     Context used to access {@link AccountManager}.
	 * @param accountType The desired account type that will be managed by the new user account manager.
	 */
	public SingleUserAccountManager(@NonNull Context context, @NonNull String accountType) {
		super(context, accountType);
	}

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Checks whether there is account created or not.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> permission.
	 *
	 * @return {@code True} if there is account created at this time, {@code false} otherwise.
	 * @see #getAccount()
	 */
	@RequiresPermission(PERMISSION_GET_ACCOUNTS)
	public boolean isAccountCreated() {
		return acquireAccount() != null;
	}

	/**
	 * Returns the current account if it is created.
	 * <p>
	 * <b>Note</b>, that default implementation of this method assumes that there can be only one
	 * account created for this Android application. If there are multiple accounts created this
	 * method will pick the first one.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> permission.
	 *
	 * @return Current account or {@code null} if there is no account created yet.
	 * @see #isAccountCreated()
	 */
	@Nullable
	@RequiresPermission(PERMISSION_GET_ACCOUNTS)
	public Account getAccount() {
		return acquireAccount();
	}

	/**
	 * Sets an authentication token for the current single account (if created) with the specified
	 * <var>authTokenType</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param authTokenType Type of the authentication token to be stored used as key.
	 * @param authToken     The desired authentication token to be stored.
	 * @return {@code True} if authentication token for the account has been updated, {@code false}
	 * if there is no account created at this time for which to update its token.
	 * @see #peekAccountAuthToken(String)
	 * @see #isAccountAuthenticated(String)
	 * @see #setAccountAuthToken(Account, String, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean setAccountAuthToken(@NonNull String authTokenType, @Nullable String authToken) {
		final Account account = acquireAccount();
		if (account == null) return false;
		setAccountAuthToken(account, authTokenType, authToken);
		return true;
	}

	/**
	 * Checks whether the current single account (if created) is authenticated or not.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param authTokenType Type of the authentication token used to resolve whether the account is
	 *                      authenticated or not.
	 * @return {@code True} if there is stored valid authentication token for the account with the
	 * specified token type, {@code false} if there is no account created at this time or there has
	 * not been stored token with the specified token type yet.
	 * @see #setAccountAuthToken(String, String)
	 * @see #isAccountAuthenticated(Account, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean isAccountAuthenticated(@NonNull String authTokenType) {
		final Account account = acquireAccount();
		return account != null && isAccountAuthenticated(account, authTokenType);
	}

	/**
	 * Returns the authentication token for the current single account (if created) stored for the
	 * specified <var>authTokenType</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param authTokenType Type of the requested authentication token to peek for the account.
	 * @return Authentication token or {@code null} if there is no account created at this time or
	 * there has not been stored token for the requested type yet or the token has been invalidated.
	 * @see #setAccountAuthToken(String, String)
	 * @see #invalidateAccountAuthToken(String)
	 * @see #peekAccountAuthToken(Account, String)
	 */
	@Nullable
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public String peekAccountAuthToken(@NonNull String authTokenType) {
		final Account account = acquireAccount();
		return account != null ? peekAccountAuthToken(account, authTokenType) : null;
	}

	/**
	 * Invalidates the specified <var>authToken</var> for the current single account (if created).
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_MANAGE_ACCOUNTS}</b> permissions.
	 *
	 * @param authToken The token that should be invalidated for the account.
	 * @return {@code True} if authentication token for the account has been invalidated, {@code false}
	 * if there is not account created at this time for which to invalidate its token.
	 * @see #peekAccountAuthToken(String)
	 * @see #invalidateAccountAuthToken(Account, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_MANAGE_ACCOUNTS
	})
	public boolean invalidateAccountAuthToken(@NonNull String authToken) {
		final Account account = acquireAccount();
		if (account == null) return false;
		invalidateAccountAuthToken(account, authToken);
		return true;
	}

	/**
	 * Sets a password for the current single account (if created).
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param password The desired password to be stored for the account. May be {@code null} to
	 *                 clear the current one.
	 * @return {@code True} if password for the account has been updated, {@code false} if there is
	 * no account created at this time for which to update its password.
	 * @see #getAccountPassword()
	 * @see #clearAccountPassword()
	 * @see #setAccountPassword(Account, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean setAccountPassword(@Nullable String password) {
		final Account account = acquireAccount();
		if (account == null) return false;
		setAccountPassword(account, password);
		return true;
	}

	/**
	 * Returns the password for the current single account (if created).
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @return Requested password or {@code null} if there is no account created at this time or
	 * there has not been stored any password for the account yet.
	 * @see #setAccountPassword(String)
	 * @see #clearAccountPassword()
	 * @see #getAccountPassword(Account)
	 */
	@Nullable
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public String getAccountPassword() {
		final Account account = acquireAccount();
		return account == null ? null : getAccountPassword(account);
	}

	/**
	 * Clears the password for the current single account (if created).
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @return {@code True} if password for the account has been cleared, {@code false} if there is
	 * no account created at this time for which to clear its password.
	 * @see #setAccountPassword(String)
	 * @see #getAccountPassword()
	 * @see #clearAccountPassword(Account)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_MANAGE_ACCOUNTS
	})
	public boolean clearAccountPassword() {
		final Account account = acquireAccount();
		if (account == null) return false;
		clearAccountPassword(account);
		return true;
	}

	/**
	 * Sets a single data <var>value</var> for the current single account (if created) with the
	 * specified <var>key</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param key   The key under which will be the desired value stored for the account.
	 * @param value The desired data value to be stored.
	 * @return {@code True} if data for the account has been updated, {@code false} if there is no
	 * account created at this time for which to update its data.
	 * @see #getAccountData(String)
	 * @see #setAccountDataBundle(Bundle)
	 * @see #setAccountData(Account, String, String)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean setAccountData(@NonNull String key, @Nullable String value) {
		final Account account = acquireAccount();
		if (account == null) return false;
		setAccountData(account, key, value);
		return true;
	}

	/**
	 * Returns the single data for the current single account (if created) stored under the specified
	 * <var>key</var>.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param key The key for which to obtain the requested account data.
	 * @return Requested account data or {@code null} if there is no account created at this time or
	 * there are no data stored for the requested key.
	 * @see #setAccountData(String, String)
	 * @see #getAccountData(Account, String)
	 */
	@Nullable
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public String getAccountData(@NonNull String key) {
		final Account account = acquireAccount();
		return account == null ? null : getAccountData(account, key);
	}


	/**
	 * Sets a bundle with data for the current single account (if created).
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param dataBundle Bundle with the desired data for the account.
	 * @return {@code True} if data bundle for the account has been updated, {@code false} if there
	 * is no account created at this time for which to update its data bundle.
	 * @see #getAccountDataBundle(String...)
	 * @see #setAccountDataBundle(Account, Bundle)
	 */
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public boolean setAccountDataBundle(@NonNull Bundle dataBundle) {
		final Account account = acquireAccount();
		if (account == null) return false;
		setAccountDataBundle(account, dataBundle);
		return true;
	}

	/**
	 * Returns the bundle with data for the current single account (if created).
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> along with
	 * <b>{@link #PERMISSION_AUTHENTICATE_ACCOUNTS}</b> permissions.
	 *
	 * @param keys Set of keys for which to obtain the desired data.
	 * @return Bundle with account data for the requested keys or {@code null} if there is no account
	 * created at this time.
	 * @see #setAccountDataBundle(Bundle)
	 * @see #setAccountData(String, String)
	 * @see #getAccountDataBundle(Account, String...)
	 */
	@Nullable
	@RequiresPermission(allOf = {
			PERMISSION_GET_ACCOUNTS,
			PERMISSION_AUTHENTICATE_ACCOUNTS
	})
	public Bundle getAccountDataBundle(@NonNull String... keys) {
		final Account account = acquireAccount();
		return account == null ? null : getAccountDataBundle(account, keys);
	}

	/**
	 * Called to acquire current single account.
	 * <p>
	 * <b>Note</b>, that default implementation of this method assumes that there can be only one
	 * account created for this Android application. If there are multiple accounts created this
	 * method will pick the first one.
	 * <p>
	 * This method requires the caller to hold <b>{@link #PERMISSION_GET_ACCOUNTS}</b> permission.
	 *
	 * @return Valid account if there is some created for the account type specified for this
	 * manager, {@code null} if there is no account created yet.
	 * @see AccountManager#getAccountsByType(String)
	 */
	@Nullable
	@RequiresPermission(PERMISSION_GET_ACCOUNTS)
	protected Account acquireAccount() {
		final AccountManager manager = AccountManager.get(mContext);
		final Account[] accounts = manager.getAccountsByType(mAccountType);
		// We assume here that there can be only one user account available/created (if any).
		return accounts.length > 0 ? accounts[0] : null;
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
