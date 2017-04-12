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
package universum.studios.android.samples.officium.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import universum.studios.android.officium.sync.BaseSyncAdapter;

/**
 * @author Martin Albedinsky
 */
public final class SyncAdapter extends BaseSyncAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = "SyncAdapter";

	SyncAdapter(@NonNull Context context) {
		super(context.getApplicationContext(), true);
		setOnTaskStateChangeListener(SyncManager.getInstance(context));
		setEventDispatcher(new SyncEventDispatcher());
		setGlobalSyncHandler(new GlobalSyncHandler());
	}
}
