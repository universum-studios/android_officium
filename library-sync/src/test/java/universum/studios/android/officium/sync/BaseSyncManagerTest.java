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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.hamcrest.core.Is;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("MissingPermission")
public final class BaseSyncManagerTest extends RobolectricTestCase {

	private static final String TEST_AUTHORITY = "universum.studios.android.officium.Provider";

	@Test public void testInstantiation() {
		// Act:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		// Assert:
		assertThat(manager.getContext(), Is.<Context>is(application));
		assertThat(manager.getAuthority(), is(TEST_AUTHORITY));
	}

	@Test public void testStartAutomaticSync() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		// Act:
		manager.startAutomaticSync();
		// Assert:
		assertThat(manager.isAutomaticSyncRunning(), is(true));
	}

	@Test public void testStartAutomaticSyncWithoutAccount() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.accountForSync = null;
		// Act:
		manager.startAutomaticSync();
		// Assert:
		assertThat(manager.isAutomaticSyncRunning(), is(false));
	}

	@Test public void testStopAutomaticSync() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.startAutomaticSync();
		// Act:
		manager.stopAutomaticSync();
		// Assert:
		assertThat(manager.isAutomaticSyncRunning(), is(false));
	}

	@Test public void testStopAutomaticSyncWithoutAccount() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.accountForSync = null;
		manager.startAutomaticSync();
		// Act:
		manager.stopAutomaticSync();
		// Assert:
		assertThat(manager.isAutomaticSyncRunning(), is(false));
	}

	@Test public void testRequestGlobalSync() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		// Act:
		manager.requestGlobalSync();
		// Assert:
		assertThat(manager.onSyncTaskStateChangedInvoked, is(true));
		assertThat(manager.onSyncTaskStateChangedToState, is(SyncTask.PENDING));
	}

	@Test public void testRequestSync() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		final SyncTask syncTask = new SyncTask.Builder<>(1).build();
		// Act:
		manager.requestSync(syncTask);
		// Assert:
		assertThat(manager.onSyncTaskStateChangedInvoked, is(true));
		assertThat(manager.onSyncTaskStateChangedToState, is(SyncTask.PENDING));
		assertThat(syncTask.getState(), is(SyncTask.PENDING));
	}

	@Test public void testRequestSyncForTaskWhichShouldNotRun() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		final SyncTask syncTask = new SyncTask.Builder<>(-1).build();
		// Act:
		manager.requestSync(syncTask);
		// Assert:
		assertThat(manager.onSyncTaskStateChangedInvoked, is(false));
		assertThat(syncTask.getState(), is(SyncTask.IDLE));
	}

	@Test public void testRequestSyncWithoutAccount() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.accountForSync = null;
		// Act:
		manager.requestSync(new SyncTask.Builder<>(SyncTask.DEFAULT_ID).build());
		// Assert:
		assertThat(manager.onSyncTaskStateChangedInvoked, is(false));
	}

	@Test public void testShouldRequestSync() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		// Act:
		final boolean result = manager.shouldRequestSync(new SyncTask.Builder<>(SyncTask.DEFAULT_ID).build(), manager.accountForSync);
		// Assert:
		assertThat(result, is(true));
	}

	@Test public void testIsSyncPendingWithoutAccount() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.accountForSync = null;
		// Act + Assert:
		assertThat(manager.isSyncPending(), is(false));
	}

	@Test public void testIsSyncActive() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		// Act + Assert:
		assertThat(manager.isSyncActive(), is(false));
	}

	@Test public void testIsSyncActiveWithoutAccount() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.accountForSync = null;
		// Act + Assert:
		assertThat(manager.isSyncActive(), is(false));
	}

	@Test public void testCancelSync() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		// Act:
		manager.cancelSync();
		// Assert:
		assertThat(manager.onCancelSyncInvoked, is(true));
	}

	@Test public void testCancelSyncWithoutAccount() {
		// Arrange:
		final TestManager manager = new TestManager(application, TEST_AUTHORITY);
		manager.accountForSync = null;
		// Act:
		manager.cancelSync();
		// Assert:
		assertThat(manager.onCancelSyncInvoked, is(false));
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