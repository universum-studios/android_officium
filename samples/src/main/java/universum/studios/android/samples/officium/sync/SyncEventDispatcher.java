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
package universum.studios.android.samples.officium.sync;

import android.support.annotation.NonNull;

import universum.studios.android.samples.officium.event.EventBusProvider;

/**
 * @author Martin Albedinsky
 */
final class SyncEventDispatcher implements SyncAdapter.EventDispatcher {

	@Override
	public void dispatch(@NonNull Object event) {
		EventBusProvider.getBusForContext(EventBusProvider.UI).post(event);
	}
}
