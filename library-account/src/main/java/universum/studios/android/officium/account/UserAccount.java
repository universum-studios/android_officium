/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.officium.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a simple holder for user data used when creating {@link Account Accounts} via
 * {@link UserAccountManager}. Each instance of UserAccount must be created with <var>name</var> and
 * <var>password</var> that are essential for creation of instance of {@link Account}.
 * <p>
 * There can be specified also additional data for each user account instance either via {@link #putData(String, String)}
 * or {@link #setDataBundle(Bundle)} including authentication tokens via {@link #putAuthToken(String, String)}.
 * <p>
 * <b>Note</b>, that {@link #getAuthTokenTypes()} method should always return array of token types
 * that are specific for a particular type of UserAccount implementation. This array is used by
 * {@link UserAccountManager} to iterate types Map ({@link #getAuthTokens()}) and obtain all token
 * data from it and store it for the associated {@link Account} via {@link AccountManager#setAuthToken(Account, String, String)}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class UserAccount {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "UserAccount";

	/**
	 * Type key for the <b>OAuth</b> token.
	 *
	 * @see #putAuthToken(String, String)
	 */
	public static final String TOKEN_TYPE_O_AUTH = "oAuth";

	/**
	 * Array specifying set of default authentication token types.
	 * <p>
	 * Contains single {@link #TOKEN_TYPE_O_AUTH} key.
	 */
	protected static final String[] AUTH_TOKEN_TYPES = {TOKEN_TYPE_O_AUTH};

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Name specified for this user account.
	 */
	@NonNull
	protected final String mName;

	/**
	 * Password specified for this user account.
	 */
	@Nullable
	protected String mPassword;

	/**
	 * Map with authentication tokens for this user account mapped to theirs types.
	 */
	private Map<String, String> mAuthTokens;

	/**
	 * Bundle with data for this user account.
	 */
	private Bundle mDataBundle;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #UserAccount(String, String)} with {@code null} password.
	 *
	 * @param name The desired name for the new account.
	 */
	public UserAccount(@NonNull final String name) {
		this(name, null);
	}

	/**
	 * Creates a new instance of UserAccount with the specified <var>name</var> and <var>password</var>.
	 *
	 * @param name     The desired name for the new account.
	 * @param password The desired password for the new account. May be {@code null}.
	 */
	public UserAccount(@NonNull final String name, @Nullable final String password) {
		this.mName = name;
		this.mPassword = password;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the name specified for this user account.
	 *
	 * @return This account's name.
	 * @see #UserAccount(String)
	 * @see #UserAccount(String, String)
	 */
	@NonNull
	public final String getName() {
		return mName;
	}

	/**
	 * Sets a new password for this account.
	 *
	 * @param password The desired new password. May be {@code null} to clear the current one.
	 * @see #UserAccount(String, String)
	 * @see #getPassword()
	 */
	public final void setPassword(@Nullable final String password) {
		this.mPassword = password;
	}

	/**
	 * Returns the password specified for this user account.
	 *
	 * @return This account's password. May be {@code null}.
	 * @see #setPassword(String)
	 */
	@Nullable
	public final String getPassword() {
		return mPassword;
	}

	/**
	 * Puts the given <var>data</var> into data bundle of this user account under the specified
	 * <var>key</var>.
	 *
	 * @param key  The key under which to map the given data value.
	 * @param data The desired data to put into bundle.
	 * @see #getData(String)
	 * @see #getDataBundle()
	 */
	public void putData(@NonNull final String key, @Nullable final String data) {
		if (mDataBundle == null) this.mDataBundle = new Bundle();
		mDataBundle.putString(key, data);
	}

	/**
	 * Checks whether this account has data with the specified <var>key</var> associated.
	 *
	 * @param key The key of the desired data to check.
	 * @return {@code True} if this account has data with the key, {@code false} otherwise.
	 * @see #putData(String, String)
	 */
	public boolean hasData(@NonNull final String key) {
		return mDataBundle != null && mDataBundle.containsKey(key);
	}

	/**
	 * Returns the data value contained within data bundle of this user account under the specified
	 * <var>key</var>.
	 *
	 * @param key The key for which to obtain the desired data.
	 * @return Data value for the specified key or {@code null} if there is no such value mapped for
	 * that key.
	 * @see #putData(String, String)
	 * @see #hasData(String)
	 * @see #getDataBundle()
	 */
	@Nullable
	public String getData(@NonNull final String key) {
		return mDataBundle == null ? null : mDataBundle.getString(key);
	}

	/**
	 * Removes the data specified for this account for the specified <var>key</var>.
	 *
	 * @param key The key for which to remove the desired data.
	 * @see #putData(String, String)
	 * @see #hasData(String)
	 */
	public void removeData(@NonNull final String key) {
		if (mDataBundle != null) mDataBundle.remove(key);
	}

	/**
	 * Sets a data bundle for this user account.
	 *
	 * @param dataBundle The desired bundle with user data. May be {@code null} to clear the current
	 *                   bundle.
	 * @see #putData(String, String)
	 * @see #getData(String)
	 */
	public void setDataBundle(@Nullable final Bundle dataBundle) {
		this.mDataBundle = dataBundle;
	}

	/**
	 * Returns the data bundle of this user account
	 *
	 * @return Bundle with user data either specified via {@link #setDataBundle(Bundle)} or separately
	 * via {@link #putData(String, String)} or {@code null} if there is no data bundle available.
	 * @see #setDataBundle(Bundle)
	 * @see #getData(String)
	 */
	@Nullable
	public Bundle getDataBundle() {
		return mDataBundle;
	}

	/**
	 * Same as {@link #putAuthToken(String, String)} with {@link #TOKEN_TYPE_O_AUTH} as <var>tokenType</var>.
	 *
	 * @param authToken The desired <b>OAuth</b> token. May be {@code null} to clear the previous one.
	 */
	public void putOAuthToken(@Nullable final String authToken) {
		putAuthToken(TOKEN_TYPE_O_AUTH, authToken);
	}

	/**
	 * Puts the given <var>token</var> into authentication tokens of this user account under the
	 * specified <var>tokenType</var> as key.
	 *
	 * @param tokenType The desired type of the given token.
	 * @param token     The desired authentication token. May be {@code null} to clear the previous one.
	 * @see #getAuthToken(String)
	 * @see #getAuthTokens()
	 */
	public void putAuthToken(@Nullable final String tokenType, @Nullable final String token) {
		if (mAuthTokens == null) this.mAuthTokens = new HashMap<>(1);
		mAuthTokens.put(tokenType, token);
	}

	/**
	 * Checks whether there is authentication token specified for this user account for the specified
	 * <var>tokenType</var>.
	 *
	 * @param tokenType Type of the desired token to check if it is specified for this account.
	 * @return {@code True} if {@link #putAuthToken(String, String)} has been called with the
	 * <var>tokenType</var>, {@code false} otherwise.
	 */
	public boolean hasAuthToken(@Nullable final String tokenType) {
		return mAuthTokens != null && mAuthTokens.containsKey(tokenType);
	}

	/**
	 * Same as {@link #getAuthToken(String)} with {@link #TOKEN_TYPE_O_AUTH} as <var>tokenType</var>.
	 */
	@Nullable
	public String getOAuthToken() {
		return getAuthToken(TOKEN_TYPE_O_AUTH);
	}

	/**
	 * Returns the authentication token specified for this user account for the requested <var>tokenType</var>.
	 *
	 * @param tokenType Type of the desired token to obtain.
	 * @return Auth token for the requested token type or {@code null} if there is no token specified
	 * for that type.
	 */
	@Nullable
	public String getAuthToken(@Nullable final String tokenType) {
		return mAuthTokens == null ? null : mAuthTokens.get(tokenType);
	}

	/**
	 * Returns the types of authentication tokens specific for this user account.
	 *
	 * @return For this account specific authentication token types.
	 */
	@Nullable
	protected String[] getAuthTokenTypes() {
		return AUTH_TOKEN_TYPES;
	}

	/**
	 * Returns the map with authentication tokens specified for this user account mapped to theirs
	 * types.
	 *
	 * @return This account's authentication tokens or {@code null} if there are no tokens specified
	 * yet.
	 * @see #putAuthToken(String, String)
	 * @see #getAuthToken(String)
	 */
	@Nullable
	protected Map<String, String> getAuthTokens() {
		return mAuthTokens;
	}

	/**
	 */
	@Override
	public int hashCode() {
		// Hashing and equals functions assume that as name is unique for each account.
		return mName.hashCode();
	}

	/**
	 */
	@Override
	public boolean equals(@Nullable final Object other) {
		// Hashing and equals functions assume that name is unique for each account.
		if (other == this) return true;
		if (!(other instanceof UserAccount)) return false;
		final UserAccount account = (UserAccount) other;
		return TextUtils.equals(account.mName, mName);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}