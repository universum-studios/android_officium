/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import androidx.annotation.NonNull;

/**
 * A {@link SimpleEventBus} implementation that may be used for event buses that should post their
 * events on the Main thread. This event bus implementation uses {@link Handler} with
 * {@link Looper#getMainLooper() main looper} to post the events supplied to {@link #post(Object)}
 * on the Main UI thread.
 *
 * @author Martin Albedinsky
 * @since 1.2
 */
public class MainEventBus extends SimpleEventBus {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "MainEventBus";

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
	 * Handler used to post events on the Main thread.
	 */
	private final Handler handler;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #MainEventBus(Bus)} with default instance of {@link Bus}.
	 */
	public MainEventBus() {
		this(new Bus());
	}

	/**
	 * Creates a new instance of MainEventBus that wraps the specified <var>bus</var>.
	 *
	 * @param bus The bus to be wrapped.
	 * @see #getBus()
	 */
	public MainEventBus(@NonNull final Bus bus) {
		super(bus);
		this.handler = new Handler(Looper.getMainLooper());
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override public void post(@NonNull final Object event) {
		// If caller is posting on the Main thread, delegate directly to the bus, otherwise use handler.
		if (Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
			bus.post(event);
		} else {
			handler.post(new Runnable() {

				/**
				 */
				@Override
				public void run() {
					bus.post(event);
				}
			});
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}