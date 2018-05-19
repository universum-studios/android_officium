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

import android.content.ServiceConnection;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;

/**
 * Manager that may be used for accessing of multiple instances of services (theirs PROXYies) and
 * also of their configurations. Each instance of ServiceManager must have its base url specified
 * via {@link #ServiceManager(String)} constructor. This base url is than attached to each
 * {@link ServiceConnection} object that is created whenever a desired services PROXY instance is
 * requested via {@link #services(Class)} or its configuration is requested via {@link #servicesConfiguration(Class)}.
 * <p>
 * As it is described in {@link ServicesConfiguration} class, each services configuration object
 * caches its services PROXY instance and re-creates it only in case of configuration change. Also
 * ServiceManager caches each instance of these configuration objects and maps them to their
 * corresponding services interface class.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class ServiceManager {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceManager";

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
	 * Base url specified for services accessible via this manager.
	 */
	// todo: make final in final production release of version 2.0.0
	private HttpUrl baseUrl;

	/**
	 * Map containing services configuration objects mapped to class of services that they configure.
	 */
	private final Map<Class<?>, ServicesConfiguration> services = new HashMap<>(1);

	/**
	 * End point for services managed by this manager.
	 *
	 * @deprecated Use {@link #baseUrl} instead.
	 */
	@Deprecated private EndPoint endPoint;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #ServiceManager(HttpUrl)} where the specified <var>baseUrl</var> string will
	 * be used to create {@link HttpUrl} via {@link HttpUrl#parse(String)}.
	 *
	 * @param baseUrl The desired base url for services to be accessible via this manager.
	 */
	public ServiceManager(@NonNull final String baseUrl) {
		this(HttpUrl.parse(baseUrl));
	}

	/**
	 * Creates a new instance of ServiceManager with the specified <var>baseUrl</var>.
	 *
	 * @param baseUrl The desired base url for services to be accessible via this manager.
	 *
	 * @see Retrofit.Builder#baseUrl(String)
	 */
	public ServiceManager(@NonNull final HttpUrl baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * <b>This constructor is deprecated and will be removed in the final production release of version 2.0.0.</b>
	 * <p>
	 * Creates a new instance of ServiceManager with the specified <var>endPoint</var>.
	 *
	 * @param endPoint The desired end point. This end point will be attached to each services
	 *                 configuration created via {@link #onCreateServicesConfiguration(Class)}.
	 * @see #getEndPoint()
	 *
	 * @deprecated Use {@link #ServiceManager(HttpUrl)} instead.
	 */
	@Deprecated
	public ServiceManager(@NonNull final EndPoint endPoint) {
		this.endPoint = endPoint;
		this.baseUrl = HttpUrl.parse(endPoint.getBaseUrl());
	}

	/**
	 * <b>This constructor is deprecated and will be removed in the final production release of version 2.0.0.</b>
	 * <p>
	 * Creates a new instance of ServiceManager without specified end point.
	 *
	 * @see #setEndPoint(EndPoint)
	 *
	 * @deprecated Use {@link #ServiceManager(HttpUrl)} instead.
	 */
	@Deprecated
	public ServiceManager() {
		this.endPoint = null;
		this.baseUrl = null;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the base url specified for this manager's services.
	 *
	 * @return The base url.
	 */
	@NonNull public HttpUrl getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Same as {@link #setEndPoint(EndPoint)} with EndPoint instance providing the given <var>baseUrl</var>
	 * as its base url via {@link EndPoint#getBaseUrl()}.
	 *
	 * @param baseUrl Base url of the desired end point.
	 *
	 * @deprecated Use {@link #ServiceManager(HttpUrl)} instead.
	 */
	@Deprecated public void setEndPoint(@NonNull final String baseUrl) {
		setEndPoint(new EndPoint() {

			/**
			 */
			@Override @NonNull public String getBaseUrl() {
				return baseUrl;
			}
		});
	}

	/**
	 * Sets an end point for this manager.
	 * <p>
	 * The specified end point will be attached to each services configuration created via
	 * {@link #onCreateServicesConfiguration(Class)}.
	 * <p>
	 * <b>Note</b> that this will not change end points of the services created prior to this call.
	 *
	 * @param endPoint The desired end point.
	 * @see #getEndPoint()
	 *
	 * @deprecated Use {@link #ServiceManager(String)} instead.
	 */
	@Deprecated public void setEndPoint(@NonNull final EndPoint endPoint) {
		this.endPoint = endPoint;
		this.baseUrl = HttpUrl.parse(endPoint.getBaseUrl());
	}

	/**
	 * Returns the end point specified for this manager.
	 *
	 * @return This manager's end point used for services configuration or {@code null} if no end
	 * point has been specified.
	 *
	 * @deprecated Use {@link #getBaseUrl()} instead.
	 */
	@Deprecated @Nullable public EndPoint getEndPoint() {
		return endPoint;
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
	@NonNull public <S> S services(@NonNull final Class<S> servicesInterface) {
		return ensureServicesConfiguration(servicesInterface).services();
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
	@NonNull public <S> ServicesConfiguration<S> servicesConfiguration(@NonNull final Class<S> servicesInterface) {
		return ensureServicesConfiguration(servicesInterface);
	}

	/**
	 * Ensures that the services configuration object is created for the given <var>servicesInterface</var>.
	 *
	 * @param servicesInterface The services interface for which to create new configuration if it
	 *                          is not created yet.
	 */
	@SuppressWarnings("unchecked")
	private <S> ServicesConfiguration<S> ensureServicesConfiguration(final Class<S> servicesInterface) {
		synchronized (services) {
			ServicesConfiguration<S> servicesConfiguration = (ServicesConfiguration<S>) services.get(servicesInterface);
			if (servicesConfiguration == null) {
				servicesConfiguration = onCreateServicesConfiguration(servicesInterface);
				services.put(servicesInterface, servicesConfiguration);
			}
			return servicesConfiguration;
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
	@CallSuper @NonNull protected <S> ServicesConfiguration<S> onCreateServicesConfiguration(@NonNull final Class<S> servicesInterface) {
		final ServicesConfiguration<S> configuration = new ServicesConfiguration<>(servicesInterface);
		configuration.retrofitBuilder().baseUrl(baseUrl);
		return configuration;
	}

	/*
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
		private final Retrofit.Builder builder = new Retrofit.Builder();

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
		 * Retrofit instance created by {@link #builder} for the current configuration parameters.
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
		private volatile boolean changed = true;

		/**
		 * Creates a new instance of ServicesConfiguration for the specified <var>servicesInterface</var>
		 * class.
		 *
		 * @param servicesInterface Class of the services interface used to create PROXY services
		 *                          instance.
		 */
		ServicesConfiguration(@NonNull final Class<S> servicesInterface) {
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
		@NonNull public Retrofit.Builder retrofitBuilder() {
			return builder;
		}

		/**
		 * Returns the Retrofit instance for the current configuration.
		 *
		 * @return Retrofit instance.
		 * @see #retrofitBuilder()
		 * @see #invalidate()
		 */
		@NonNull public Retrofit retrofit() {
			this.ensureValid();
			return retrofit;
		}

		/**
		 * Returns the instance of services PROXY created from services interface specified for
		 * this configuration.
		 *
		 * @return Services PROXY configured according to this configuration.
		 */
		@NonNull S services() {
			this.ensureValid();
			return services;
		}

		/**
		 * Ensures that the current Retrofit instance and services PROXY are valid according to the
		 * current configuration.
		 */
		private void ensureValid() {
			if (changed) {
				synchronized (builder) {
					this.retrofit = builder.build();
					this.services = retrofit.create(servicesInterface);
					this.changed = false;
				}
			}
		}

		/**
		 * Invalidates the current configuration. Next call to {@link ServiceManager#services(Class)}
		 * with services interface associated with this configuration will create a new instance
		 * of the desired services PROXY.
		 */
		public void invalidate() {
			this.changed = true;
		}
	}
}