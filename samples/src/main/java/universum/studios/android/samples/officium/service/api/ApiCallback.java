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

import universum.studios.android.samples.officium.event.EventBusProvider;
import universum.studios.android.officium.service.ServiceCallback;
import universum.studios.android.officium.service.ServiceError;

/**
 * @author Martin Albedinsky
 */
public final class ApiCallback<R> extends ServiceCallback<R> {

	@SuppressWarnings("unused")
	private static final String TAG = "ApiCallback";

	@Override
	protected void onDispatchResponse(@NonNull R responseBody) {
		EventBusProvider.getBusForContext(EventBusProvider.UI).post(responseBody);
	}

	@Override
	protected void onDispatchError(@NonNull ServiceError error) {
		EventBusProvider.getBusForContext(EventBusProvider.UI).post(error);
	}
}
