/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("MissingPermission")
public final class BaseSyncManagerTest extends InstrumentedTestCase {

	private static final String TEST_AUTHORITY = "universum.studios.android.officium.Provider";

	@Test
	public void testIsSyncPending() {
		assertThat(new TestManager(mContext, TEST_AUTHORITY).isSyncPedning(), is(false));
	}

	private static final class TestManager extends BaseSyncManager {

		Account accountForSync = new Account("TestName", "TestType");
		boolean onSyncTaskStateChangedInvoked;
		int onSyncTaskStateChangedToState;
		boolean onCancelSyncInvoked;

		TestManager(@NonNull Context context, @NonNull String authority) {
			super(context, authority);
		}

		@Nullable
		@Override
		protected Account pickAccountForSync() {
			return accountForSync;
		}

		@Override
		protected boolean shouldRequestSync(@NonNull SyncTask syncTask, @NonNull Account account) {
			return syncTask.getId() >= 0 && super.shouldRequestSync(syncTask, account);
		}

		@Override
		public void onSyncTaskStateChanged(@NonNull SyncTask syncTask, @NonNull Account account) {
			super.onSyncTaskStateChanged(syncTask, account);
			this.onSyncTaskStateChangedInvoked = true;
			this.onSyncTaskStateChangedToState = syncTask.getState();
		}

		@Override
		protected void onCancelSync(@NonNull Account account) {
			super.onCancelSync(account);
			this.onCancelSyncInvoked = true;
		}
	}
}