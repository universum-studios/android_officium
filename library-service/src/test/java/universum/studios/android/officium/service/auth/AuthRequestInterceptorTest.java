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
package universum.studios.android.officium.service.auth;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import universum.studios.android.officium.service.TestChain;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class AuthRequestInterceptorTest extends LocalTestCase {

	@Test public void testConstants() {
		// Act + Assert:
		assertThat(AuthRequestInterceptor.HEADER_NAME, is("Authorization"));
	}

	@Test public void testInterceptWithoutToken() throws IOException {
		// Arrange:
		final AuthTokenProvider mockTokenProvider = mock(AuthTokenProvider.class);
		final AuthRequestInterceptor interceptor = new AuthRequestInterceptor(mockTokenProvider);
		final Request request = new Request.Builder().url("https://google.com").build();
		// Act:
		final Response response = interceptor.intercept(new TestChain(request));
		// Assert:
		assertThat(response, is(notNullValue()));
		final Request responseRequest = response.request();
		assertThat(responseRequest, is(request));
		final Headers headers = responseRequest.headers();
		assertThat(headers, is(notNullValue()));
		assertThat(headers.get(AuthRequestInterceptor.HEADER_NAME), is(nullValue()));
	}

	@Test public void testInterceptWithToken() throws IOException {
		// Arrange:
		final AuthTokenProvider mockTokenProvider = mock(AuthTokenProvider.class);
		when(mockTokenProvider.peekToken()).thenReturn("81297a2ee021a840a160551409407615f0d05b15");
		final AuthRequestInterceptor interceptor = new AuthRequestInterceptor(mockTokenProvider);
		final Request request = new Request.Builder().url("https://google.com").build();
		// Act:
		final Response response = interceptor.intercept(new TestChain(request));
		// Assert:
		assertThat(response, is(notNullValue()));
		final Request responseRequest = response.request();
		assertThat(responseRequest, is(not(request)));
		final Headers headers = responseRequest.headers();
		assertThat(headers, is(notNullValue()));
		assertThat(headers.get(AuthRequestInterceptor.HEADER_NAME), is("Bearer 81297a2ee021a840a160551409407615f0d05b15"));
	}
}