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
import android.support.annotation.VisibleForTesting;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A {@link Call} implementation that wraps original Retrofit call to provide functionality that
 * supports association of {@link ServiceCallback} with the related service along with service request.
 * <p>
 * Each instance of service call need to be created with the original call via {@link #ServiceCall(Call)}
 * constructor along with id of the corresponding service via {@link #withServiceId(int)}. The provided
 * service id is than attached to the callback when {@link #enqueue(ServiceCallback)} is called
 * along with the request id which this method returns. Subclasses may implement {@link #nextRequestId()}
 * to generate custom unique id for a specific request. In such case, do not forget to properly override
 * also {@link #clone()} method.
 *
 * @author Martin Albedinsky
 * @since 1.2
 */
public class ServiceCall<T> implements Call<T> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceCall";

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
	 * Original Retrofit call to which this service call delegates all its methods.
	 */
	@VisibleForTesting final Call<T> call;

	/**
	 * Id of the service with which is this call associated.
	 */
	@VisibleForTesting Integer serviceId;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceCall with the given original Retrofit <var>call</var>.
	 *
	 * @param call The Retrofit call to which will be this service call delegating its methods.
	 */
	public ServiceCall(@NonNull final Call<T> call) {
		this.call = call;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Specifies id of the service to be this call associated with. The provided id will be also
	 * attached to the {@link ServiceCallback} when {@link #enqueue(ServiceCallback)} is called
	 * and the callback does not have service id attached yet.
	 *
	 * @param serviceId Id of the desired service to be this call associated with.
	 * @return This service call to allow methods chaining.
	 * @throws UnsupportedOperationException If service id for this call has been already specified.
	 */
	public ServiceCall<T> withServiceId(final int serviceId) {
		if (this.serviceId == null) {
			this.serviceId = serviceId;
			return this;
		}
		throw new UnsupportedOperationException("Service id is already specified!");
	}

	/**
	 */
	@Override public Response<T> execute() throws IOException {
		return call.execute();
	}

	/**
	 * Like {@link #enqueue(Callback)}, but this method also obtains and returns unique id of this
	 * asynchronous request so it may be later identified through the associated service objects:
	 * {@link ServiceCallback} which results to either {@link ServiceResponse} or {@link ServiceError}.
	 * <p>
	 * Also id of the service specified for this service call will be attached to the callback if
	 * it has no service id attached yet.
	 *
	 * @param callback The desired callback that should be notified when service request is finished.
	 * @return Unique id of this request for later identification of received callback, either
	 * successful response or error.
	 * @see ServiceObject#getServiceId()
	 * @see ServiceObject#getRequestId()
	 */
	@NonNull public String enqueue(@NonNull final ServiceCallback<T> callback) {
		final String requestId = nextRequestId();
		ServiceCallback.associateWith(callback, serviceId, requestId);
		enqueue((Callback<T>) callback);
		return requestId;
	}

	/**
	 * Called to generate a unique id for the current service request that has been requested to be
	 * executed asynchronously via {@link #enqueue(ServiceCallback)}.
	 * <p>
	 * This implementation returns a string representation of the current time in milliseconds as
	 * {@link Long#toString(long) Long.toString(System.currentTimeMillis())}.
	 *
	 * @return Unique id for the current request that will be attached to the service callback.
	 */
	@NonNull protected String nextRequestId() {
		return Long.toString(System.currentTimeMillis());
	}

	/**
	 */
	@Override public void enqueue(@NonNull final Callback<T> callback) {
		this.call.enqueue(callback);
	}

	/**
	 */
	@Override public boolean isExecuted() {
		return call.isExecuted();
	}

	/**
	 */
	@Override public void cancel() {
		this.call.cancel();
	}

	/**
	 */
	@Override public boolean isCanceled() {
		return call.isCanceled();
	}

	/**
	 */
	@Override public Request request() {
		return call.request();
	}

	/**
	 * Creates a new clone of this service call with the original <b>Retrofit</b> call also cloned
	 * and with the same service id as specified via {@link #withServiceId(int)} (if any).
	 */
	@SuppressWarnings("CloneDoesntCallSuperClone")
	@Override public ServiceCall<T> clone() {
		final ServiceCall<T> serviceCall = new ServiceCall<>(call.clone());
		serviceCall.serviceId = serviceId;
		return serviceCall;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}