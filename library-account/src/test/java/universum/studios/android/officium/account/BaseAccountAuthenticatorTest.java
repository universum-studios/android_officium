/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
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

import android.accounts.AccountAuthenticatorResponse;
import android.content.Context;

import org.junit.Test;

import androidx.annotation.NonNull;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
public final class BaseAccountAuthenticatorTest extends RobolectricTestCase {

	private static final String ACCOUNT_TYPE = "account.com";

	@Test public void testInstantiation() {
		// Act:
		new TestAuthenticator(context);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testEditProperties() {
		// Arrange:
		final AccountAuthenticatorResponse mockResponse = mock(AccountAuthenticatorResponse.class);
		final BaseAccountAuthenticator authenticator = new TestAuthenticator(context);
		// Act:
		authenticator.editProperties(mockResponse, ACCOUNT_TYPE);
	}

	private static final class TestAuthenticator extends BaseAccountAuthenticator {

		TestAuthenticator(@NonNull final Context context) {
			super(context);
		}
	}
}