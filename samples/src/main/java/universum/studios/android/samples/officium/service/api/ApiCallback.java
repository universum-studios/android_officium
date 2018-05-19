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

import universum.studios.android.samples.officium.event.EventBusProvider;
import universum.studios.android.officium.service.ServiceCallback;
import universum.studios.android.officium.service.ServiceError;

/**
 * @author Martin Albedinsky
 */
public final class ApiCallback<R> extends ServiceCallback<R> {

	@Override protected void onDispatchResponse(@NonNull final R responseBody) {
		EventBusProvider.getBusForContext(EventBusProvider.UI).post(responseBody);
	}

	@Override protected void onDispatchError(@NonNull final ServiceError error) {
		EventBusProvider.getBusForContext(EventBusProvider.UI).post(error);
	}
}