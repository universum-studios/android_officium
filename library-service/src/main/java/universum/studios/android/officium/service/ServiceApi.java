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
 * @param <M> Type of the service manager used by the ServiceApi implementation.
 * @author Martin Albedinsky
 * @see ServiceApiProvider
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
	protected M mManager;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceApi with the specified service <var>manager</var>.
	 *
	 * @param manager The desired service manager used to configure and access services.
	 */
	protected ServiceApi(@NonNull M manager) {
		this.mManager = manager;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns an instance of services PROXY for the specified <var>servicesInterface</var>.
	 *
	 * @param servicesInterface The services interface for which to the associated PROXY.
	 * @param <S>               Type of the requested services.
	 * @return Services PROXY ready to be used for requests execution.
	 * @see ServiceManager#services(Class)
	 */
	@NonNull
	protected final <S> S services(@NonNull Class<S> servicesInterface) {
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
	@NonNull
	protected final <S> ServiceManager.ServicesConfiguration<S> servicesConfiguration(@NonNull Class<S> servicesInterface) {
		return mManager.servicesConfiguration(servicesInterface);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
