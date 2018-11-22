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
package universum.studios.android.officium.service;

import androidx.annotation.NonNull;

/**
 * ServiceApiProvider represents a provider that can provide a single instance of {@link ServiceApi}
 * implementation. Already created api instance is cached within api provider due to performance
 * reasons. Instance of api can be obtained via {@link #getApi()}.
 * <p>
 * Each ServiceApiProvider instance need to be created with {@link ServiceApi.Factory} via
 * {@link #ServiceApiProvider(ServiceApi.Factory)} that is used by the provider to create instance
 * of the associated api when such api is first time requested. Implementations of ServiceApiProvider
 * are allowed to override {@link #onPrepareApi(Object)} method which may be used for additional
 * preparation of the api before it is returned to the caller.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @param <A> Type of the API provided by this provider.
 */
public class ServiceApiProvider<A> {

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
	 *
	 * @deprecated Use {@link #apiFactory} instead as synchronization lock.
	 */
	@Deprecated private final Object LOCK = new Object();

	/**
	 * Factory that is used by this provider to create single api instance.
	 */
	private ServiceApi.Factory<A> apiFactory;

	/**
	 * Cached api implementation created via {@link #onCreateApi()}.
	 */
	private volatile A api;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceApiProvider.
	 *
	 * @deprecated Use {@link #ServiceApiProvider(ServiceApi.Factory)} instead.
	 */
	@Deprecated public ServiceApiProvider() {
		super();
	}

	/**
	 * Creates a new instance of ServiceApiProvider with the given <var>apiFactory</var>.
	 *
	 * @param apiFactory The desired factory that will be used by the new provider to create a single
	 *                   instance of the api when it is first time requested via {@link #getApi()}.
	 */
	public ServiceApiProvider(@NonNull final ServiceApi.Factory<A> apiFactory) {
		this.apiFactory = apiFactory;
	}

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
	@SuppressWarnings("deprecation")
	@NonNull public final A getApi() {
		if (api == null) {
			synchronized (LOCK) {
				this.api = onCreateApi();
			}
		}
		return onPrepareApi(api);
	}

	/**
	 * Invoked whenever {@link #getApi()} is called in order to allow to make some additional
	 * preparation of the given <var>api</var> instance before it is returned to the caller.
	 *
	 * @param api The api instance provided by this provider.
	 * @return The passed api prepared to be used.
	 */
	@NonNull protected A onPrepareApi(@NonNull final A api) {
		return api;
	}

	/**
	 * Invoked whenever {@link #getApi()} is called and there is no instance of api specific for this
	 * provider instantiated whether due to first call to {@link #getApi()} or due to invalidation
	 * of the previous api instance via {@link #invalidateApi()}.
	 *
	 * @return New instance of api implementation specific for this provider.
	 * @see #onPrepareApi(Object)
	 *
	 * @deprecated Use {@link #ServiceApiProvider(ServiceApi.Factory)} instead.
	 */
	@Deprecated @NonNull protected A onCreateApi() {
		// todo: move instantiation of the api to the getApi() method when final production release of version 2.0.0 is ready
		if (apiFactory == null) {
			throw new IllegalStateException("No api factory provided!");
		}
		return apiFactory.create();
	}

	/**
	 * Makes the current api implementation cached by this provider invalid so when {@link #getApi()}
	 * is next time called a new instance of api will be requested via {@link #onCreateApi()}.
	 *
	 * @see #onPrepareApi(Object)
	 *
	 * @deprecated Api cannot be invalidated but rather its state changed.
	 */
	@Deprecated protected void invalidateApi() {
		if (api != null) {
			synchronized (LOCK) {
				this.api = null;
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}