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
package universum.studios.android.officium.service.auth;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link Interceptor} implementation that may be used for interception of requests that require
 * an <b>authorization</b> token to be sent to the server along with the request data.
 * <p>
 * An instance of AuthRequestInterceptor requires instance of {@link AuthTokenProvider}. Such provider
 * is than used to peek the authorization token via {@link AuthTokenProvider#peekToken()} whenever
 * {@link #intercept(Chain)} is invoked. If the peeked token is valid the interceptor will add a new
 * header into request with name: <b>Authorization</b> and value: <b>Bearer TOKEN_VALUE</b>. If token
 * provider does not provide valid token, request remains unchanged.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class AuthRequestInterceptor implements Interceptor {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "AuthRequestInterceptor";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Name of the header that may contain authorization token.
	 */
	public static final String HEADER_NAME = "Authorization";

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Provider that can provide authorization token.
	 */
	private final AuthTokenProvider tokenProvider;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of AuthRequestInterceptor with the given authorization <var>tokenProvider</var>.
	 *
	 * @param tokenProvider The desired provider that will be used by the new interceptor to peek
	 *                      the authorization token when intercepting requests.
	 * @see #intercept(Chain)
	 */
	public AuthRequestInterceptor(@NonNull final AuthTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	/*
	 * Methods =====================================================================================
	 */

	// todo: add public static AuthRequestInterceptor create(@NonNull final AuthTokenProvider tokenProvider) { ... }

	// todo: ... and make the constructor private ...

	/**
	 */
	@Override public Response intercept(@NonNull final Chain chain) throws IOException {
		final String authToken = tokenProvider.peekToken();
		final Request request = chain.request();
		return (authToken == null || authToken.length() == 0) ?
				chain.proceed(request) :
				chain.proceed(request.newBuilder()
						.header(HEADER_NAME, "Bearer " + authToken)
						.build()
				);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}