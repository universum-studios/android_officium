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
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestResources;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings("MissingPermission")
public final class BaseSyncManagerTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "BaseSyncManagerTest";
	private static final String TEST_AUTHORITY = "universum.studios.android.officium.Provider";

	private String mAuthority = TEST_AUTHORITY;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		if (TestUtils.hasLibraryRootPackageName(mContext)) {
			this.mAuthority = mContext.getString(TestResources.resourceIdentifier(mContext, TestResources.STRING, "config_data_authority"));
		}
	}

	@Test
	public void testInstantiation() {
		final TestManager manager = new TestManager(mContext, mAuthority);
	    assertThat(manager.getContext(), is(mContext));
	    assertThat(manager.getAuthority(), is(mAuthority));
    }

    @Test
    public void testStartAutomaticSync() {
	    final TestManager manager = new TestManager(mContext, mAuthority);
	    manager.startAutomaticSync();
	    assertThat(manager.isAutomaticSyncRunning(), is(true));
    }

    @Test
    public void testStartAutomaticSyncWithoutAccount() {
	    final TestManager manager = new TestManager(mContext, mAuthority);
	    manager.accountForSync = null;
	    manager.startAutomaticSync();
	    assertThat(manager.isAutomaticSyncRunning(), is(false));
    }

    @Test
    public void testStopAutomaticSync() {
	    final TestManager manager = new TestManager(mContext, mAuthority);
	    manager.startAutomaticSync();
	    manager.stopAutomaticSync();
	    assertThat(manager.isAutomaticSyncRunning(), is(false));
    }

    @Test
    public void testStopAutomaticSyncWithoutAccount() {
	    final TestManager manager = new TestManager(mContext, mAuthority);
	    manager.accountForSync = null;
	    manager.startAutomaticSync();
	    manager.stopAutomaticSync();
	    assertThat(manager.isAutomaticSyncRunning(), is(false));
    }

	@Test
	public void testRequestGlobalSync() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		manager.requestGlobalSync();
		assertThat(manager.onSyncTaskStateChangedInvoked, is(true));
		assertThat(manager.onSyncTaskStateChangedToState, is(SyncTask.PENDING));
	}

	@Test
	public void testRequestSync() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		final SyncTask syncTask = new SyncTask.Builder<>(1).build();
		manager.requestSync(syncTask);
		assertThat(manager.onSyncTaskStateChangedInvoked, is(true));
		assertThat(manager.onSyncTaskStateChangedToState, is(SyncTask.PENDING));
		assertThat(syncTask.getState(), is(SyncTask.PENDING));
	}

	@Test
	public void testRequestSyncForTaskWhichShouldNotRun() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		final SyncTask syncTask = new SyncTask.Builder<>(-1).build();
		manager.requestSync(syncTask);
		assertThat(manager.onSyncTaskStateChangedInvoked, is(false));
		assertThat(syncTask.getState(), is(SyncTask.IDLE));
	}

	@Test
	public void testRequestSyncWithoutAccount() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		manager.accountForSync = null;
		manager.requestSync(new SyncTask.Builder<>(SyncTask.DEFAULT_ID).build());
		assertThat(manager.onSyncTaskStateChangedInvoked, is(false));
	}

	@Test
	public void testShouldRequestSync() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		assertThat(manager.shouldRequestSync(
				new SyncTask.Builder<>(SyncTask.DEFAULT_ID).build(),
				manager.accountForSync
		), is(true));
	}

	@Test
	public void testIsSyncPending() {
		assertThat(new TestManager(mContext, mAuthority).isSyncPedning(), is(false));
	}

	@Test
	public void testIsSyncPendingWithoutAccount() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		manager.accountForSync = null;
		assertThat(manager.isSyncPedning(), is(false));
	}

	@Test
	public void testIsSyncActive() {
		assertThat(new TestManager(mContext, mAuthority).isSyncActive(), is(false));
	}

	@Test
	public void testIsSyncActiveWithoutAccount() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		manager.accountForSync = null;
		assertThat(manager.isSyncActive(), is(false));
	}

	@Test
	public void testCancelSync() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		manager.cancelSync();
		assertThat(manager.onCancelSyncInvoked, is(true));
	}

	@Test
	public void testCancelSyncWithoutAccount() {
		final TestManager manager = new TestManager(mContext, mAuthority);
		manager.accountForSync = null;
		manager.cancelSync();
		assertThat(manager.onCancelSyncInvoked, is(false));
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
