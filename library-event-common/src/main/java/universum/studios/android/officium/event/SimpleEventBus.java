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

import com.squareup.otto.Bus;

/**
 * An {@link EventBus} implementation that wraps {@link Bus} provided by the <b><a href="http://square.github.io/otto/">Otto</a></b>
 * library. Simple bus need to be created with an instance of Otto's bus via {@link #SimpleEventBus(Bus)}.
 * The wrapped bus may be than obtained via {@link #getBus()} if needed.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class SimpleEventBus implements EventBus {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SimpleEventBus";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * The bus used by this event bus wrapper.
	 */
	@NonNull final Bus bus;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #SimpleEventBus(Bus)} with default instance of {@link Bus}.
	 */
	public SimpleEventBus() {
		this(new Bus());
	}

	/**
	 * Creates a new instance of SimpleEventBus that wraps the specified <var>bus</var>.
	 *
	 * @param bus The bus to be wrapped.
	 * @see #getBus()
	 */
	public SimpleEventBus(@NonNull final Bus bus) {
		this.bus = bus;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the original bus wrapped.
	 *
	 * @return Bus wrapped by this event bus wrapper.
	 * @see #SimpleEventBus(Bus)
	 */
	@NonNull public final Bus getBus() {
		return bus;
	}

	/**
	 * Delegates to {@link Bus#register(Object)}.
	 */
	@Override public void register(@NonNull final Object object) {
		this.bus.register(object);
	}

	/**
	 * Delegates to {@link Bus#unregister(Object)}.
	 */
	@Override public void unregister(@NonNull final Object object) {
		this.bus.unregister(object);
	}

	/**
	 * Delegates to {@link Bus#post(Object)}.
	 */
	@Override public void post(@NonNull final Object event) {
		this.bus.post(event);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}