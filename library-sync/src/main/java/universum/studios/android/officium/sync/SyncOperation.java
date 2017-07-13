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
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * SyncOperation contains data that are describing a specific synchronization request to be executed
 * in {@link BaseSyncAdapter}.
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("WeakerAccess")
public final class SyncOperation {

	 /*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SyncOperation";

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
	 * Account associated with this sync operation.
	 */
	@NonNull
	public final Account account;

	/**
	 * Authority associated with this sync operation.
	 */
	@NonNull
	public final String authority;

	/**
	 * Task associated with this sync operation.
	 */
	@NonNull
	public final SyncTask task;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SyncOperation with data provided by the specified <var>builder</var>.
	 *
	 * @param builder The builder with data for the new SyncOperation.
	 */
	SyncOperation(final Builder builder) {
		this.account = builder.account;
		this.authority = builder.authority;
		this.task = builder.task;
	}

	/*
	 * Methods =====================================================================================
	 */

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Builder that may be used to build instances of {@link SyncOperation}.
	 *
	 * @author Martin Albedinsky
	 */
	public static final class Builder {

		/**
		 * See {@link SyncOperation#account}.
		 */
		Account account;

		/**
		 * See {@link SyncOperation#authority}.
		 */
		String authority;

		/**
		 * See {@link SyncOperation#task}.
		 */
		SyncTask task;

		/**
		 * Specifies an account for the sync operation.
		 *
		 * @param account The account to associate with the sync operation.
		 * @return This builder to allow methods chaining.
		 * @see SyncOperation#account
		 */
		public Builder account(@NonNull final Account account) {
			this.account = account;
			return this;
		}

		/**
		 * Specifies an authority for the sync operation.
		 *
		 * @param authority The authority to associate with the sync operation.
		 * @return This builder to allow methods chaining.
		 * @see SyncOperation#authority
		 */
		public Builder authority(@NonNull final String authority) {
			this.authority = authority;
			return this;
		}

		/**
		 * Specifies a task for the sync operation.
		 *
		 * @param task The task to associate with the sync operation.
		 * @return This builder to allow methods chaining.
		 * @see SyncOperation#task
		 */
		public Builder task(@NonNull final SyncTask task) {
			this.task = task;
			return this;
		}

		/**
		 * Builds a new instance of SyncOperation from the current data.
		 *
		 * @return New instance of SyncOperation.
		 * @throws IllegalArgumentException If any of the required arguments is missing.
		 */
		@NonNull
		public SyncOperation build() {
			if (account == null) throw new IllegalArgumentException("No account specified.");
			if (TextUtils.isEmpty(authority)) throw new IllegalArgumentException("No authority specified.");
			if (task == null) throw new IllegalArgumentException("No task specified.");
			return new SyncOperation(this);
		}
	}
}
