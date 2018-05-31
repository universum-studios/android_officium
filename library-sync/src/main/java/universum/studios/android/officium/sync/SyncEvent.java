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
package universum.studios.android.officium.sync;

import android.accounts.Account;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Event that can be fired/post for receivers that listen for state of a concrete synchronization
 * process/task. Each instance of SyncEvent can be identified by its {@link SyncEvent#taskId taskId}
 * and {@link SyncEvent#type type}. Additionally for events type of {@link #PROGRESS} can be specified
 * {@link SyncEvent#progress progress} value and for events type of {@link #FAILURE} can be specified
 * occurred synchronization {@link SyncEvent#error}.
 * <p>
 * Creation of instances of {@link SyncEvent SyncEvents} is restricted via {@link Builder}
 * only.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class SyncEvent {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SyncEvent";

	/**
	 * Type flag for {@link SyncEvent} determining <b>start</b> of synchronization.
	 */
	public static final int START = 0x01;

	/**
	 * Type flag for {@link SyncEvent} determining <b>progress</b> of synchronization.
	 */
	public static final int PROGRESS = 0x02;

	/**
	 * Type flag for {@link SyncEvent} determining <b>finish</b> of synchronization.
	 */
	public static final int FINISH = 0x03;

	/**
	 * Type flag for {@link SyncEvent} determining <b>failure</b> occurred during synchronization.
	 */
	public static final int FAILURE = 0x04;

	/**
	 * Defines an annotation for determining set of available types for {@link SyncEvent}.
	 *
	 * <h3>Available types</h3>
	 * <ul>
	 * <li>{@link #START}</li>
	 * <li>{@link #PROGRESS}</li>
	 * <li>{@link #FINISH}</li>
	 * <li>{@link #FAILURE}</li>
	 * </ul>
	 */
	@IntDef({START, PROGRESS, FINISH, FAILURE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Type {}

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
	 * Id of the synchronization task for which has been this synchronization event fired.
	 */
	public final int taskId;

	/**
	 * Type of this synchronization event. One of {@link #START}, {@link #PROGRESS}, {@link #FINISH}
	 * or {@link #FAILURE} or some custom specified type.
	 */
	@Type public final int type;

	/**
	 * Current progress of synchronization task with id of{@link #taskId}.
	 */
	public final int progress;

	/**
	 * Error that has occurred during execution of synchronization task with id of {@link #taskId}.
	 */
	@Nullable public final Exception error;

	/**
	 * Account used during execution of synchronization task with id of {@link #taskId}.
	 */
	@Nullable public final Account account;

	/**
	 * Bundle with additional extra information.
	 */
	@Nullable public final Bundle extras;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SyncEvent from the specified <var>builder</var>.
	 *
	 * @param builder The builder with data for the new event instance.
	 */
	@SuppressWarnings("WeakerAccess")
	SyncEvent(@NonNull final Builder builder) {
		this.taskId = builder.taskId;
		this.type = builder.type;
		this.progress = builder.progress;
		this.error = builder.error;
		this.account = builder.account;
		this.extras = builder.extras;
	}

	/*
	 * Methods =====================================================================================
	 */

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Builder that can be used to create a new instance of {@link SyncEvent}.
	 *
	 * @author Martin Albedinsky
	 */
	public static final class Builder {

		/**
		 * See {@link SyncEvent#taskId}.
		 */
		final int taskId;

		/**
		 * See {@link SyncEvent#type}.
		 */
		int type = START;

		/**
		 * See {@link SyncEvent#progress}.
		 */
		int progress;

		/**
		 * See {@link SyncEvent#error}.
		 */
		Exception error;

		/**
		 * See {@link SyncEvent#account}.
		 */
		Account account;

		/**
		 * See {@link SyncEvent#extras}.
		 */
		Bundle extras;

		/**
		 * Creates a new instance of Builder with the specified <var>taskId</var>.
		 *
		 * @param taskId Id of the synchronization task for which is the new SyncEvent created.
		 * @see SyncEvent#taskId
		 */
		public Builder(final int taskId) {
			this.taskId = taskId;
		}

		/**
		 * Specifies a type of SyncEvent that will be fired.
		 * <p>
		 * Default value: <b>{@link #START}</b>
		 *
		 * @param type The desired type. May be one of {@link #START}, {@link #PROGRESS}, {@link #FINISH}
		 *             or {@link #FAILURE} or custom specified type.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#type
		 */
		public Builder type(@Type final int type) {
			this.type = type;
			return this;
		}

		/**
		 * Specifies an account for which SyncEvent will be fired.
		 *
		 * @param account The desired account.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#account
		 */
		public Builder account(@NonNull final Account account) {
			this.account = account;
			return this;
		}

		/**
		 * Specifies a progress of running synchronization.
		 *
		 * @param progress The desired progress.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#progress
		 */
		public Builder progress(final int progress) {
			this.progress = progress;
			return this;
		}

		/**
		 * Specifies an error that occurred during synchronization.
		 *
		 * @param error The desired error.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#error
		 */
		public Builder error(@NonNull final Exception error) {
			this.error = error;
			return this;
		}

		/**
		 * Specifies a bundle with extra information for the event.
		 *
		 * @param extras The desired bundle with extras.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#extras
		 */
		public Builder extras(@NonNull final Bundle extras) {
			this.extras = extras;
			return this;
		}

		/**
		 * Builds a new instance of SyncEvent.
		 *
		 * @return New instance of SyncEvent with data specified for this builder.
		 */
		@NonNull public SyncEvent build() {
			return new SyncEvent(this);
		}
	}
}