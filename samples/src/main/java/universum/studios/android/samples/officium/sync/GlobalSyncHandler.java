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
import android.support.annotation.Nullable;

import universum.studios.android.officium.sync.SyncHandler;
import universum.studios.android.officium.sync.SyncOperation;
import universum.studios.android.officium.sync.SyncTask;

/**
 * @author Martin Albedinsky
 */
public final class GlobalSyncHandler extends SyncHandler<SyncTask.EmptyRequest, Boolean> {

	@SuppressWarnings("unused")
	private static final String TAG = "GlobalSyncHandler";

	protected GlobalSyncHandler() {
		super(SyncTask.DEFAULT_ID);
	}

	@Nullable
	@Override
	protected Boolean onHandleSync(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable SyncTask.EmptyRequest syncRequest) throws Exception {
		return false;
	}

	@Override
	protected void onSyncError(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable SyncTask.EmptyRequest syncRequest, @NonNull Exception error) {
		super.onSyncError(context, syncOperation, syncRequest, error);
	}
}
