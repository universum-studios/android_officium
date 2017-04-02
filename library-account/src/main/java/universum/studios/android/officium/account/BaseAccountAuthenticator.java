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
	public BaseAccountAuthenticator(@NonNull Context context) {
		super(context);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public String getAuthTokenLabel(String authTokenType) {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/**
	 */
	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
		throw new UnsupportedOperationException();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
