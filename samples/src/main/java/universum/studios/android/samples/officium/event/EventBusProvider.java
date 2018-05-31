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
package universum.studios.android.samples.officium.event;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.officium.event.EventBus;
import universum.studios.android.officium.event.MainEventBus;
import universum.studios.android.officium.event.SimpleEventBus;

/**
 * @author Martin Albedinsky
 */
public final class EventBusProvider {

	@IntDef({MAIN, SERVICES})
	@Retention(RetentionPolicy.SOURCE)
	public @interface BusContext {}

	public static final int MAIN = 0x00;
	public static final int SERVICES = 0x01;

	private static final SparseArray<EventBus> buses = new SparseArray<>(2);

	@NonNull public static EventBus getBusForContext(@BusContext final int busContext) {
		EventBus bus;
		synchronized (buses) {
			bus = buses.get(busContext);
			if (bus == null) {
				switch (busContext) {
					case MAIN:
						bus = new MainEventBus();
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