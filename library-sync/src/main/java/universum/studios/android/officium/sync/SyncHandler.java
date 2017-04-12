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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import universum.studios.android.officium.OfficiumLogging;

/**
 * Base class for handlers that are used by {@link BaseSyncAdapter} to handle synchronization process
 * for a specific {@link SyncTask SyncTasks}. Each implementation of SyncHandler must be associated
 * with its corresponding {@link SyncTask} via {@link #SyncHandler(int)} and {@link SyncTask#getId()}.
 * Additionally if a specific SyncHandler implementation needs/accepts for its synchronization logic
 * some additional data, these can be specified via {@link SyncTask.Builder#request(SyncTask.Request)}.
 * The specified synchronization request will be parsed whenever {@link #handleSync(Context, SyncOperation)}
 * is called via {@link SyncTask#getRequest(Class)} using the request class specified in {@link #SyncHandler(int, Class)}.
 * <p>
 * Each SyncHandler implementation must implement at least {@link #onHandleSync(Context, SyncOperation, SyncTask.Request)}
 * method to which will be passed all data needed to perform for that handler specific synchronization
 * logic. In case of failed synchronization execution of {@link #onHandleSync(Context, SyncOperation, SyncTask.Request)}
 * will be invoked {@link #onSyncError(Context, SyncOperation, SyncTask.Request, Exception)} method all
 * internally by SyncHandler class. Any exception thrown further from {@link #onSyncError(Context, SyncOperation, SyncTask.Request, Exception)}
 * method will be dispatched to the calling synchronization context (by default {@link BaseSyncAdapter}).
 *
 * @param <Request> Type of the request that the SyncHandler implementation needs to perform its
 *                  specific synchronization process.
 * @param <Result>  Type of the result returned by the SyncHandler implementation whenever
 *                  {@link #handleSync(Context, SyncOperation)} finishes without any error.
 * @author Martin Albedinsky
 */
public abstract class SyncHandler<Request extends SyncTask.Request, Result> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SyncHandler";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Id of the synchronization task associated with this handler.
	 *
	 * @see #handleSync(Context, SyncOperation)
	 */
	private final int mTaskId;

	/**
	 * Class of synchronization request that will be delivered by instance of associated {@link SyncTask}
	 * to this handler whenever {@link #handleSync(Context, SyncOperation)} is invoked.
	 */
	private final Class<Request> mRequestClass;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #SyncHandler(int, Class)} without <var>classOfRequest</var> specified.
	 * <p>
	 * This constructor should be used only for synchronization handlers that do not accept any
	 * request to perform synchronization. This means that such handlers can perform synchronization
	 * without any request data sent from a client.
	 *
	 * @param taskId Id of the {@link SyncTask} associated with this handler.
	 */
	protected SyncHandler(final int taskId) {
		this.mTaskId = taskId;
		this.mRequestClass = null;
	}

	/**
	 * Creates a new instance of SyncHandler with the specified <var>taskId</var> and <var>classOfRequest</var>.
	 *
	 * @param taskId         Id of the {@link SyncTask} associated with this handler.
	 * @param classOfRequest The class that is used to parse synchronization request delivered by
	 *                       instance of associated {@link SyncTask} whenever {@link #handleSync(Context, SyncOperation)}
	 *                       is called. May be {@code null} if {@link SyncTask.EmptyRequest} is used.
	 * @see SyncTask#getRequest(Class)
	 */
	protected SyncHandler(final int taskId, @Nullable final Class<Request> classOfRequest) {
		this.mTaskId = taskId;
		this.mRequestClass = classOfRequest;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns id of the associated synchronization task.
	 *
	 * @return Id of the SyncTask for which can this handler perform synchronization.
	 * @see #handleSync(Context, SyncOperation)
	 */
	public final int getTaskId() {
		return mTaskId;
	}

	/**
	 * Called to perform synchronization process that is specific for this handler. Each synchronization
	 * handler may perform synchronization in a different way. It is completely depends on a specific
	 * implementation of this SyncHandler class.
	 * <p>
	 * If this handler accepts synchronization request needed to perform synchronization process,
	 * that request should be delivered to this handler by the given <var>syncOperation</var>.
	 * <p>
	 * <b>Note</b>, that any exceptions thrown during synchronization are handled internally by this
	 * handler, however this handler may throw some additional exceptions in case those exceptions
	 * should be dispatched to clients that requested this synchronization.
	 *
	 * @param context       Context that may be used to access application data and services needed
	 *                      to perform requested synchronization.
	 * @param syncOperation Operation describing the synchronization request.
	 * @return Synchronization result specific for this handler. May be {@code null} if this handler
	 * does not return any result or synchronization has failed and this handler did not throw any
	 * additional exceptions.
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public final Result handleSync(@NonNull final Context context, @NonNull final SyncOperation syncOperation) {
		final Request syncRequest = mRequestClass == null ? null : (Request) syncOperation.task.getRequest(mRequestClass);
		try {
			return onHandleSync(context, syncOperation, syncRequest);
		} catch (Exception error) {
			onSyncError(context, syncOperation, syncRequest, error);
		}
		return null;
	}

	/**
	 * Invoked whenever {@link #handleSync(Context, SyncOperation)} is called to perform synchronization
	 * specific for this handler.
	 * <p>
	 * Any {@link Exception} thrown by this method will be handled by this handler and passed to
	 * {@link #onSyncError(Context, SyncOperation, SyncTask.Request, Exception)}.
	 *
	 * @param context       Context that may be used to access application data and services needed
	 *                      to perform requested synchronization.
	 * @param syncOperation Operation describing the synchronization request.
	 * @param syncRequest   Synchronization request that has been specified for {@link SyncTask} passed
	 *                      to {@link #handleSync(Context, SyncOperation)} method via
	 *                      {@link SyncOperation}. The request is parsed via {@link SyncTask#getRequest(Class)}
	 *                      using the request class specified for this handler. If this handler does
	 *                      not have its request class specified or the delivered SyncTask does not
	 *                      contain any request body to parse request from, the request parameter
	 *                      will be {@code null}.
	 * @return Synchronization result specific for this handler. May be {@code null} if this handler
	 * does not return any result.
	 * @throws Exception The exception occurred during synchronization handling specific for this
	 *                   handler.
	 */
	@Nullable
	protected abstract Result onHandleSync(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable Request syncRequest) throws Exception;

	/**
	 * Invoked whenever {@link #onHandleSync(Context, SyncOperation, SyncTask.Request)} is invoked
	 * and there is thrown the given <var>error</var> exception during its execution.
	 * <p>
	 * <b>Note</b>, that any additional exception thrown from this method should be handled by the
	 * calling synchronization service/context. By default, if used with {@link BaseSyncAdapter},
	 * that sync adapter will handle the thrown exception and will dispatch it to the clients that
	 * requested this synchronization.
	 *
	 * @param context       Context passed to this handler during synchronization request.
	 * @param syncOperation Operation describing the synchronization request.
	 * @param syncRequest   Synchronization request for which has synchronization failed. May be
	 *                      {@code null} if this handler does not accept any request.
	 * @param error         The error exception thrown by {@link #onHandleSync(Context, SyncOperation, SyncTask.Request)}.
	 */
	protected void onSyncError(@NonNull final Context context, @NonNull final SyncOperation syncOperation, @Nullable final Request syncRequest, @NonNull final Exception error) {
		OfficiumLogging.e(getClass().getSimpleName(), "An error occurred during synchronization handling!", error);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
