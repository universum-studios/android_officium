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
package universum.studios.android.samples.officium.service.api;

import android.support.annotation.NonNull;

import universum.studios.android.officium.service.ServiceApiProvider;
import universum.studios.android.officium.service.auth.AuthTokenProvider;

/**
 * @author Martin Albedinsky
 */
public final class ApiProvider extends ServiceApiProvider<Api> {

	@SuppressWarnings("unused")
	private static final String TAG = "ApiProvider";
	private static final Object LOCK = new Object();

	private static ApiProvider instance;

	private AuthTokenProvider mAuthTokenProvider;
	private boolean mAuthTokenProviderChanged;

	private ApiProvider() {
	}

	@NonNull
	public static ApiProvider getInstance() {
		synchronized (LOCK) {
			if (instance == null) instance = new ApiProvider();
		}
		return instance;
	}

	public void setAuthTokenProvider(@NonNull AuthTokenProvider tokenProvider) {
		this.mAuthTokenProvider = tokenProvider;
		this.mAuthTokenProviderChanged = true;
	}

	@NonNull
	@Override
	protected Api onCreateApi() {
		return new ApiImpl();
	}

	@NonNull
	@Override
	protected Api onPrepareApi(@NonNull Api api) {
		super.onPrepareApi(api);
		if (mAuthTokenProviderChanged) {
			((ApiImpl) api).setAuthTokenProvider(mAuthTokenProvider);
		}
		return api;
	}
}
