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
package universum.studios.android.officium.sync;

import android.os.Bundle;

/**
 * Class specifying keys for synchronization extras {@link Bundle}.
 *
 * @author Martin Albedinsky
 */
final class SyncExtras {

	/**
	 * Prefix for synchronization extra key.
	 */
	private static final String PREFIX = SyncExtras.class.getPackage().getName() + ".EXTRA.";

	/**
	 * Key used to store id of synchronization task within extras {@link Bundle}.
	 */
	static final String EXTRA_TASK_ID = PREFIX + "Task.Id";

	/**
	 * Key used to store state of synchronization task within extras {@link Bundle}.
	 */
	static final String EXTRA_TASK_STATE = PREFIX + "Task.State";

	/**
	 * Key used to store request body of synchronization task within extras {@link Bundle}.
	 */
	static final String EXTRA_TASK_REQUEST_BODY = PREFIX + "Task.RequestBody";

	/**
	 */
	private SyncExtras() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}
}
