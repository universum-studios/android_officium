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
package universum.studios.android.officium.service.auth;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import universum.studios.android.officium.service.TestChain;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class AuthRequestInterceptorTest extends LocalTestCase {

    @Test
	public void testHEADER_NAME() {
		assertThat(AuthRequestInterceptor.HEADER_NAME, is("Authorization"));
	}

	@Test
	public void testInterceptWithoutToken() throws IOException {
		final AuthTokenProvider mockTokenProvider = mock(AuthTokenProvider.class);
		final AuthRequestInterceptor interceptor = new AuthRequestInterceptor(mockTokenProvider);
		final Request request = new Request.Builder().url("https://google.com").build();
		final Response response = interceptor.intercept(new TestChain(request));
		assertThat(response, is(notNullValue()));
		final Request responseRequest = response.request();
		assertThat(responseRequest, is(request));
		final Headers headers = responseRequest.headers();
		assertThat(headers, is(notNullValue()));
		assertThat(headers.get(AuthRequestInterceptor.HEADER_NAME), is(nullValue()));
	}

	@Test
	public void testInterceptWithToken() throws IOException {
		final AuthTokenProvider mockTokenProvider = mock(AuthTokenProvider.class);
		when(mockTokenProvider.peekToken()).thenReturn("81297a2ee021a840a160551409407615f0d05b15");
		final AuthRequestInterceptor interceptor = new AuthRequestInterceptor(mockTokenProvider);
		final Request request = new Request.Builder().url("https://google.com").build();
		final Response response = interceptor.intercept(new TestChain(request));
		assertThat(response, is(notNullValue()));
		final Request responseRequest = response.request();
		assertThat(responseRequest, is(not(request)));
		final Headers headers = responseRequest.headers();
		assertThat(headers, is(notNullValue()));
		assertThat(headers.get(AuthRequestInterceptor.HEADER_NAME), is("Bearer 81297a2ee021a840a160551409407615f0d05b15"));
	}
}
