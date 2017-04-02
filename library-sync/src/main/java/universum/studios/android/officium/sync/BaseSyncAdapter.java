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
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import universum.studios.android.officium.OfficiumConfig;

/**
 * An {@link AbstractThreadedSyncAdapter} implementation that provides simple API for registration
 * of {@link SyncHandler SyncHandlers} that are responsible for synchronization tasks handling. Each
 * synchronization handler can be registered via {@link #registerTaskHandler(SyncHandler)}. Every
 * registered handler is mapped within BaseSyncAdapter based on the id ({@link SyncHandler#getTaskId()})
 * of a task for which handling is such handler responsible. Each handler may be responsible only for
 * single task handling. To unregister already registered handler, call {@link #unregisterTaskHandler(SyncHandler)}.
 * <p>
 * This class is closely connected with {@link SyncTask} as instance of BaseSyncAdapter assumes that
 * extras {@link Bundle} received in {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}
 * contains data from which can be re-created instance of SyncTask and than that instance of task
 * passed to its associated handler. For simplified synchronization requesting can be used implementation
 * of {@link BaseSyncManager} that handles putting of a specific instance of SyncTask into extras
 * Bundle that is than by the Android framework delivered to implementation of this BaseSyncAdapter.
 * <p>
 * Whenever {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)} method
 * is invoked by the Android framework, BaseSyncAdapter checks the received extras Bundle if it
 * contains data for SyncTask instantiation. If yes, an instance of SyncTask is created from that
 * extras and the associated SyncHandler is retrieved and requested to handle synchronization
 * via {@link SyncHandler#handleSync(Context, SyncOperation)} with that instance of SyncTask.
 * If the received extras Bundle does not contain data for SyncTask or contains data for task with
 * default id ({@link SyncTask#DEFAULT_ID}), {@link #onPerformGlobalSync(SyncOperation)}
 * method is invoked to perform global synchronization. Handler that should handle this global
 * synchronization can be specified via {@link #setGlobalSyncHandler(SyncHandler)}.
 *
 * <h3>Sync task state change listening</h3>
 * To listen for changes in state of {@link SyncTask SyncTasks} within the sync adapter implementation,
 * register {@link OnSyncTaskStateChangeListener} via {@link #setOnTaskStateChangeListener(OnSyncTaskStateChangeListener)}
 * where this listener will be notified whenever a state of a particular sync task is changed within
 * the adapter.
 *
 * <h3>Sync events dispatching</h3>
 * By default BaseSyncAdapter does not dispatch/notify any state about the current running synchronization
 * process however there can be specified an instance of {@link EventDispatcher} that can be used for
 * such dispatching. If this dispatcher is specified via {@link #setEventDispatcher(EventDispatcher)}
 * BaseSyncAdapter will dispatch instances of {@link SyncEvent} as described below:
 * <ul>
 * <li>
 * {@link #onPerformGlobalSync(SyncOperation)}
 * <p>
 * - dispatches {@link SyncEvent} type of {@link SyncEvent#START} with default task id
 * {@link SyncTask#DEFAULT_ID} along with <var>account</var>
 * </li>
 * <li>
 * {@link #onGlobalSyncFinished(SyncOperation)}
 * <p>
 * - dispatches {@link SyncEvent} type of {@link SyncEvent#FINISH} with default task id
 * {@link SyncTask#DEFAULT_ID} along with <var>account</var>
 * </li>
 * <li>
 * {@link #onGlobalSyncFailed(SyncOperation, Exception)}
 * <p>
 * - dispatches {@link SyncEvent} type of {@link SyncEvent#ERROR} with default task id
 * {@link SyncTask#DEFAULT_ID} along with occurred <var>error</var> and <var>account</var>
 * </li>
 * <li>
 * {@link #onPerformSync(SyncOperation)}
 * <p>
 * - dispatches {@link SyncEvent} type of {@link SyncEvent#START} with id of the specified task
 * along with <var>account</var>
 * </li>
 * <li>
 * {@link #onSyncFinished(SyncOperation)}
 * <p>
 * - dispatches {@link SyncEvent} type of {@link SyncEvent#FINISH} with id of the specified task
 * along with <var>account</var>
 * </li>
 * <li>
 * {@link #onSyncFailed(SyncOperation, Exception)}
 * <p>
 * - dispatches {@link SyncEvent} type of {@link SyncEvent#ERROR} with id of the specified task
 * along with occurred <var>error</var> and <var>account</var>
 * </li>
 * </ul>
 * <b>Dispatching of synchronization events type of {@link SyncEvent#PROGRESS} is responsibility of
 * each specific {@link SyncHandler} implementation</b>.
 *
 * @author Martin Albedinsky
 * @see BaseSyncManager
 * @see SyncHandler
 * @see SyncEvent
 */
public abstract class BaseSyncAdapter extends AbstractThreadedSyncAdapter {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "BaseSyncAdapter";

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Interface used by BaseSyncAdapter to dispatch occurred synchronization events.
	 * <p>
	 * See {@link BaseSyncAdapter BaseSyncAdapter description} for events that are dispatched
	 * by this adapter.
	 *
	 * @author Martin Albedinsky
	 * @see #setEventDispatcher(EventDispatcher)
	 */
	public interface EventDispatcher {

		/**
		 * Called by a specific implementation of BaseSyncAdapter to dispatch the given synchronization
		 * <var>event</var>.
		 *
		 * @param event The synchronization event to be dispatched.
		 */
		void dispatch(@NonNull Object event);
	}

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Listener that are notified about state change of a particular {@link SyncTask}.
	 */
	private OnSyncTaskStateChangeListener mTaskStateChangeListener;

	/**
	 * Dispatcher that is used to dispatch synchronization events.
	 */
	private EventDispatcher mEventDispatcher;

	/**
	 * Handler that is responsible for global synchronization handling.
	 *
	 * @see #onPerformGlobalSync(SyncOperation)
	 */
	private SyncHandler mGlobalSyncHandler;

	/**
	 * Array map containing registered handlers that are responsible for synchronization handling
	 * associated with a specific {@link SyncTask}. These handlers are mapped under the id of theirs
	 * associated SyncTask ({@link SyncHandler#getTaskId()}).
	 *
	 * @see #onPerformSync(SyncOperation)
	 */
	private SparseArray<SyncHandler> mTaskHandlers;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #BaseSyncAdapter(Context, boolean, boolean)} with <var>allowParallelSyncs</var>
	 * parameter set to {@code false}.
	 */
	public BaseSyncAdapter(@NonNull Context context, boolean autoInitialize) {
		this(context, autoInitialize, false);
	}

	/**
	 * Creates a new instance of BaseSyncAdapter with the specified <var>context</var> and configuration
	 * flags.
	 * <p>
	 * See {@link AbstractThreadedSyncAdapter#AbstractThreadedSyncAdapter(Context, boolean, boolean)}
	 * for more information.
	 */
	public BaseSyncAdapter(@NonNull Context context, boolean autoInitialize, boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
	}

	/*
	 * Methods =====================================================================================
	 */


	/**
	 * Registers a callback to be invoked whenever a state is changed for a particular {@link SyncTask}
	 * within this sync adapter.
	 *
	 * @param listener The desired listener callback. May be {@code null} to clear the current one.
	 * @see SyncTask#getState()
	 */
	protected final void setOnTaskStateChangeListener(@Nullable OnSyncTaskStateChangeListener listener) {
		this.mTaskStateChangeListener = listener;
	}

	/**
	 * Sets an dispatcher that is used by this sync adapter to dispatch synchronization events.
	 * <p>
	 * See class {@link BaseSyncAdapter description} for events that are dispatched by this adapter.
	 *
	 * @param dispatcher The desired event dispatcher. May be {@code null} to not dispatch any events.
	 * @see #getEventDispatcher()
	 * @see #dispatchSyncEvent(Object)
	 */
	protected final void setEventDispatcher(@Nullable EventDispatcher dispatcher) {
		this.mEventDispatcher = dispatcher;
	}

	/**
	 * Returns the event dispatcher specified for this sync adapter.
	 *
	 * @return This adapter's event dispatcher.
	 * @see #setEventDispatcher(EventDispatcher)
	 */
	@Nullable
	protected final EventDispatcher getEventDispatcher() {
		return mEventDispatcher;
	}

	/**
	 * Registers a sync handler that will be used by this sync adapter for synchronization handling
	 * of a {@link SyncTask} associated with the given <var>handler</var> via {@link SyncHandler#getTaskId()}.
	 * <p>
	 * The given handler will be used to perform synchronization whenever
	 * {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)} is invoked
	 * with extras Bundle that contains data for the associated SyncTask (mainly its id). Instance
	 * of such SyncTask is created from the delivered extras and passed to the registered handler
	 * via {@link SyncHandler#handleSync(Context, SyncOperation)}.
	 * <p>
	 * <b>Note</b>, that if there is already registered sync handler with the same task id, such
	 * handler will be replaced by the new one.
	 *
	 * @param handler The desired sync handler to be registered.
	 * @see #onPerformSync(SyncOperation)
	 * @see #unregisterTaskHandler(SyncHandler)
	 */
	protected void registerTaskHandler(@NonNull SyncHandler handler) {
		if (mTaskHandlers == null) {
			this.mTaskHandlers = new SparseArray<>();
		}
		final int taskId = handler.getTaskId();
		if (mTaskHandlers.indexOfKey(taskId) >= 0) {
			if (OfficiumConfig.DEBUG_LOG_ENABLED) {
				Log.v(TAG, "Replacing old synchronization handler by a new one for task with id(" + taskId + ").");
			}
			mTaskHandlers.append(taskId, handler);
		} else {
			if (OfficiumConfig.DEBUG_LOG_ENABLED) {
				Log.v(TAG, "Registering new synchronization handler for task with id(" + taskId + ").");
			}
			mTaskHandlers.append(taskId, handler);
		}
	}

	/**
	 * Un-registers previously registered sync handler.
	 * <p>
	 * If the given handler has not been registered before this method does nothing.
	 *
	 * @param handler The desired sync handler to un-register.
	 * @see #onPerformSync(SyncOperation)
	 */
	protected void unregisterTaskHandler(@NonNull SyncHandler handler) {
		if (mTaskHandlers != null && mTaskHandlers.size() > 0)
			mTaskHandlers.remove(handler.getTaskId());
	}

	/**
	 * Sets a sync handler that is responsible for global synchronization handling.
	 *
	 * @param handler The desired sync handler. May be {@code null} if handling of global synchronization
	 *                is not desired.
	 * @see #onPerformGlobalSync(SyncOperation)
	 * @see #getGlobalSyncHandler()
	 */
	protected void setGlobalSyncHandler(@Nullable SyncHandler handler) {
		this.mGlobalSyncHandler = handler;
	}

	/**
	 * Returns the sync handler that is responsible for global synchronization handling.
	 *
	 * @return This adapter's global sync handler.
	 * @see #setGlobalSyncHandler(SyncHandler)
	 */
	@Nullable
	protected SyncHandler getGlobalSyncHandler() {
		return mGlobalSyncHandler;
	}

	/**
	 * @see #createTaskFromExtras(Bundle)
	 * @see #onPerformGlobalSync(SyncOperation)
	 * @see #onPerformSync(SyncOperation)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		final SyncOperation syncOperation = new SyncOperation.Builder()
				.account(account)
				.authority(authority)
				.task(createTaskFromExtras(extras))
				.build();
		changeTaskStateToAndNotify(syncOperation, SyncTask.RUNNING);
		if (syncOperation.task.getId() == SyncTask.DEFAULT_ID) {
			onPerformGlobalSync(syncOperation);
		} else {
			onPerformSync(syncOperation);
		}
	}

	/**
	 * Called to create instance of SyncTask from the given <var>extras</var> whenever
	 * {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)} is invoked.
	 * <p>
	 * Inheritance hierarchies may override this method to instantiate custom implementations of
	 * {@link SyncTask SyncTasks}.
	 *
	 * @param extras The synchronization extras passed to {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}.
	 * @return New instance of SyncTask with data parsed from the given extras.
	 */
	@NonNull
	protected SyncTask createTaskFromExtras(@NonNull Bundle extras) {
		return new SyncTask(extras);
	}

	/**
	 * Invoked whenever {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}
	 * is invoked and the <var>extras</var> Bundle does not contain data for a specific {@link SyncTask}
	 * but rather for a global SyncTask. This is determined whether the extras contains id of that
	 * specific synchronization task or not.
	 *
	 * @param syncOperation Operation describing the global synchronization request. The operation's
	 *                      associated task will always have {@link SyncTask#DEFAULT_ID} id.
	 * @see #onGlobalSyncFinished(SyncOperation)
	 * @see #onGlobalSyncFailed(SyncOperation, Exception)
	 */
	@SuppressWarnings("unchecked")
	protected void onPerformGlobalSync(@NonNull SyncOperation syncOperation) {
		if (mGlobalSyncHandler == null) return;
		dispatchSyncEvent(
				new SyncEvent.Builder(syncOperation.task.getId())
						.type(SyncEvent.START)
						.account(syncOperation.account)
						.build()
		);
		try {
			mGlobalSyncHandler.handleSync(getContext(), syncOperation);
			changeTaskStateToAndNotify(syncOperation, SyncTask.FINISHED);
			onGlobalSyncFinished(syncOperation);
		} catch (Exception error) {
			changeTaskStateToAndNotify(syncOperation, SyncTask.FAILED);
			onGlobalSyncFailed(syncOperation, error);
		}
	}

	/**
	 * Invoked whenever {@link #onPerformGlobalSync(SyncOperation)} finishes without any error.
	 *
	 * @param syncOperation Operation describing the global synchronization request.
	 * @see #onGlobalSyncFailed(SyncOperation, Exception)
	 */
	protected void onGlobalSyncFinished(@NonNull SyncOperation syncOperation) {
		dispatchSyncEvent(
				new SyncEvent.Builder(syncOperation.task.getId())
						.type(SyncEvent.FINISH)
						.account(syncOperation.account)
						.build()
		);
	}

	/**
	 * Invoked whenever {@link #onPerformGlobalSync(SyncOperation)} is invoked and there is thrown
	 * the given <var>error</var> exception during its execution.
	 *
	 * @param syncOperation Operation describing the global synchronization request.
	 * @param error         The error exception thrown by {@link #onPerformGlobalSync(SyncOperation)}.
	 * @see #onSyncFinished(SyncOperation)
	 */
	protected void onGlobalSyncFailed(@NonNull SyncOperation syncOperation, @NonNull Exception error) {
		dispatchSyncEvent(
				new SyncEvent.Builder(syncOperation.task.getId())
						.type(SyncEvent.ERROR)
						.account(syncOperation.account)
						.error(error)
						.build()
		);
	}

	/**
	 * Invoked whenever {@link #onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}
	 * is invoked and the <var>extras</var> Bundle does contain data for a specific {@link SyncTask}.
	 * This is determined whether the extras contains id of that specific synchronization task or not.
	 *
	 * @param syncOperation Operation describing the synchronization request.
	 * @see #onSyncFinished(SyncOperation)
	 * @see #onSyncFailed(SyncOperation, Exception)
	 */
	@SuppressWarnings("unchecked")
	protected void onPerformSync(@NonNull SyncOperation syncOperation) {
		final SyncHandler taskHandler = mTaskHandlers == null ? null : mTaskHandlers.get(syncOperation.task.getId());
		if (taskHandler == null) {
			Log.e(TAG, "No synchronization handler found for task with id(" + syncOperation.task.getId() + ").");
			return;
		}
		dispatchSyncEvent(
				new SyncEvent.Builder(syncOperation.task.getId())
						.type(SyncEvent.START)
						.account(syncOperation.account)
						.build()
		);
		try {
			taskHandler.handleSync(getContext(), syncOperation);
			changeTaskStateToAndNotify(syncOperation, SyncTask.FINISHED);
			onSyncFinished(syncOperation);
		} catch (Exception error) {
			changeTaskStateToAndNotify(syncOperation, SyncTask.FAILED);
			onSyncFailed(syncOperation, error);
		}
	}

	/**
	 * Invoked whenever {@link #onPerformSync(SyncOperation)} finishes without any error.
	 *
	 * @param syncOperation Operation describing the synchronization request.
	 * @see #onSyncFailed(SyncOperation, Exception)
	 */
	protected void onSyncFinished(@NonNull SyncOperation syncOperation) {
		dispatchSyncEvent(
				new SyncEvent.Builder(syncOperation.task.getId())
						.type(SyncEvent.FINISH)
						.account(syncOperation.account)
						.build()
		);
	}

	/**
	 * Invoked whenever {@link #onPerformSync(SyncOperation)} is invoked and there is thrown the
	 * given <var>error</var> exception during its execution.
	 *
	 * @param syncOperation Operation describing the synchronization request.
	 * @param error         The error exception thrown by {@link #onPerformSync(SyncOperation)}.
	 * @see #onSyncFinished(SyncOperation)
	 */
	protected void onSyncFailed(@NonNull SyncOperation syncOperation, @NonNull Exception error) {
		dispatchSyncEvent(
				new SyncEvent.Builder(syncOperation.task.getId())
						.type(SyncEvent.ERROR)
						.account(syncOperation.account)
						.error(error)
						.build()
		);
	}

	/**
	 * Changes current state of {@link SyncTask} associated with the given <var>syncOperation</var>
	 * to the specified one via {@link SyncTask#setState(int)}. If the task's state is changed, also
	 * the current registered {@link OnSyncTaskStateChangeListener} (if any) is notified about the
	 * change.
	 *
	 * @param syncOperation The sync operation containing the sync task of which state to change.
	 * @param state         The new state for the task. Should be one of states defined by {@link SyncTask.State @State}
	 *                      annotation.
	 */
	private void changeTaskStateToAndNotify(SyncOperation syncOperation, @SyncTask.State int state) {
		if (syncOperation.task.getState() != state) {
			syncOperation.task.setState(state);
			if (mTaskStateChangeListener != null) {
				mTaskStateChangeListener.onSyncTaskStateChanged(syncOperation.task, syncOperation.account);
			}
		}
	}

	/**
	 * Invoked whenever this sync adapter wants to dispatch the given synchronization <var>event</var>
	 * to its clients.
	 * <p>
	 * Default implementation uses {@link EventDispatcher} specified via {@link #setEventDispatcher(EventDispatcher)}
	 * (if any). If there is no dispatcher specified this method does nothing.
	 * <p>
	 * See class {@link BaseSyncAdapter description} for events that are dispatched by this adapter.
	 *
	 * @param event The synchronization event to be dispatched.
	 */
	protected void dispatchSyncEvent(@NonNull Object event) {
		if (mEventDispatcher != null) mEventDispatcher.dispatch(event);
	}

	/*
	 * Inner classes ===============================================================================
	 */

}
