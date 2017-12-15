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

	@SuppressWarnings("unused")
	private static final String TAG = "SyncManager";
	private static final Object LOCK = new Object();

	private static volatile SyncManager instance;

	private SyncManager(@NonNull Context context) {
		super(context.getApplicationContext(), context.getString(R.string.config_account_authority));
	}

	@NonNull
	public static SyncManager getInstance(@NonNull Context context) {
		if (instance == null) {
			synchronized (LOCK) {
				instance = new SyncManager(context);
			}
		}
		return instance;
	}

	@Nullable
	@Override
	@SuppressWarnings("MissingPermission")
	protected Account pickAccountForSync() {
		return MobileAccountManager.getInstance(getContext()).getAccount();
	}
}
