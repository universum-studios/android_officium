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

import com.squareup.otto.Bus;

/**
 * An {@link EventBus} implementation that wraps {@link Bus} provided by the <b><a href="http://square.github.io/otto/">Otto</a></b>
 * library. Simple bus need to be created with an instance of Otto's bus via {@link #SimpleEventBus(Bus)}.
 * The wrapped bus may be than obtained via {@link #getBus()} if needed.
 *
 * @author Martin Albedinsky
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
	 *
	 * @deprecated Use {@link #getBus()} instead.
	 */
	@NonNull
	final Bus mBus;

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
		this.mBus = bus;
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
	@NonNull
	public final Bus getBus() {
		return mBus;
	}

	/**
	 * Delegates to {@link Bus#register(Object)}.
	 */
	@Override
	public void register(@NonNull final Object object) {
		mBus.register(object);
	}

	/**
	 * Delegates to {@link Bus#unregister(Object)}.
	 */
	@Override
	public void unregister(@NonNull final Object object) {
		mBus.unregister(object);
	}

	/**
	 * Delegates to {@link Bus#post(Object)}.
	 */
	@Override
	public void post(@NonNull final Object event) {
		mBus.post(event);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
