/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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
package universum.studios.android.officium.service.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import universum.studios.android.officium.service.ServiceCall;

/**
 * A {@link CallAdapter.Factory} implementation which can provide instance of {@link CallAdapter}
 * that can adapt {@link Call} to {@link ServiceCall}.
 *
 * @author Martin Albedinsky
 * @since 1.2
 */
public final class ServiceCallAdapterFactory extends CallAdapter.Factory {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceCallAdapterFactory";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceCallAdapterFactory.
	 */
	private ServiceCallAdapterFactory() {
		super();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates and returns a new instance of ServiceCallAdapterFactory.
	 *
	 * @return Instance of call adapter factory ready to be used.
	 */
	@NonNull public static ServiceCallAdapterFactory create() {
		return new ServiceCallAdapterFactory();
	}

	/**
	 */
	@Override @Nullable public CallAdapter<?, ?> get(@NonNull final Type returnType, @NonNull final Annotation[] annotations, @NonNull final Retrofit retrofit) {
		final Class<?> rawType = getRawType(returnType);
		if (!rawType.equals(ServiceCall.class)) {
			return null;
		}
		if (!(returnType instanceof ParameterizedType)) {
			throw new IllegalStateException(
					"ServiceCall return type must be parametrized as ServiceCall<Foo> or ServiceCall<? extends Foo>."
			);
		}
		return new ServiceCallAdapter<>(getParameterUpperBound(0, (ParameterizedType) returnType));
	}

	/*
	 * Inner classes ===============================================================================
	 */
}