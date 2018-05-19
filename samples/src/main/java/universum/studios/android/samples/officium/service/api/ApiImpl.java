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

import universum.studios.android.officium.service.ServiceApi;
import universum.studios.android.officium.service.ServiceManager;
import universum.studios.android.officium.service.auth.AuthTokenProvider;
import universum.studios.android.samples.officium.service.request.ApiSignInRequest;
import universum.studios.android.samples.officium.service.response.ApiSignInResponse;

/**
 * @author Martin Albedinsky
 */
final class ApiImpl extends ServiceApi<ServiceManager> implements Api {

	ApiImpl() {
		super(new ServiceManager(END_POINT));
		final ServiceManager.ServicesConfiguration configuration = mManager.servicesConfiguration(Services.class);
		configuration.retrofitBuilder();
		configuration.retrofit();
		configuration.invalidate();
	}

	void setAuthTokenProvider(@NonNull final AuthTokenProvider tokenProvider) {
		// todo: servicesConfiguration(Services.class).requestInterceptor(new AuthRequestInterceptor(tokenProvider));
	}

	@Override @NonNull public ApiCall<ApiSignInResponse> signIn(@NonNull final ApiSignInRequest request) {
		return new ApiCall<>(services(Services.class).signIn(request)).withServiceId(SIGN_IN);
	}
}