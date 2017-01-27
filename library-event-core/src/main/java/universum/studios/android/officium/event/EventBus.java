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
package universum.studios.android.officium.event;

import android.support.annotation.NonNull;

/**
 * Unified interface for event bus that may be used to wrap a specific implementation of bus.
 *
 * @author Martin Albedinsky
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
