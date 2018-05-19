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
package universum.studios.android.samples.officium.service.api;

import android.support.annotation.NonNull;

import universum.studios.android.officium.service.ServiceApiProvider;
import universum.studios.android.officium.service.auth.AuthTokenProvider;

/**
 * @author Martin Albedinsky
 */
public final class ApiProvider extends ServiceApiProvider<Api> {

	private static final Object LOCK = new Object();

	private static volatile ApiProvider instance;

	private AuthTokenProvider mAuthTokenProvider;
	private boolean mAuthTokenProviderChanged;

	private ApiProvider() {}

	@NonNull public static ApiProvider get() {
		if (instance == null) {
			synchronized (LOCK) {
				instance = new ApiProvider();
			}
		}
		return instance;
	}

	public void setAuthTokenProvider(@NonNull final AuthTokenProvider tokenProvider) {
		this.mAuthTokenProvider = tokenProvider;
		this.mAuthTokenProviderChanged = true;
	}

	@Override @NonNull protected Api onCreateApi() {
		return new ApiImpl();
	}

	@Override @NonNull protected Api onPrepareApi(@NonNull final Api api) {
		super.onPrepareApi(api);
		if (mAuthTokenProviderChanged) {
			((ApiImpl) api).setAuthTokenProvider(mAuthTokenProvider);
		}
		return api;
	}
}