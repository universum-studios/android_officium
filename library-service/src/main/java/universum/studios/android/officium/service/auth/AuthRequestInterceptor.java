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
package universum.studios.android.officium.service.auth;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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
 */
public final class AuthRequestInterceptor implements Interceptor {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "AuthRequestInterceptor";

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Provider that can provide authorization token.
	 */
	private final AuthTokenProvider tokenProvider;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of AuthRequestInterceptor with the given authorization <var>tokenProvider</var>.
	 *
	 * @param tokenProvider The desired provider that will be used by the new interceptor to peek
	 *                      the authorization token when intercepting requests.
	 * @see #intercept(Chain)
	 */
	public AuthRequestInterceptor(@NonNull AuthTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public Response intercept(Chain chain) throws IOException {
		final String authToken = tokenProvider.peekToken();
		final Request request = chain.request();
		return TextUtils.isEmpty(authToken) ? chain.proceed(request) : chain.proceed(request.newBuilder()
				.header("Authorization", "Bearer " + authToken)
				.build()
		);
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
