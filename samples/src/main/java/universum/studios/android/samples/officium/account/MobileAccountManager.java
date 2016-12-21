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
package universum.studios.android.samples.officium.account;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import universum.studios.android.officium.account.SingleUserAccountManager;
import universum.studios.android.samples.officium.R;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings({"MissingPermission", "StaticFieldLeak"})
public final class MobileAccountManager extends SingleUserAccountManager<MobileAccount> {

	@SuppressWarnings("unused")
	private static final String TAG = "MobileAccountManager";
	private static final Object LOCK = new Object();

	private static MobileAccountManager instance;

	private MobileAccountManager(@NonNull Context applicationContext) {
		super(applicationContext, applicationContext.getString(R.string.config_account_type));
	}

	@NonNull
	public static MobileAccountManager getInstance(@NonNull Context context) {
		synchronized (LOCK) {
			if (instance == null) instance = new MobileAccountManager(context.getApplicationContext());
		}
		return instance;
	}

	@Nullable
	public MobileAccount getUserAccount() {
		final Account account = acquireAccount();
		if (account != null) {
			final MobileAccount userAccount = new MobileAccount(account.name, getAccountPassword(account));
			// todo: userAccount.setDataBundle(getAccountDataBundle(account, MobileAccount.keys));
			return userAccount;
		}
		return null;
	}

	public boolean updateUserAccount(@NonNull MobileAccount userAccount) {
		final Bundle dataBundle = userAccount.getDataBundle();
		if (dataBundle != null) {
			final Account account = acquireAccount();
			if (account != null) {
				setAccountDataBundle(account, dataBundle);
				return true;
			}
		}
		return false;
	}
}
