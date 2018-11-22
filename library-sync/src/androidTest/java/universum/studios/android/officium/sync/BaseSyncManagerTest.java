/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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
import android.content.Context;

import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("MissingPermission")
public final class BaseSyncManagerTest extends InstrumentedTestCase {

	private static final String TEST_AUTHORITY = "universum.studios.android.officium.Provider";

	@Test public void testIsSyncPending() {
		// Arrange:
		final TestManager manager = new TestManager(context, TEST_AUTHORITY);
		// Act + Assert:
		assertThat(manager.isSyncPending(), is(false));
	}

	private static final class TestManager extends BaseSyncManager {

		Account accountForSync = new Account("TestName", "TestType");
		boolean onSyncTaskStateChangedInvoked;
		int onSyncTaskStateChangedToState;
		boolean onCancelSyncInvoked;

		TestManager(@NonNull final Context context, @NonNull final String authority) {
			super(context, authority);
		}

		@Override @Nullable protected Account pickAccountForSync() {
			return accountForSync;
		}

		@Override protected boolean shouldRequestSync(@NonNull final SyncTask syncTask, @NonNull final Account account) {
			return syncTask.getId() >= 0 && super.shouldRequestSync(syncTask, account);
		}

		@Override public void onSyncTaskStateChanged(@NonNull final SyncTask syncTask, @NonNull final Account account) {
			super.onSyncTaskStateChanged(syncTask, account);
			this.onSyncTaskStateChangedInvoked = true;
			this.onSyncTaskStateChangedToState = syncTask.getState();
		}

		@Override protected void onCancelSync(@NonNull final Account account) {
			super.onCancelSync(account);
			this.onCancelSyncInvoked = true;
		}
	}
}