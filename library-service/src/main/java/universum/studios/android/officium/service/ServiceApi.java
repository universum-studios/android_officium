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
 *      String END_POINT = "http://messenger.com";
 *      String VERSION = "1";
 *      String URL = "/api/v" + VERSION;
 *
 *      void signIn(&#64;NonNull SignInRequest request);
 *
 *      void forgotPassword(&#64;NonNull ForgotPasswordRequest request);
 * }
 *
 * // Retrofit services definition interface.
 * interface Services {
 *
 *      &#64;POST(Api.URL + "/signIn")
 *      void signIn(
 *          &#64;Body &#64;NonNull SignInRequest request,
 *          &#64;NonNull Callback&lt;SignInResponse&gt; callback
 *      );
 *
 *      &#64;POST(Api.URL + "/forgotPassword")
 *      void forgotPassword(
 *          &#64;Body &#64;NonNull ForgotPasswordRequest request,
 *          &#64;NonNull Callback&lt;ForgotPasswordResponse&gt; callback
 *      );
 * }
 *
 * // Api implementation provided by ApiProvider.
 * final class ApiImpl implements Api {
 *
 *      ApiImpl(&#64;NonNull Bus bus) {
 *          super(new ServiceManager(), bus);
 *      }
 *
 *      &#64;Override
 *      public void signIn(&#64;NonNull SignInRequest request) {
 *          services(Services.class).signIn(
 *              request,
 *              new ServiceCallback&lt;SignInResponse&gt;(1, mBus)
 *          );
 *      }
 *
 *      &#64;Override
 *      public void forgotPassword(&#64;NonNull ForgotPasswordRequest request) {
 *          services(Services.class).forgotPassword(
 *              request,
 *              new ServiceCallback&lt;ForgotPasswordResponse&gt;(2, mBus)
 *          );
 *      }
 * }
 *
 * // Provider that provides instance of ApiImpl for application clients.
 * public final class ApiProvider extends ServiceApiProvider&lt;Api&gt; {
 *
 *     public ApiProvider(&#64;NonNull Bus bus) {
 *         super(bus);
 *     }
 *
 *     &#64;NonNull
 *     &#64;Override
 *     protected Api onCreateApi(@NonNull Bus bus) {
 *          return new ApiImpl(bus);
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
	 */
	@NonNull
	@Deprecated
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

	// todo: implement protected final M getManager() ....

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