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
package universum.studios.android.officium.event;

import android.support.annotation.NonNull;

/**
 * Unified interface for event bus that may be used to wrap a specific implementation of bus.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public interface EventBus {

	/**
	 * Registers the specified <var>object</var> as handler and producer on this event bus.
	 *
	 * @param object The desired object to register.
	 * @see #unregister(Object)
	 */
	void register(@NonNull Object object);

	/**
	 * Unregisters the specified <var>object</var> as handler and producer from this event bus.
	 *
	 * @param object The desired object to unregister.
	 * @see #register(Object)
	 */
	void unregister(@NonNull Object object);

	/**
	 * Posts the specified <var>event</var> to all handlers registered on this event bus.
	 *
	 * @param event The desired event to post.
	 */
	void post(@NonNull Object event);
}