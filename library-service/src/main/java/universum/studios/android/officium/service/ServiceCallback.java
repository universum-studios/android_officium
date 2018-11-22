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
package universum.studios.android.officium.service;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base implementation of {@link Callback} that may be used to receive a response for a particular
 * service request asynchronously and dispatch it to its receivers.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @param <R> Type of the response body to be received by this callback.
 */
public abstract class ServiceCallback<R> extends BaseServiceObject implements Callback<R> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceCallback";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override public void onResponse(@NonNull final Call<R> call, @NonNull final Response<R> response) {
		if (response.isSuccessful()) {
			final R responseBody = response.body();
			if (responseBody instanceof ServiceObject) {
				associateWith((ServiceObject) responseBody, serviceId, requestId);
			}
			onDispatchResponse(responseBody);
		} else {
			final ServiceError error = new ServiceError(response.code(), response.errorBody());
			associateWith(error, serviceId, requestId);
			onDispatchError(error);
		}
	}

	/**
	 * Invoked to dispatch the given <var>responseBody</var> object to its receivers.
	 *
	 * @param responseBody The response body to be dispatched.
	 * @see Response#body()
	 * @see #onDispatchError(ServiceError)
	 */
	protected abstract void onDispatchResponse(@NonNull R responseBody);

	/**
	 */
	@Override public void onFailure(@NonNull final Call<R> call, @NonNull final Throwable failure) {
		final ServiceError error = new ServiceError(failure);
		associateWith(error, serviceId, requestId);
		onDispatchError(error);
	}

	/**
	 * Invoked to dispatch the given <var>error</var> object to its receivers.
	 *
	 * @param error The error to be dispatched.
	 * @see #onDispatchResponse(Object)
	 */
	protected abstract void onDispatchError(@NonNull ServiceError error);

	/*
	 * Inner classes ===============================================================================
	 */
}