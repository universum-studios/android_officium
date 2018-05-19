/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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
		new TestAuthenticator(application);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testEditProperties() {
		new TestAuthenticator(application).editProperties(
				mock(AccountAuthenticatorResponse.class),
				ACCOUNT_TYPE
		);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAccount() throws Exception {
		new TestAuthenticator(application).addAccount(
				mock(AccountAuthenticatorResponse.class),
				ACCOUNT_TYPE,
				"oAuth",
				new String[]{},
				Bundle.EMPTY
		);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testConfirmCredentials() throws Exception {
		new TestAuthenticator(application).confirmCredentials(
				mock(AccountAuthenticatorResponse.class),
				mock(Account.class),
				Bundle.EMPTY
		);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetAuthToken() throws Exception {
		new TestAuthenticator(application).getAuthToken(
				mock(AccountAuthenticatorResponse.class),
				mock(Account.class),
				"oAuth",
				Bundle.EMPTY
		);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetAuthTokenLabel() {
		new TestAuthenticator(application).getAuthTokenLabel(ACCOUNT_TYPE);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUpdateCredentials() throws Exception {
		new TestAuthenticator(application).updateCredentials(
				mock(AccountAuthenticatorResponse.class),
				mock(Account.class),
				"oAuth",
				Bundle.EMPTY
		);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testHasFeatures() throws Exception {
		new TestAuthenticator(application).hasFeatures(
				mock(AccountAuthenticatorResponse.class),
				mock(Account.class),
				new String[]{}
		);
	}

	private static final class TestAuthenticator extends BaseAccountAuthenticator {

		TestAuthenticator(@NonNull final Context context) {
			super(context);
		}
	}
}