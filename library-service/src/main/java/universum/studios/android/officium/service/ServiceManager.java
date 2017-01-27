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

import android.content.ServiceConnection;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import retrofit2.Retrofit;

/**
 * Manager that may be used for accessing of multiple instances of services (theirs PROXYies) and
 * also of theirs configuration. Each instance of ServiceManager should have its end point specified
 * via {@link #setEndPoint(EndPoint)}. This end point is than attached to each {@link ServiceConnection}
 * object that is created whenever a desired services PROXY instance is requested via {@link #services(Class)}
 * or its configuration is requested via {@link #servicesConfiguration(Class)}.
 * <p>
 * As it is described in {@link ServicesConfiguration} class, each services configuration object
 * caches its services PROXY instance and re-creates it only in case of configuration change. Also
 * ServiceManager caches each instance of these configuration objects and maps them to theirs
 * corresponding services interface.
 *
 * @author Martin Albedinsky
 */
public class ServiceManager {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceManager";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Map containing services configuration objects mapped to class of services that they configure.
	 */
	private final HashMap<Class<?>, ServicesConfiguration> mServices = new HashMap<>(1);

	/**
	 * End point for services managed by this manager.
	 */
	private EndPoint mEndPoint;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceManager without specified end point.
	 *
	 * @see #setEndPoint(EndPoint)
	 */
	public ServiceManager() {
		this.mEndPoint = null;
	}

	/**
	 * Creates a new instance of ServiceManager with the specified <var>endPoint</var>.
	 *
	 * @param endPoint The desired end point. This end point will be attached to each services
	 *                 configuration created via {@link #onCreateServicesConfiguration(Class)}.
	 * @see #getEndPoint()
	 */
	public ServiceManager(@NonNull EndPoint endPoint) {
		this.mEndPoint = endPoint;
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Same as {@link #setEndPoint(EndPoint)} with EndPoint instance providing the given <var>baseUrl</var>
	 * as its base url via {@link EndPoint#getBaseUrl()}.
	 *
	 * @param baseUrl Base url of the desired end point.
	 */
	public void setEndPoint(@NonNull final String baseUrl) {
		setEndPoint(new EndPoint() {

			/**
			 */
			@NonNull
			@Override
			public String getBaseUrl() {
				return baseUrl;
			}
		});
	}

	/**
	 * Sets an end point for this manager.
	 * <p>
	 * The specified end point will be attached to each services configuration created via
	 * {@link #onCreateServicesConfiguration(Class)}.
	 *
	 * @param endPoint The desired end point.
	 * @see #getEndPoint()
	 */
	public void setEndPoint(@NonNull EndPoint endPoint) {
		this.mEndPoint = endPoint;
	}

	/**
	 * Returns the end point specified for this manager.
	 *
	 * @return This manager's end point used for services configuration or {@code null} if no end
	 * point has been specified.
	 */
	@Nullable
	public EndPoint getEndPoint() {
		return mEndPoint;
	}

	/**
	 * Returns the PROXY instance for the requested <var>servicesInterface</var>. The returned PROXY
	 * can be immediately used for services invocation that are declared by the given services interface.
	 * <p>
	 * Each services interface can be configured via its corresponding configuration object that can
	 * be obtained via {@link #servicesConfiguration(Class)}.
	 *
	 * @param servicesInterface The services interface for which to access its PROXY instance.
	 * @param <S>               Type of the services interface.
	 * @return PROXY for the desired services interface ready for services invocation.
	 * @see #servicesConfiguration(Class)
	 */
	@SuppressWarnings("unchecked")
	public <S> S services(@NonNull Class<S> servicesInterface) {
		this.ensureHasServicesConfiguration(servicesInterface);
		return (S) mServices.get(servicesInterface).services();
	}

	/**
	 * Returns the services configuration object for the requested <var>servicesInterface</var>.
	 *
	 * @param servicesInterface The services interface for which to return its configuration.
	 * @param <S>               Type of the services interface.
	 * @return Configuration object that can be used to configure the desired services.
	 * @see ServicesConfiguration
	 * @see #services(Class)
	 */
	@SuppressWarnings("unchecked")
	public <S> ServicesConfiguration<S> servicesConfiguration(@NonNull Class<S> servicesInterface) {
		this.ensureHasServicesConfiguration(servicesInterface);
		return mServices.get(servicesInterface);
	}

	/**
	 * Ensures that the services configuration object is created for the given <var>servicesInterface</var>.
	 *
	 * @param servicesInterface The services interface for which to create new configuration if it
	 *                          is not created yet.
	 */
	private void ensureHasServicesConfiguration(Class<?> servicesInterface) {
		synchronized (mServices) {
			final ServicesConfiguration servicesConfiguration = mServices.get(servicesInterface);
			if (servicesConfiguration == null) mServices.put(
					servicesInterface,
					onCreateServicesConfiguration(servicesInterface)
			);
		}
	}

	/**
	 * Invoked whenever {@link #servicesConfiguration(Class)} or {@link #services(Class)} is called
	 * for the first time for the specified <var>servicesInterface</var>.
	 * <p>
	 * Default implementation creates a new instance of ServicesConfiguration with the given services
	 * interface and end point specified for this manager (if any).
	 * <p>
	 * Custom implementations of ServiceManager may override this method to perform default services
	 * configuration for them specific.
	 *
	 * @param servicesInterface Class of the services interface for which to create new configuration
	 *                          object.
	 * @return New services configuration for the services interface.
	 */
	@NonNull
	@CallSuper
	protected ServicesConfiguration onCreateServicesConfiguration(@NonNull Class<?> servicesInterface) {
		final ServicesConfiguration configuration = new ServicesConfiguration<>(servicesInterface);
		if (mEndPoint != null) configuration.retrofitBuilder().baseUrl(mEndPoint.getBaseUrl());
		return configuration;
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * Class used for configuration of a specific Retrofit services interface. Each configuration
	 * object manages a single instance of {@link Retrofit} along with its corresponding
	 * {@link Retrofit.Builder} that are used to build a PROXY instance for services interface for
	 * the current configuration.
	 * <p>
	 * Created instance of services PROXY is cached so it does not need to be created each time it
	 * is requested via {@link ServiceManager#services(Class)}. This instance of PROXY is re-created
	 * only in case where there has been performed some configuration change and {@link #invalidate()}
	 * has been called to indicate the services PROXY is no longer valid.
	 *
	 * @param <S> Type of the services interface specific for this configuration.
	 */
	public static final class ServicesConfiguration<S> {

		/**
		 * Builder used to create an instance of {@link #retrofit} for the current configuration
		 * parameters.
		 */
		private final Retrofit.Builder BUILDER = new Retrofit.Builder();

		/**
		 * Class of services interface used to create {@link #services} PROXY by {@link #retrofit}.
		 */
		private final Class<S> servicesInterface;

		/**
		 * Current instance (if any) of services PROXY created using {@link #retrofit} for the
		 * current configuration.
		 *
		 * @see #services()
		 */
		private S services;

		/**
		 * Retrofit instance created by {@link #BUILDER} for the current configuration parameters.
		 * This retrofit instance is used to create an instance of services PROXY whenever
		 * {@link #services()} is invoked and there has been registered configuration change.
		 *
		 * @see Retrofit#create(Class)
		 */
		private Retrofit retrofit;

		/**
		 * Flag indicating whether this services configuration has changed or not. If {@code true}
		 * the current instance of {@link #retrofit} should be updated the next time services
		 * are requested via {@link #services()}.
		 */
		private boolean changed = true;

		/**
		 * Creates a new instance of ServicesConfiguration for the specified <var>servicesInterface</var>
		 * class.
		 *
		 * @param servicesInterface Class of the services interface used to create PROXY services
		 *                          instance.
		 */
		private ServicesConfiguration(@NonNull Class<S> servicesInterface) {
			this.servicesInterface = servicesInterface;
		}

		/**
		 * Returns the Retrofit builder that is used to build instance of Retrofit and instance of
		 * services PROXY for this services configuration.
		 * <p>
		 * When any of the current parameters of the builder is changed do not forget to call
		 * {@link #invalidate()} so the change is accepted and new updated services PROXY is created
		 * in next call to {@link ServiceManager#services(Class)}.
		 *
		 * @return Retrofit builder that may be used to configure services PROXY associated with this
		 * configuration and may be obtained via {@link ServiceManager#services(Class)}.
		 */
		@NonNull
		public Retrofit.Builder retrofitBuilder() {
			return BUILDER;
		}

		/**
		 * Invalidates the current configuration. Next call to {@link ServiceManager#services(Class)}
		 * with services interface associated with this configuration will create a new instance
		 * of the desired services PROXY.
		 */
		public void invalidate() {
			this.changed = true;
		}

		/**
		 * Returns the Retrofit instance for the current configuration.
		 *
		 * @return Retrofit instance.
		 * @see #retrofitBuilder()
		 * @see #invalidate()
		 */
		@NonNull
		public Retrofit retrofit() {
			this.ensureValid();
			return retrofit;
		}

		/**
		 * Returns the instance of services PROXY created from services interface specified for
		 * this configuration.
		 *
		 * @return Services PROXY configured according to this configuration.
		 */
		@NonNull
		private S services() {
			this.ensureValid();
			return services;
		}

		/**
		 * Ensures that the current Retrofit instance and services PROXY are valid according to the
		 * current configuration.
		 */
		private void ensureValid() {
			synchronized (BUILDER) {
				if (changed) {
					this.retrofit = BUILDER.build();
					this.services = retrofit.create(servicesInterface);
					this.changed = false;
				}
			}
		}
	}
}
