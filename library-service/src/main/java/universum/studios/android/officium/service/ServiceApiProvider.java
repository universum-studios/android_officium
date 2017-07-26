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
package universum.studios.android.officium.service;

import android.support.annotation.NonNull;

/**
 * ServiceApiProvider represents a provider that can provide an instance of {@link ServiceApi}
 * implementation. Each instance of ServiceApiProvider implementation can provide only single instance
 * of a specific ServiceApi. Already created api instance is cached within api provider due to
 * performance reasons. An instance of api can be obtained via {@link #getApi()}.
 * <p>
 * Implementations of ServiceApiProvider are required to implement {@link #onCreateApi()} to
 * create a new instance of api that is specific for them. If there is required some additional set
 * up of api instance before it is returned to its users via {@link #getApi()} than such set up
 * may be accomplished via {@link #onPrepareApi(Object)} method.
 * <p>
 * Whenever an existing instance of api becomes invalid that api instance may be invalidated via call
 * to {@link #invalidateApi()} which will result in call to {@link #onCreateApi()} when
 * {@link #getApi()} is next time invoked.
 *
 * @param <A> Type of the API provided by the ServiceApiProvider implementation.
 * @author Martin Albedinsky
 */
public abstract class ServiceApiProvider<A> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceApiProvider";

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
	 * Lock used for synchronized operations.
	 */
	private final Object LOCK = new Object();

	/**
	 * Cached api implementation created via {@link #onCreateApi()}.
	 */
	private volatile A mApi;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the service api implementation specific for this api provider.
	 * <p>
	 * Subsequent calls to this method will return already created and cached instance of api.
	 *
	 * @return Instance of api ready to be used for service requests execution.
	 */
	@NonNull
	public final A getApi() {
		if (mApi == null) {
			synchronized (LOCK) {
				this.mApi = onCreateApi();
			}
		}
		return onPrepareApi(mApi);
	}

	/**
	 * Invoked whenever {@link #getApi()} is called and there is no instance of api specific for this
	 * provider instantiated whether due to first call to {@link #getApi()} or due to invalidation
	 * of the previous api instance via {@link #invalidateApi()}.
	 *
	 * @return New instance of api implementation specific for this provider.
	 * @see #onPrepareApi(Object)
	 */
	@NonNull
	protected abstract A onCreateApi();

	/**
	 * Invoked whenever {@link #getApi()} is called to make some additional preparation of the given
	 * <var>api</var> instance before it is returned to the user.
	 *
	 * @param api The current api instance cached or just created by this provider.
	 * @return The passed api prepared to be used.
	 * @see #onCreateApi()
	 */
	@NonNull
	protected A onPrepareApi(@NonNull final A api) {
		return api;
	}

	/**
	 * Makes the current api implementation cached by this provider invalid so when {@link #getApi()}
	 * is next time called a new instance of api will be requested via {@link #onCreateApi()}.
	 *
	 * @see #onPrepareApi(Object)
	 */
	protected void invalidateApi() {
		if (mApi != null) {
			synchronized (LOCK) {
				this.mApi = null;
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
