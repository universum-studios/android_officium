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

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.squareup.otto.Bus;

/**
 * A {@link SimpleEventBus} implementation that may be used for event buses that should post their
 * events on the UI thread. This event bus implementation uses {@link Handler} with
 * {@link Looper#getMainLooper() main looper} to post the events supplied to {@link #post(Object)}
 * on the Main UI thread.
 *
 * @author Martin Albedinsky
 *
 * @deprecated Use {@link MainEventBus} instead.
 */
@Deprecated
public class UiEventBus extends MainEventBus {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "UiEventBus";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #UiEventBus(Bus)} with default instance of {@link Bus}.
	 */
	public UiEventBus() {
		this(new Bus());
	}

	/**
	 * Creates a new instance of UiEventBus that wraps the specified <var>bus</var>.
	 *
	 * @param bus The bus to be wrapped.
	 * @see #getBus()
	 */
	public UiEventBus(@NonNull final Bus bus) {
		super(bus);
	}

	/*
	 * Methods =====================================================================================
	 */

	/*
	 * Inner classes ===============================================================================
	 */
}
