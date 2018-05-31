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
package universum.studios.android.officium.sync;

import android.accounts.Account;
import android.support.annotation.NonNull;

/**
 * Listener that may be used to receive callback whenever a state of a particular {@link SyncTask}
 * is changed.
 *
 * @author Martin Albedinsky
 * @since 1.0
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