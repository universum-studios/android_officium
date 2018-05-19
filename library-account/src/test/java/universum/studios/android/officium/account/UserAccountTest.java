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

import android.os.Bundle;

import org.junit.Test;

import java.util.Map;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class UserAccountTest extends RobolectricTestCase {

	private static final String NAME = "user@name.com";
	private static final String PASSWORD = "pass";

    @Test
	public void testConstants() {
		assertThat(UserAccount.TOKEN_TYPE_O_AUTH, is("oAuth"));
		assertThat(UserAccount.AUTH_TOKEN_TYPES, is(new String[]{UserAccount.TOKEN_TYPE_O_AUTH}));
    }

    @Test
	public void testInstantiationWithName() {
    	final UserAccount account = new UserAccount(NAME);
    	assertThat(account.getName(), is(NAME));
    	assertThat(account.getPassword(), is(nullValue()));
    }

    @Test
	public void testInstantiationWithNameAndPassword() {
    	final UserAccount account = new UserAccount(NAME, PASSWORD);
    	assertThat(account.getName(), is(NAME));
    	assertThat(account.getPassword(), is(PASSWORD));
    }

	@Test
	public void testSetPassword() {
		final UserAccount account = new UserAccount(NAME, PASSWORD);
		account.setPassword("newPass");
		assertThat(account.getPassword(), is("newPass"));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testPutGetData() {
		final UserAccount account = new UserAccount(NAME);
		assertThat(account.getData("data:1"), is(nullValue()));
		account.putData("data:1", "Data 1");
		account.putData("data:2", "Data 2");
		account.putData("data:3", "Data 3");
		final Bundle dataBundle = account.getDataBundle();
		assertThat(dataBundle, is(notNullValue()));
		assertThat(dataBundle.size(), is(3));
		assertThat(account.getData("data:1"), is("Data 1"));
		assertThat(account.getData("data:1"), is(dataBundle.get("data:1")));
		assertThat(account.getData("data:2"), is("Data 2"));
		assertThat(account.getData("data:2"), is(dataBundle.get("data:2")));
		assertThat(account.getData("data:3"), is("Data 3"));
		assertThat(account.getData("data:3"), is(dataBundle.get("data:3")));
	}

	@Test
	public void testHasData() {
		final UserAccount account = new UserAccount(NAME);
		assertThat(account.hasData("data:1"), is(false));
		assertThat(account.hasData("data:2"), is(false));
		account.putData("data:1", "Data 1");
		account.putData("data:2", null);
		assertThat(account.hasData("data:1"), is(true));
		assertThat(account.hasData("data:2"), is(true));
    }

	@Test
	public void testRemoveData() {
		final UserAccount account = new UserAccount(NAME);
		account.removeData("data:1");
		account.putData("data:1", "Data 1");
		account.putData("data:2", null);
		account.removeData("data:1");
		account.removeData("data:2");
		account.removeData("data:3");
		assertThat(account.hasData("data:1"), is(false));
		assertThat(account.hasData("data:2"), is(false));
    }

	@Test
	public void testGetDataBundleDefault() {
    	assertThat(new UserAccount(NAME).getDataBundle(), is(nullValue()));
	}

	@Test
	public void testSetDataBundle() {
    	final UserAccount account = new UserAccount(NAME);
    	final Bundle dataBundle = new Bundle();
    	dataBundle.putString("data:1", "Data 1");
    	dataBundle.putString("data:2", "Data 2");
    	dataBundle.putInt("data:int", 1);
    	account.setDataBundle(dataBundle);
    	assertThat(account.getDataBundle(), is(dataBundle));
    	assertThat(account.getData("data:1"), is("Data 1"));
    	assertThat(account.getData("data:2"), is("Data 2"));
    	assertThat(account.getData("data:int"), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testPutGetOAuthToken() {
		final UserAccount account = new UserAccount(NAME);
		assertThat(account.getOAuthToken(), is(nullValue()));
		account.putOAuthToken("123456789");
		account.putOAuthToken("1234567890");
		assertThat(account.getOAuthToken(), is("1234567890"));
		final Map<String, String> authTokens = account.getAuthTokens();
		assertThat(authTokens, is(notNullValue()));
		assertThat(authTokens.size(), is(1));
		assertThat(authTokens.get(UserAccount.TOKEN_TYPE_O_AUTH), is("1234567890"));
    }

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testPutGetAuthToken() {
		final UserAccount account = new UserAccount(NAME);
		account.putAuthToken(UserAccount.TOKEN_TYPE_O_AUTH, "123456789");
		account.putAuthToken("authToken:1", "123456789");
		account.putAuthToken("authToken:2", "1234567890");
		final Map<String, String> authTokens = account.getAuthTokens();
		assertThat(authTokens, is(notNullValue()));
		assertThat(authTokens.size(), is(3));
		assertThat(authTokens.get(UserAccount.TOKEN_TYPE_O_AUTH), is("123456789"));
		assertThat(authTokens.get("authToken:1"), is("123456789"));
		assertThat(authTokens.get("authToken:2"), is("1234567890"));
	}

	@Test
	public void testHasAuthToken() {
		final UserAccount account = new UserAccount(NAME);
		assertThat(account.hasAuthToken(UserAccount.TOKEN_TYPE_O_AUTH), is(false));
		account.putAuthToken(UserAccount.TOKEN_TYPE_O_AUTH, "0");
		account.putAuthToken("authToken:1", "1");
		account.putAuthToken("authToken:2", "2");
		assertThat(account.hasAuthToken(UserAccount.TOKEN_TYPE_O_AUTH), is(true));
		assertThat(account.hasAuthToken("authToken:1"), is(true));
		assertThat(account.hasAuthToken("authToken:2"), is(true));
		assertThat(account.hasAuthToken("authToken:3"), is(false));
	}

	@Test
	public void testGetAuthTokenTypes() {
    	assertThat(new UserAccount(NAME).getAuthTokenTypes(), is(UserAccount.AUTH_TOKEN_TYPES));
	}

	@Test
	public void testGetAuthTokensDefault() {
    	assertThat(new UserAccount(NAME).getAuthTokens(), is(nullValue()));
	}

	@Test
	public void testHashCode() {
		assertThat(new UserAccount(NAME).hashCode(), is(NAME.hashCode()));
		assertThat(new UserAccount(NAME, PASSWORD).hashCode(), is(NAME.hashCode()));
	}

	@Test
	@SuppressWarnings({"ObjectEqualsNull", "EqualsBetweenInconvertibleTypes", "EqualsWithItself"})
	public void testEquals() {
    	final UserAccount account = new UserAccount(NAME);
    	assertThat(account.equals(account), is(true));
    	assertThat(new UserAccount(NAME).equals(new UserAccount(NAME)), is(true));
    	assertThat(new UserAccount(NAME, PASSWORD).equals(new UserAccount(NAME)), is(true));
    	assertThat(new UserAccount(NAME).equals(new UserAccount("user2@name.com")), is(false));
    	assertThat(new UserAccount(NAME).equals(null), is(false));
    	assertThat(new UserAccount(NAME).equals(0), is(false));
	}
}