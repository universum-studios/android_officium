/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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
package universum.studios.android.officium.service.adapter;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import universum.studios.android.officium.service.ServiceCall;

/**
 * A {@link CallAdapter} implementation which can adapt {@link Call} to {@link ServiceCall}.
 *
 * @author Martin Albedinsky
 */
final class ServiceCallAdapter<R> implements CallAdapter<R, ServiceCall<R>> {

    /*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceCallAdapter";

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
	 * Type of the response associated with the {@link Call} this adapter should adapt to
	 * {@link ServiceCall} whenever {@link #adapt(Call)} is invoked.
	 */
	private final Type mResponseType;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceCallAdapter with the specified <var>responseType</var>.
	 *
	 * @param responseType Type of the response associated with call to be adapted by this adapter.
	 */
	ServiceCallAdapter(final Type responseType) {
		this.mResponseType = responseType;
	}
	 
	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	public Type responseType() {
		return mResponseType;
	}

	/**
	 */
	@Override
	public ServiceCall<R> adapt(@NonNull final Call<R> call) {
		return new ServiceCall<>(call);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
