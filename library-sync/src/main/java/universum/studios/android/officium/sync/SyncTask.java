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
import android.content.ContentProviderClient;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Task that can be used to request synchronization via implementation of {@link BaseSyncManager}.
 * Each instance of SyncTask may be identified by its id that can be obtained via {@link #getId()}.
 * <p>
 * Creation of instances of {@link SyncTask SyncTasks} is restricted via {@link Builder} only.
 * <p>
 * If synchronization related to a particular SyncTask depends on some additional data that are needed
 * to perform synchronization logic, these data can be specified via {@link Builder#request(Request)}.
 * The synchronization request object should be a simple POJO object that can be processed by {@link Gson}
 * into its {@link String} representation and also from it. The request object will be processed into
 * Json data whenever instance of SyncTask is requested to put its data into extras {@link Bundle}
 * via {@link #intoExtras(Bundle)} and instantiated from that Json data whenever {@link #getRequest(Class)}
 * is called and the request instance has not been parsed yet.
 * <p>
 * Inheritance instance should override both {@link #SyncTask(Builder)} and {@link #SyncTask(Bundle)}
 * constructors along with {@link #intoExtras(Bundle)} method to perform custom instantiation logic
 * via custom implementation of {@link Builder} or from/into extras {@link Bundle}.
 *
 * @param <R> Type of the request specific for the SyncTask implementation.
 * @author Martin Albedinsky
 */
public class SyncTask<R extends SyncTask.Request> implements Cloneable {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SyncTask";

	/**
	 * Initial state for each new {@link SyncTask}.
	 */
	public static final int IDLE = 0x00;

	/**
	 * State indicating that {@link SyncTask} is in pending state waiting to be executed.
	 * <p>
	 * Task becomes pending when {@link BaseSyncManager#requestSync(SyncTask)} has been called with
	 * that particular task.
	 */
	public static final int PENDING = 0x01;

	/**
	 * State indicating that {@link SyncTask} is currently being executed.
	 * <p>
	 * Task becomes running when {@link BaseSyncAdapter#onPerformSync(Account, Bundle, String, ContentProviderClient, SyncResult)}
	 * has been called with extras containing that particular task.
	 */
	public static final int RUNNING = 0x02;

	/**
	 * State indicating that {@link SyncTask} has been already executed without any error.
	 * <p>
	 * Task becomes finished when {@link BaseSyncAdapter#onSyncFinished(SyncOperation)} has been
	 * called with sync operation containing that particular task.
	 */
	public static final int FINISHED = 0x03;

	/**
	 * State indicating that {@link SyncTask} has been already executed but an error occurred during
	 * its execution.
	 * <p>
	 * Task becomes finished when {@link BaseSyncAdapter#onSyncFailed(SyncOperation, Exception)} has
	 * been called with sync operation containing that particular task.
	 */
	public static final int FAILED = 0x04;

	/**
	 * State indicating that {@link SyncTask} has been canceled.
	 * <p>
	 * Task becomes canceled when {@link BaseSyncManager#cancelSync()} has been called.
	 */
	public static final int CANCELED = 0x05;

	/**
	 * Defines an annotation for determining set of available states for {@link SyncTask}.
	 * <h3>Available states</h3>
	 * <ul>
	 * <li>{@link #IDLE}</li>
	 * <li>{@link #PENDING}</li>
	 * <li>{@link #RUNNING}</li>
	 * <li>{@link #FINISHED}</li>
	 * <li>{@link #FAILED}</li>
	 * <li>{@link #CANCELED}</li>
	 * </ul>
	 *
	 * @see #getState()
	 */
	@IntDef({
			IDLE,
			PENDING,
			RUNNING,
			FINISHED,
			FAILED,
			CANCELED
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface State {
	}

	/**
	 * Default id for synchronization task. This id may be used to identify <b>global synchronization task</b>.
	 */
	public static final int DEFAULT_ID = 0;

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Required interface for all synchronization requests.
	 *
	 * @author Martin Albedinsky
	 */
	public interface Request {
	}

	/**
	 * A {@link Request} implementation that may be used for {@link SyncHandler SyncHandlers} that
	 * does not require any synchronization request.
	 *
	 * @author Martin Albedinsky
	 */
	public static final class EmptyRequest implements Request {

		/**
		 */
		private EmptyRequest() {
			// Not allowed to be instantiated publicly.
			throw new UnsupportedOperationException();
		}
	}

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Empty instance of {@link SyncTask} that may be used in order to pass a sync task to a sync
	 * handler that does not require any data from a caller to perform synchronization logic.
	 */
	@SuppressWarnings("unused")
	public static final SyncTask<EmptyRequest> EMPTY = new SyncTask<>();

	/**
	 * Instance of Gson used to parse {@link #mRequestBody} into {@link #mRequest} instance and
	 * vice versa.
	 */
	private static final Gson GSON = new Gson();

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Id specified for this task.
	 */
	private final int mId;

	/**
	 * Request specified for this task. May be {@code null}.
	 */
	private R mRequest;

	/**
	 * Json data of request obtained from {@link Bundle} when this task is created from synchronization
	 * extras via {@link #SyncTask(Bundle)}. This string is used when parsing instance of Request
	 * specific for this
	 *
	 * @see #getRequest(Class)
	 */
	private final String mRequestBody;

	/**
	 * Current state of this synchronization task. May be one of states defined by {@link State @State}
	 * annotation.
	 */
	private int mState = IDLE;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates an empty instance of SyncTask.
	 */
	private SyncTask() {
		this.mId = -1;
		this.mRequestBody = null;
	}

	/**
	 * Creates a new instance of SyncTask with data of the given one.
	 *
	 * @param other The other sync task of which data to copy to the new one.
	 */
	private SyncTask(final SyncTask other) {
		this.mId = other.mId;
		this.mRequestBody = other.mRequestBody;
		this.mState = other.mState;
	}

	/**
	 * Creates a new instance of SyncTask with data specified within the given <var>builder</var>.
	 *
	 * @param builder The builder containing data for the new SyncTask instance.
	 */
	protected SyncTask(@NonNull final Builder<R> builder) {
		this.mId = builder.id;
		this.mRequest = builder.request;
		this.mRequestBody = mRequest == null ? null : GSON.toJson(mRequest);
	}

	/**
	 * Creates a new instance of SyncTask with data specified within the given <var>extras</var>
	 * Bundle.
	 * <p>
	 * <b>Note</b>, that proper instantiation of SyncTask from the given <var>extras</var> Bundle
	 * may be performed only if data for the new SyncTask instance has been previously stored within
	 * the given bundle via {@link #intoExtras(Bundle)} method.
	 *
	 * @param extras The synchronization extras Bundle containing data for the new SyncTask instance.
	 * @see #intoExtras(Bundle)
	 */
	protected SyncTask(@NonNull final Bundle extras) {
		this.mId = extras.getInt(SyncExtras.EXTRA_TASK_ID, DEFAULT_ID);
		this.mRequestBody = extras.getString(SyncExtras.EXTRA_TASK_REQUEST_BODY);
		this.mState = extras.getInt(SyncExtras.EXTRA_TASK_STATE, mState);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the id of this task.
	 *
	 * @return Id specified for this task.
	 */
	public final int getId() {
		return mId;
	}

	/**
	 * Changes the current state of this task to the specified one.
	 *
	 * @param state The new state for this task. Should be one of states defined by {@link State @State}
	 *              annotation.
	 * @see #getState()
	 */
	final void setState(@State final int state) {
		this.mState = state;
	}

	/**
	 * Returns the current state of this task.
	 *
	 * @return This task's state. May be one of states defined by {@link State @State} annotation.
	 */
	@State
	public final int getState() {
		return mState;
	}

	/**
	 * Returns the request specified for this task. If request is {@code null} but there is request
	 * body available after this sync task has been created from the extras {@link Bundle} the desired
	 * request will be parsed from its body using {@link Gson}.
	 *
	 * @param classOfRequest Class used to parse the desired request if it is not parsed yet and there
	 *                       is request body available.
	 * @return This task's request or {@code null} if there is no request specified.
	 */
	@Nullable
	public final R getRequest(@NonNull final Class<R> classOfRequest) {
		if (mRequest == null) {
			this.mRequest = TextUtils.isEmpty(mRequestBody) ? null : GSON.fromJson(mRequestBody, classOfRequest);
		}
		return mRequest;
	}

	/**
	 * Returns a body of the request specified for this task.
	 *
	 * @return This task's request body in Json format or {@code null} if there is not request specified.
	 */
	@Nullable
	public final String getRequestBody() {
		return mRequestBody;
	}

	/**
	 * Puts all data of this task into the given <var>extras</var> Bundle. Instance of this SyncTask
	 * may be than instantiated from the specified extras via {@link #SyncTask(Bundle)} constructor.
	 *
	 * @param extras The synchronization extras Bundle into which to put data of this task.
	 * @return The given extras Bundle with this task's data.
	 */
	@NonNull
	public Bundle intoExtras(@NonNull final Bundle extras) {
		extras.putInt(SyncExtras.EXTRA_TASK_ID, mId);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, mRequestBody);
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, mState);
		return extras;
	}

	/**
	 */
	@Override
	public int hashCode() {
		int hash = mId;
		if (!TextUtils.isEmpty(mRequestBody)) {
			hash = 31 * hash + mRequestBody.hashCode();
		}
		return hash;
	}

	/**
	 */
	@Override
	public boolean equals(@Nullable final Object other) {
		if (other == this) return true;
		if (!(other instanceof SyncTask)) return false;
		final SyncTask task = (SyncTask) other;
		if (task.mId != mId) {
			return false;
		}
		final int requestHash = mRequestBody == null ? 0 : mRequestBody.hashCode();
		final int otherRequestHash = task.mRequestBody == null ? 0 : task.mRequestBody.hashCode();
		return requestHash == otherRequestHash;
	}

	/**
	 */
	@Override
	@SuppressWarnings("StringBufferReplaceableByString")
	public String toString() {
		final StringBuilder builder = new StringBuilder(64);
		builder.append(getClass().getSimpleName());
		builder.append("{id: ");
		builder.append(mId);
		builder.append(", request: ");
		builder.append(mRequest == null ? mRequestBody : mRequest);
		builder.append(", state: ");
		builder.append(stateName(mState));
		return builder.append("}").toString();
	}

	/**
	 * Returns name of the specified <var>state</var>.
	 *
	 * @param state The desired state of which name to return.
	 * @return The state's name.
	 */
	private static String stateName(final int state) {
		switch (state) {
			case IDLE:
				return "IDLE";
			case PENDING:
				return "PENDING";
			case RUNNING:
				return "RUNNING";
			case FINISHED:
				return "FINISHED";
			case FAILED:
				return "FAILED";
			default:
				return "UNKNOWN";
		}
	}

	/**
	 * Makes a clone of this task with the same id and request body but with the initial state, which
	 * is {@link #IDLE}.
	 */
	@Override
	@SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "CloneDoesntCallSuperClone"})
	protected SyncTask clone() {
		final SyncTask clone = new SyncTask(this);
		clone.mState = IDLE;
		return clone;
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Builder that can be used to create a new instance of {@link SyncTask}.
	 *
	 * @author Martin Albedinsky
	 */
	public static class Builder<R extends Request> {

		/**
		 * See {@link SyncTask#mId}.
		 */
		private final int id;

		/**
		 * See {@link SyncTask#mRequest}.
		 */
		private R request;

		/**
		 * Creates a new instance of Builder for the specified synchronization <var>taskId</var>.
		 *
		 * @param taskId Id of the synchronization task to build.
		 */
		public Builder(final int taskId) {
			this.id = taskId;
		}

		/**
		 * Specifies a request for the synchronization task to build.
		 *
		 * @param request The desired request. This should be a simple POJO object that can be processed
		 *                by {@link Gson}. May be {@code null} if request is not necessary for the
		 *                sync task.
		 * @return This builder to allow methods chaining.
		 */
		public Builder request(@Nullable final R request) {
			this.request = request;
			return this;
		}

		/**
		 * Builds a new instance of SyncTask.
		 *
		 * @return New instance of SyncTask with data specified for this builder.
		 */
		@NonNull
		public SyncTask<R> build() {
			return new SyncTask<>(this);
		}
	}
}
