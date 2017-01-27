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
package universum.studios.android.officium.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import universum.studios.android.officium.OfficiumConfig;

/**
 * Base class that can be used for implementation of managers responsible for requesting of synchronization
 * operations to be performed. Each implementation of BaseSyncManager can request synchronization
 * only for a single <b>content authority</b> specified during initialization of BaseSyncManager and
 * for a single {@link Account} that should be picked by a specific BaseSyncManager implementation
 * whenever {@link #pickAccountForSync()} is called.
 * <p>
 * To request synchronization for a specific {@link SyncTask} call {@link #requestSync(SyncTask)}
 * method. If a global synchronization should be performed, call {@link #requestGlobalSync()}.
 * Base synchronization manager class implements also some configuration methods like to start/stop
 * automatic synchronization via {@link #startAutomaticSync()} and {@link #stopAutomaticSync()} or
 * to check whether there are some synchronization operations pending or active via {@link #isSyncPedning()}
 * and {@link #isSyncPedning()}. Any additional methods may be freely implemented by the inheritance
 * hierarchies.
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResourceType")
public abstract class BaseSyncManager implements OnSyncTaskStateChangeListener {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "BaseSyncManager";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Context with which has been this manager created.
	 */
	protected final Context mContext;

	/**
	 * Content authority with which has been this manager created. This authority is used to set up
	 * all synchronization requests via {@link ContentResolver}.
	 */
	protected final String mAuthority;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of BaseSyncManager with the given <var>context</var> and <var>authority</var>.
	 *
	 * @param context   Context that may be used by inheritance hierarchies to access application
	 *                  data and services needed to perform requested synchronization.
	 * @param authority The content authority for which the manager should request synchronization
	 *                  via {@link ContentResolver#requestSync(Account, String, Bundle)}.
	 */
	public BaseSyncManager(@NonNull Context context, @NonNull String authority) {
		this.mContext = context;
		this.mAuthority = authority;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Starts automatic synchronization for the content authority specified for this manager and
	 * account picked for synchronization by this manager implementation.
	 *
	 * @see #stopAutomaticSync()
	 * @see ContentResolver#setSyncAutomatically(Account, String, boolean)
	 */
	public void startAutomaticSync() {
		setAutomaticSyncEnabled(true);
	}

	/**
	 * Stops automatic synchronization for the content authority specified for this manager and
	 * account picked for synchronization by this manager implementation.
	 *
	 * @see #startAutomaticSync()
	 * @see ContentResolver#setSyncAutomatically(Account, String, boolean)
	 */
	public void stopAutomaticSync() {
		setAutomaticSyncEnabled(false);
	}

	/**
	 * Enables/disables automatic synchronization via {@link ContentResolver#setSyncAutomatically(Account, String, boolean)}.
	 *
	 * @param enabled {@code True} to enable automatic synchronization, {@code false} to disable it.
	 */
	private void setAutomaticSyncEnabled(boolean enabled) {
		final Account account = pickAccountForSync();
		if (account != null) ContentResolver.setSyncAutomatically(account, mAuthority, enabled);
	}

	/**
	 * Same as {@link #requestSync(SyncTask)} with sync task with {@link SyncTask#DEFAULT_ID} id.
	 */
	public void requestGlobalSync() {
		requestSync(new SyncTask.Builder<>(SyncTask.DEFAULT_ID).build());
	}

	/**
	 * Requests synchronization operation to be performed for the specified <var>syncTask</var>.
	 * Synchronization will be requested for the content authority specified for this manager and
	 * account picked for synchronization by this manager implementation.
	 * <p>
	 * If there is successfully picked synchronization account, a {@link Bundle} for sync adapter
	 * is created with the following extras:
	 * <ul>
	 * <li>{@link ContentResolver#SYNC_EXTRAS_MANUAL SYNC_EXTRAS_MANUAL}: <b>true</b></li>
	 * <li>{@link ContentResolver#SYNC_EXTRAS_EXPEDITED SYNC_EXTRAS_EXPEDITED}: <b>true</b></li>
	 * <li>{@link SyncTask} put into the extras Bundle</li>
	 * </ul>
	 *
	 * @param syncTask The desired task for which to request synchronization. This task will be put
	 *                 into {@link Bundle} along with other sync extras for the sync adapter registered
	 *                 for this Android application.
	 * @see #requestGlobalSync()
	 * @see ContentResolver#requestSync(Account, String, Bundle)
	 */
	public void requestSync(@NonNull SyncTask syncTask) {
		final Account account = pickAccountForSync();
		if (account != null) {
			if (shouldRequestSync(syncTask, account)) {
				syncTask.setState(SyncTask.PENDING);
				onSyncTaskStateChanged(syncTask, account);
				final Bundle extras = syncTask.intoExtras(new Bundle());
				extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
				extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
				if (OfficiumConfig.LOG_ENABLED) {
					Log.v(TAG, "Requesting synchronization for task(" + syncTask + ").");
				}
				ContentResolver.requestSync(account, mAuthority, extras);
			}
		} else if (OfficiumConfig.LOG_ENABLED) {
			Log.v(TAG, "Cannot perform synchronization for task(" + syncTask + "). No account picked for synchronization.");
		}
	}

	/**
	 * Called from {@link #requestSync(SyncTask)} to check whether a synchronization should be requested
	 * for the specified <var>syncTask</var> and <var>account</var>.
	 * <p>
	 * This implementation returns always {@code true}.
	 *
	 * @param syncTask The sync task for which to check if synchronization should be requested.
	 * @param account  The account for which to request synchronization.
	 * @return {@code True} if {@link #requestSync(SyncTask)} should proceed and request synchronization
	 * via {@link ContentResolver#requestSync(Account, String, Bundle)} for the task and account,
	 * {@code false} otherwise.
	 * @see #pickAccountForSync()
	 */
	protected boolean shouldRequestSync(@NonNull SyncTask syncTask, @NonNull Account account) {
		return true;
	}

	/**
	 */
	@Override
	@CallSuper
	public void onSyncTaskStateChanged(@NonNull SyncTask syncTask, @NonNull Account account) {
		// May be implemented by the inheritance hierarchies.
	}

	/**
	 * Checks whether there is currently a synchronization being processed for the content authority
	 * specified for this manager and account picked for synchronization by this manager implementation.
	 *
	 * @return {@code True} if synchronization is active at this time, {@code false} otherwise.
	 * @see ContentResolver#isSyncActive(Account, String)
	 * @see #isSyncPedning()
	 * @see #cancelSync()
	 */
	public boolean isSyncActive() {
		final Account account = pickAccountForSync();
		return account != null && ContentResolver.isSyncActive(account, mAuthority);
	}

	/**
	 * Checks whether there are any pending synchronizations for the content authority specified for
	 * this manager and account picked for synchronization by this manager implementation.
	 *
	 * @return {@code True} if there are some synchronizations pending, {@code false} otherwise.
	 * @see ContentResolver#isSyncPending(Account, String)
	 * @see #isSyncActive()
	 * @see #cancelSync()
	 */
	public boolean isSyncPedning() {
		final Account account = pickAccountForSync();
		return account != null && ContentResolver.isSyncPending(account, mAuthority);
	}

	/**
	 * Cancels any active or pending synchronizations that match the content authority specified for
	 * this manager and account picked for synchronization by this manager implementation.
	 *
	 * @see ContentResolver#cancelSync(Account, String)
	 * @see #isSyncActive()
	 * @see #isSyncPedning()
	 */
	public void cancelSync() {
		final Account account = pickAccountForSync();
		if (account != null) onCancelSync(account);
	}

	/**
	 * Invoked whenever {@link #cancelSync()} is called.
	 * <p>
	 * Default implementation cancels current sync via {@link ContentResolver#cancelSync(Account, String)}
	 * for the specified account and authority specified for this manager.
	 *
	 * @param account The account picked via {@link #pickAccountForSync()}.
	 */
	protected void onCancelSync(@NonNull Account account) {
		ContentResolver.cancelSync(account, mAuthority);
	}

	/**
	 * Called to pick account for the current synchronization related request dispatched to this
	 * manager.
	 *
	 * @return Account for which should be performed synchronization request, whether {@link #startAutomaticSync()},
	 * {@link #stopAutomaticSync()}, {@link #requestGlobalSync()}, {@link #requestSync(SyncTask)},
	 * {@link #isSyncActive()}, {@link #isSyncPedning()}, {@link #cancelSync()}.
	 */
	@Nullable
	protected abstract Account pickAccountForSync();

	/**
	 * Inner classes ===============================================================================
	 */
}
