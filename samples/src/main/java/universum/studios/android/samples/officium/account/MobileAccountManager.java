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

	private static final Object LOCK = new Object();

	private static MobileAccountManager instance;

	private MobileAccountManager(@NonNull final Context applicationContext) {
		super(applicationContext, applicationContext.getString(R.string.config_account_type));
	}

	@NonNull public static MobileAccountManager getInstance(@NonNull final Context context) {
		synchronized (LOCK) {
			if (instance == null) instance = new MobileAccountManager(context.getApplicationContext());
		}
		return instance;
	}

	@Nullable public MobileAccount getUserAccount() {
		final Account account = acquireAccount();
		if (account != null) {
			final MobileAccount userAccount = new MobileAccount(account.name, getAccountPassword(account));
			// todo: userAccount.setDataBundle(getAccountDataBundle(account, MobileAccount.keys));
			return userAccount;
		}
		return null;
	}

	public boolean updateUserAccount(@NonNull final MobileAccount userAccount) {
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