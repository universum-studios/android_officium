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

import android.support.annotation.Nullable;

/**
 * Interface for providers that can provide an instance of authorization token.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
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
		@Override @Nullable public String peekToken() {
			return null;
		}
	};

	/**
	 * Returns the auth token provided by this token provided.
	 *
	 * @return Peeked authorization token or {@code null} if there is no token available at this time.
	 */
	@Nullable String peekToken();
}