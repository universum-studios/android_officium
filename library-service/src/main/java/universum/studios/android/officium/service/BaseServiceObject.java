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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Base implementation of {@link ServiceObject} for objects associated to services.
 *
 * @author Martin Albedinsky
 * @since 1.0
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
	Integer serviceId;

	/**
	 * Request id to which is this object associated.
	 */
	String requestId;

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
		if (serviceId != null && serviceObject.getServiceId() == NO_SERVICE)
			serviceObject.setServiceId(serviceId);
		if (requestId != null && NO_REQUEST.equals(serviceObject.getRequestId()))
			serviceObject.setRequestId(requestId);
	}

	/**
	 * @throws UnsupportedOperationException If service id has been already specified for this
	 *                                       service object.
	 */
	@Override public final void setServiceId(final int serviceId) {
		if (this.serviceId == null) {
			this.serviceId = serviceId;
		} else {
			throw new UnsupportedOperationException("Cannot change already specified service id(" + this.serviceId + ")!");
		}
	}

	/**
	 */
	@Override public final int getServiceId() {
		return serviceId == null ? NO_SERVICE : serviceId;
	}

	/**
	 * @throws UnsupportedOperationException If request id has been already specified for this
	 *                                       service object.
	 */
	@Override public final void setRequestId(@NonNull final String requestId) {
		if (this.requestId == null) {
			this.requestId = requestId;
		} else {
			throw new UnsupportedOperationException("Cannot change already specified request id(" + this.requestId + ")!");
		}
	}

	/**
	 */
	@Override @NonNull public final String getRequestId() {
		return requestId == null ? NO_REQUEST : requestId;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}