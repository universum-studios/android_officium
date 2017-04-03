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
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Base implementation of {@link ServiceObject} for objects associated to services.
 *
 * @author Martin Albedinsky
 */
public abstract class BaseServiceObject implements ServiceObject {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "BaseServiceObject";

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
	 * Service id to which is this object associated.
	 */
	Integer mServiceId;

	/**
	 * Request id to which is this object associated.
	 */
	String mRequestId;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Associates the given <var>serviceObject</var> with the specified <var>serviceId</var> and
	 * <var>requestId</var>.
	 *
	 * @param serviceObject The service object to be associated with the specified service and request.
	 * @param serviceId     Id of the service to associate the service object with. May be {@code null}
	 *                      to not associate.
	 * @param requestId     Id of the service request to associate the service object with. May be {@code null}
	 *                      to not associate.
	 */
	static void associateWith(@NonNull final ServiceObject serviceObject, @Nullable final Integer serviceId, @Nullable final String requestId) {
		if (serviceId != null && serviceObject.getServiceId() == NO_SERVICE) serviceObject.setServiceId(serviceId);
		if (requestId != null && TextUtils.isEmpty(serviceObject.getRequestId())) serviceObject.setRequestId(requestId);
	}

	/**
	 * @throws UnsupportedOperationException If service id has been already specified for this
	 *                                       service object.
	 */
	@Override
	public final void setServiceId(final int serviceId) {
		if (mServiceId == null) {
			this.mServiceId = serviceId;
		} else {
			throw new UnsupportedOperationException("Cannot change already specified service id(" + mServiceId + ")!");
		}
	}

	/**
	 */
	@Override
	public final int getServiceId() {
		return mServiceId == null ? NO_SERVICE : mServiceId;
	}

	/**
	 * @throws UnsupportedOperationException If request id has been already specified for this
	 *                                       service object.
	 */
	@Override
	public final void setRequestId(@NonNull final String requestId) {
		if (mRequestId == null) {
			this.mRequestId = requestId;
		} else {
			throw new UnsupportedOperationException("Cannot change already specified request id(" + mRequestId + ")!");
		}
	}

	/**
	 */
	@NonNull
	@Override
	public final String getRequestId() {
		return mRequestId == null ? NO_REQUEST : mRequestId;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
