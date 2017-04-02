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

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A {@link AbstractAccountAuthenticator} implementation that can be used as base for custom implementations
 * of account authenticator.
 * <p>
 * Each and every abstract method of {@link AbstractAccountAuthenticator} is implemented by this
 * base authenticator class and throws {@link UnsupportedOperationException}.
 *
 * @author Martin Albedinsky
 */
public abstract class BaseAccountAuthenticator extends AbstractAccountAuthenticator {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "BaseAccountAuthenticator";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ========================u========================================================
	 */

	/**
	 * Creates a new instance of BaseAccountAuthenticator with the given <var>context</var>.
	 *
	 * @param context The context used by parent {@link AbstractAccountAuthenticator} for permissions
	 *                checks.
	 */
	public BaseAccountAuthenticator(@NonNull final Context context) {
		super(context);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Nullable
	@Override
	public Bundle editProperties(
			@NonNull final AccountAuthenticatorResponse response,
			@NonNull final String accountType
	) {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Nullable
	@Override
	public Bundle addAccount(
			@NonNull final AccountAuthenticatorResponse response,
			@NonNull final String accountType,
			@Nullable final String authTokenType,
			@Nullable final String[] requiredFeatures,
			@Nullable final Bundle options
			) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Nullable
	@Override
	public Bundle confirmCredentials(
			@NonNull final AccountAuthenticatorResponse response,
			@NonNull final Account account,
			@Nullable final Bundle options
	) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Nullable
	@Override
	public Bundle getAuthToken(
			@NonNull final AccountAuthenticatorResponse response,
			@NonNull final Account account,
			@NonNull final String authTokenType,
			@Nullable final Bundle options
	) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public String getAuthTokenLabel(@NonNull final String authTokenType) {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Nullable
	@Override
	public Bundle updateCredentials(
			@NonNull final AccountAuthenticatorResponse response,
			@NonNull final Account account,
			@Nullable final String authTokenType,
			@Nullable final Bundle options
	) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Nullable
	@Override
	public Bundle hasFeatures(
			@NonNull final AccountAuthenticatorResponse response,
			@NonNull final Account account,
			@NonNull final String[] features
	) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
