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

import android.support.annotation.NonNull;

/**
 * ServiceApi represents an <b>access layer</b> for a single services interface or set of such service
 * interfaces.
 *
 * <h3>Sample API structure</h3>
 * Classes described below should be placed in same package: <b>ANDROID.APPLICATION.service</b>
 * <pre>
 * // Api interface that is visible across whole application.
 * public interface Api {
 *
 *      String BASE_URL = "https://www.android.com/";
 *      String VERSION = "1";
 *      String URL = "api/v" + VERSION;
 *
 *      Single&lt;SignInResponse&gt; signIn(&#64;NonNull SignInRequest request);
 *
 *      Single&lt;SignOutResponse&gt; signOut(&#64;NonNull SignOutRequest request);
 * }
 *
 * // Retrofit services definition interface.
 * interface Services {
 *
 *      &#64;POST(Api.URL + "/signIn")
 *      Single&lt;SignInResponse&gt; signIn(&#64;Body &#64;NonNull SignInRequest request);
 *
 *      &#64;POST(Api.URL + "/signOut")
 *      Single&lt;SignOutResponse&gt; forgotPassword(&#64;Body &#64;NonNull SignOutRequest request);
 * }
 *
 * // Api implementation provided by ApiProvider.
 * final class ApiImpl extends ServiceApi&lt;ServiceManager&gt; implements Api {
 *
 *      ApiImpl(&#64;NonNull ServiceManager manager) {
 *          super(manager);
 *      }
 *
 *      &#64;Override
 *      public Single&lt;SignInResponse&gt; signIn(&#64;NonNull SignInRequest request) {
 *          return services(Services.class).signIn(request);
 *      }
 *
 *      &#64;Override
 *      public Single&lt;SignOutResponse&gt; forgotPassword(&#64;NonNull ForgotPasswordRequest request) {
 *          return services(Services.class).forgotPassword(request);
 *      }
 * }
 *
 * // Provider that provides instance of Api for application clients.
 * public final class ApiProvider extends ServiceApiProvider&lt;Api&gt; {
 *
 *     public ApiProvider() {
 *         super();
 *     }
 *
 *     &#64;NonNull
 *     &#64;Override
 *     protected Api onCreateApi() {
 *          return new ApiImpl();
 *     }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see ServiceApiProvider
 *
 * @param <M> Type of the service manager used by the ServiceApi implementation.
 */
public class ServiceApi<M extends ServiceManager> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceApi";

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
	 * Instance of {@link ServiceManager} used to configure and access services provided by this API.
	 *
	 * @deprecated Use {@link #getManager()} instead.
	 */
	@NonNull
	@Deprecated
	// todo: make private in final production release ...
	protected final M mManager;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceApi with the specified service <var>manager</var>.
	 *
	 * @param manager The desired service manager used to configure and access services.
	 */
	protected ServiceApi(@NonNull final M manager) {
		this.mManager = manager;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the service manager used by this api implementation.
	 *
	 * @return Service manager of this api.
	 */
	@NonNull protected final M getManager() {
		return mManager;
	}

	/**
	 * Returns an instance of services PROXY for the specified <var>servicesInterface</var>.
	 *
	 * @param servicesInterface The services interface for which to the associated PROXY.
	 * @param <S>               Type of the requested services.
	 * @return Services PROXY ready to be used for requests execution.
	 * @see ServiceManager#services(Class)
	 */
	@NonNull protected final <S> S services(@NonNull final Class<S> servicesInterface) {
		return mManager.services(servicesInterface);
	}

	/**
	 * Returns the configuration for services with the specified <var>servicesInterface</var>.
	 * <p>
	 * Returned configuration object can be used to change current configuration of a specific
	 * services PROXY instance.
	 *
	 * @param servicesInterface The services interface for which to return theirs configuration.
	 * @param <S>               Type of the services of the requested services configuration.
	 * @return Configuration for the requested services.
	 * @see ServiceManager#servicesConfiguration(Class)
	 */
	@NonNull protected final <S> ServiceManager.ServicesConfiguration<S> servicesConfiguration(@NonNull final Class<S> servicesInterface) {
		return mManager.servicesConfiguration(servicesInterface);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}