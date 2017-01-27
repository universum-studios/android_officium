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

import android.accounts.Account;
import android.support.annotation.NonNull;

/**
 * Listener that may be used to receive callback whenever a state of a particular {@link SyncTask}
 * is changed.
 *
 * @author Martin Albedinsky
 */
public interface OnSyncTaskStateChangeListener {

	/**
	 * Invoked whenever a state of the specified <var>syncTask</var> has been changed.
	 *
	 * @param syncTask The task of which state has changed.
	 * @param account  The account for which has been requested synchronization associated with the
	 *                 sync task.
	 * @see SyncTask#getState()
	 */
	void onSyncTaskStateChanged(@NonNull SyncTask syncTask, @NonNull Account account);
}
