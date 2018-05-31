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
 * ServiceApi represents a <b>facade</b> for services provided by a remote server.
 *
 * <h3>Sample API structure</h3>
 * Classes described below should be placed in same package: <b>ANDROID.APPLICATION.service</b>
 * <pre>
 * // Retrofit services definition interface.
 * interface Services {
 *
 *      &#64;POST("signIn")
 *      Single&lt;SignInResponse&gt; signIn(&#64;Body &#64;NonNull SignInRequest request);
 *
 *      &#64;POST("signOut")
 *      Single&lt;SignOutResponse&gt; forgotPassword(&#64;Body &#64;NonNull SignOutRequest request);
 * }
 *
 * // Api interface that is visible across whole application.
 * public interface Api {
 *
 *      String BASE_URL = "https://www.android.com/api/v1/";
 *
 *      Single&lt;SignInResponse&gt; signIn(&#64;NonNull SignInRequest request);
 *
 *      Single&lt;SignOutResponse&gt; signOut(&#64;NonNull SignOutRequest request);
 * }
 *
 *
 * // Api implementation provided by ApiProvider.
 * final class ApiImpl extends ServiceApi&lt;ServiceManager&gt; implements Api {
 *
 *      static final class ApiFactory implements ServiceApi.Factory&lt;Api&gt; {
 *
 *          &#64;Override &#64;NonNull public Api create() {
 *              return new ApiImpl()
 *          }
 *      }
 *
 *      ApiImpl() {
 *          super(new ServiceManager(Api.BASE_URL));
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
 *      private static final Object LOCK = new Object();
 *      private static volatile ApiProvider instance;
 *
 *      private ApiProvider() {
 *          super(new ApiImpl.ApiFactory());
 *      }
 *
 *      &#64;NonNull public static ApiProvider get() {
 *          if (instance == null) {
 *              synchronized (LOCK) {
 *                  instance = new ApiProvider();
 *              }
 *          }
 *          return instance;
 *      }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see ServiceApiProvider
 *
 * @param <M> Type of the service manager used by ServiceApi implementation.
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

	/**
	 * Basic interface for factories which may be used to create instances of {@link ServiceApi}.
	 *
	 * @author Martin Albedinsky
	 * @since 2.0
	 *
	 * @param <T> Type of the api of which instance this factory can create.
	 */
	public interface Factory<T> {

		/**
		 * Creates a new instance of api specific for this factory.
		 *
		 * @return New api ready to be used.
		 */
		@NonNull T create();
	}

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