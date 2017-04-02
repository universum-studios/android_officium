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
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Event that can be fired/post for receivers that listen for state of a concrete synchronization
 * process/task. Each instance of SyncEvent can be identified by its {@link SyncEvent#id id} and
 * {@link SyncEvent#type type}. Additionally for events type of {@link #PROGRESS} can be specified
 * {@link SyncEvent#progress progress} value and for events type of {@link #ERROR} can be specified
 * occurred synchronization {@link SyncEvent#error}.
 * <p>
 * Creation of instances of {@link SyncEvent SyncEvents} is restricted via {@link Builder}
 * only.
 *
 * @author Martin Albedinsky
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
	 * Type flag for {@link SyncEvent} determining <b>error</b> occurred during synchronization.
	 */
	public static final int ERROR = 0x04;

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
	public final int id;

	/**
	 * Type of this synchronization event. One of {@link #START}, {@link #PROGRESS}, {@link #FINISH}
	 * or {@link #ERROR} or some custom specified type.
	 */
	public final int type;

	/**
	 * Current progress of synchronization task with {@link #id}.
	 */
	public final int progress;

	/**
	 * Error that has occurred during execution of synchronization task with {@link #id}.
	 */
	public final Exception error;

	/**
	 * Account used during execution of synchronization task with {@link #id}.
	 */
	public final Account account;

	/**
	 * Bundle with additional extra information.
	 */
	public final Bundle extras;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SyncEvent from the specified <var>builder</var>.
	 *
	 * @param builder The builder with data for the new event instance.
	 */
	private SyncEvent(@NonNull Builder builder) {
		this.id = builder.id;
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
		 * See {@link SyncEvent#id}.
		 */
		private final int id;

		/**
		 * See {@link SyncEvent#error}.
		 */
		private int type;

		/**
		 * See {@link SyncEvent#progress}.
		 */
		private int progress;

		/**
		 * See {@link SyncEvent#error}.
		 */
		private Exception error;

		/**
		 * See {@link SyncEvent#account}.
		 */
		private Account account;

		/**
		 * See {@link SyncEvent#extras}.
		 */
		private Bundle extras;

		/**
		 * Creates a new instance of Builder with the specified <var>id</var>.
		 *
		 * @param id The desired id for the new SyncEvent.
		 * @see SyncEvent#id
		 */
		public Builder(int id) {
			this.id = id;
		}

		/**
		 * Specifies a type of SyncEvent that will be fired.
		 *
		 * @param type The desired type. May be one of {@link #START}, {@link #PROGRESS}, {@link #FINISH}
		 * or {@link #ERROR} or custom specified type.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#type
		 */
		public Builder type(int type) {
			this.type = type;
			return this;
		}

		/**
		 * Specifies a progress of running synchronization.
		 *
		 * @param progress The desired progress.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#progress
		 */
		public Builder progress(int progress) {
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
		public Builder error(@NonNull Exception error) {
			this.error = error;
			return this;
		}

		/**
		 * Specifies an account for which SyncEvent will be fired.
		 *
		 * @param account The desired account.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#account
		 */
		public Builder account(@NonNull Account account) {
			this.account = account;
			return this;
		}

		/**
		 * Specifies a bundle with extra information for the event.
		 *
		 * @param extras The desired bundle with extras.
		 * @return This builder to allow methods chaining.
		 * @see SyncEvent#extras
		 */
		public Builder extras(@NonNull Bundle extras) {
			this.extras = extras;
			return this;
		}

		/**
		 * Builds a new instance of SyncEvent.
		 *
		 * @return New instance of SyncEvent with data specified for this builder.
		 */
		@NonNull
		public SyncEvent build() {
			return new SyncEvent(this);
		}
	}
}
