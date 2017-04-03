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
 * along with the request id which this method returns. Subclasses may implement {@link #requestId()}
 * to generate custom unique id for a specific request. In such case, do not forget to properly override
 * also {@link #clone()} method.
 *
 * @author Martin Albedinsky
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
	private final Call<T> mCall;

	/**
	 * Id of the service with which is this call associated.
	 */
	private Integer mServiceId;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceCall with the given original Retrofit <var>call</var>.
	 *
	 * @param call The Retrofit call to which will be this service call delegating its methods.
	 */
	public ServiceCall(@NonNull final Call<T> call) {
		this.mCall = call;
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
		if (mServiceId == null) {
			this.mServiceId = serviceId;
			return this;
		}
		throw new UnsupportedOperationException("Service id is already specified!");
	}

	/**
	 */
	@Override
	public Response<T> execute() throws IOException {
		return mCall.execute();
	}

	/**
	 * Like {@link #enqueue(Callback)}, but this method also obtains and returns unique id of this
	 * asynchronous request so it may be later identified through the associated service objects:
	 * {@link ServiceCallback} which results to either {@link ServiceResponse} or {@link ServiceError}.
	 * <p>
	 * Also id of the service specified for this service call will be attached to the callback if
	 * it has no service id attached yet.
	 *
	 * @param callback The desired callback that should be notified when service requests is finished.
	 * @return Unique id of this request for later identification of received callback, either
	 * successful response or error.
	 * @see ServiceObject#getServiceId()
	 * @see ServiceObject#getRequestId()
	 */
	@NonNull
	public String enqueue(@NonNull final ServiceCallback<T> callback) {
		final String requestId = requestId();
		ServiceCallback.associateWith(callback, mServiceId, requestId);
		enqueue((Callback<T>) callback);
		return requestId;
	}

	/**
	 * Called to obtain a unique id for the current service request that has been requested to be
	 * executed asynchronously via {@link #enqueue(ServiceCallback)}.
	 * <p>
	 * This implementation returns a string representation of the current time in milliseconds as
	 * {@link Long#toString(long) Long.toString(System.currentTimeMillis())}.
	 *
	 * @return Unique id for the current request that will be attached to the service callback.
	 */
	@NonNull
	protected String requestId() {
		return Long.toString(System.currentTimeMillis());
	}

	/**
	 */
	@Override
	public void enqueue(@NonNull final Callback<T> callback) {
		mCall.enqueue(callback);
	}

	/**
	 */
	@Override
	public boolean isExecuted() {
		return mCall.isExecuted();
	}

	/**
	 */
	@Override
	public void cancel() {
		mCall.cancel();
	}

	/**
	 */
	@Override
	public boolean isCanceled() {
		return mCall.isCanceled();
	}

	/**
	 */
	@Override
	public Request request() {
		return mCall.request();
	}

	/**
	 * Creates a new clone of this service call with the original <b>Retrofit</b> call also cloned
	 * and with the same service id as specified via {@link #withServiceId(int)} (if any).
	 */
	@Override
	@SuppressWarnings("CloneDoesntCallSuperClone")
	public Call<T> clone() {
		final ServiceCall<T> serviceCall = new ServiceCall<>(mCall.clone());
		serviceCall.mServiceId = mServiceId;
		return serviceCall;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
