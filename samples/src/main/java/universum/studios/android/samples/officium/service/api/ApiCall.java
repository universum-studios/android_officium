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

import universum.studios.android.officium.service.ServiceCall;

import retrofit2.Call;

/**
 * @author Martin Albedinsky
 */
public final class ApiCall<T> extends ServiceCall<T> {

	ApiCall(@NonNull final Call<T> call) {
		super(call);
	}

	@Override public ApiCall<T> withServiceId(final int serviceId) {
		return (ApiCall<T>) super.withServiceId(serviceId);
	}

	@NonNull public String enqueue() {
		return enqueue(new ApiCallback<T>());
	}

	@Override @NonNull protected String nextRequestId() {
		return Long.toString(System.currentTimeMillis());
	}
}