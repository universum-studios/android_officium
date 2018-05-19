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
package universum.studios.android.samples.officium.sync;

import android.support.annotation.NonNull;

import universum.studios.android.samples.officium.event.EventBusProvider;

/**
 * @author Martin Albedinsky
 */
final class SyncEventDispatcher implements SyncAdapter.EventDispatcher {

	@Override public void dispatch(@NonNull final Object event) {
		EventBusProvider.getBusForContext(EventBusProvider.UI).post(event);
	}
}