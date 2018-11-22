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
package universum.studios.android.samples.officium.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.officium.sync.SyncHandler;
import universum.studios.android.officium.sync.SyncOperation;
import universum.studios.android.officium.sync.SyncTask;

/**
 * @author Martin Albedinsky
 */
public final class GlobalSyncHandler extends SyncHandler<SyncTask.EmptyRequest, Boolean> {

	GlobalSyncHandler() {
		super(SyncTask.DEFAULT_ID);
	}

	@Override @NonNull protected Boolean onHandleSync(
			@NonNull final Context context,
			@NonNull final SyncOperation syncOperation,
			@Nullable final SyncTask.EmptyRequest syncRequest
	) throws Exception {
		return false;
	}

	@Override protected void onSyncError(
			@NonNull final Context context,
			@NonNull final SyncOperation syncOperation,
			@Nullable final SyncTask.EmptyRequest syncRequest,
			@NonNull final Exception error
	) {
		super.onSyncError(context, syncOperation, syncRequest, error);
	}
}