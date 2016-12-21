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
package universum.studios.android.samples.officium.event;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.officium.event.EventBus;
import universum.studios.android.officium.event.SimpleEventBus;
import universum.studios.android.officium.event.UiEventBus;

/**
 * @author Martin Albedinsky
 */
public final class EventBusProvider {

	@SuppressWarnings("unused")
	private static final String TAG = "EventBusProvider";

	@IntDef({UI, SERVICES})
	@Retention(RetentionPolicy.SOURCE)
	public @interface BusContext {}

	public static final int UI = 0x00;
	public static final int SERVICES = 0x01;

	private static final SparseArray<EventBus> buses = new SparseArray<>(2);

	@NonNull
	public static EventBus getBusForContext(@BusContext int busContext) {
		EventBus bus;
		synchronized (buses) {
			bus = buses.get(busContext);
			if (bus == null) {
				switch (busContext) {
					case UI:
						bus = new UiEventBus();
						break;
					default:
						bus = new SimpleEventBus();
						break;
				}
				buses.put(busContext, bus);
			}
		}
		return bus;
	}
}
