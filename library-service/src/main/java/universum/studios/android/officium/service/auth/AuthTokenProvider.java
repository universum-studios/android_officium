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

import android.support.annotation.Nullable;

/**
 * Interface for providers that can provide an instance of authorization token.
 *
 * @author Martin Albedinsky
 * @see AuthRequestInterceptor
 */
public interface AuthTokenProvider {

	/**
	 * Provider instance that may be used for <b>unauthorized</b> requests that does not require
	 * authorization token to be send to an application server.
	 * <p>
	 * This provider instance simple returns {@code null} whenever its {@link #peekToken()} method
	 * is invoked.
	 */
	AuthTokenProvider UNAUTHORIZED = new AuthTokenProvider() {

		/**
		 */
		@Nullable
		@Override
		public String peekToken() {
			return null;
		}
	};

	/**
	 * Returns the auth token provided by this token provided.
	 *
	 * @return Peeked authorization token or {@code null} if there is no token available at this time.
	 */
	@Nullable
	String peekToken();
}
