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

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import universum.studios.android.officium.sync.BaseSyncManager;
import universum.studios.android.samples.officium.R;
import universum.studios.android.samples.officium.account.MobileAccountManager;

/**
 * @author Martin Albedinsky
 */
@SuppressLint("StaticFieldLeak")
public final class SyncManager extends BaseSyncManager {

	private static final Object LOCK = new Object();

	private static volatile SyncManager instance;

	private SyncManager(@NonNull final Context context) {
		super(context.getApplicationContext(), context.getString(R.string.config_account_authority));
	}

	@NonNull public static SyncManager getInstance(@NonNull final Context context) {
		if (instance == null) {
			synchronized (LOCK) {
				instance = new SyncManager(context);
			}
		}
		return instance;
	}

	@Override @Nullable protected Account pickAccountForSync() {
		return MobileAccountManager.getInstance(getContext()).getAccount();
	}
}