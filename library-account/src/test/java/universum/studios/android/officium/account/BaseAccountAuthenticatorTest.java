/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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
import android.accounts.AccountAuthenticatorResponse;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
public final class BaseAccountAuthenticatorTest extends RobolectricTestCase {

	private static final String ACCOUNT_TYPE = "account@test.com";
    
    @Test
	public void testInstantiation() {
		new AccountAuthenticator(mApplication);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testEditProperties() {
    	new AccountAuthenticator(mApplication).editProperties(
    			mock(AccountAuthenticatorResponse.class),
			    ACCOUNT_TYPE
	    );
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAccount() throws Exception {
		new AccountAuthenticator(mApplication).addAccount(
				mock(AccountAuthenticatorResponse.class),
				ACCOUNT_TYPE,
				"oAuth",
				new String[]{},
				Bundle.EMPTY
		);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testConfirmCredentials() throws Exception {
    	new AccountAuthenticator(mApplication).confirmCredentials(
			    mock(AccountAuthenticatorResponse.class),
			    mock(Account.class),
			    Bundle.EMPTY
	    );
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetAuthToken() throws Exception {
    	new AccountAuthenticator(mApplication).getAuthToken(
			    mock(AccountAuthenticatorResponse.class),
			    mock(Account.class),
			    "oAuth",
			    Bundle.EMPTY
	    );
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetAuthTokenLabel() {
    	new AccountAuthenticator(mApplication).getAuthTokenLabel(ACCOUNT_TYPE);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUpdateCredentials() throws Exception {
    	new AccountAuthenticator(mApplication).updateCredentials(
			    mock(AccountAuthenticatorResponse.class),
			    mock(Account.class),
			    "oAuth",
			    Bundle.EMPTY
	    );
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testHasFeatures() throws Exception {
    	new AccountAuthenticator(mApplication).hasFeatures(
			    mock(AccountAuthenticatorResponse.class),
			    mock(Account.class),
			    new String[]{}
	    );
	}

	private static final class AccountAuthenticator extends BaseAccountAuthenticator {

		AccountAuthenticator(@NonNull Context context) {
			super(context);
		}
	}
}
