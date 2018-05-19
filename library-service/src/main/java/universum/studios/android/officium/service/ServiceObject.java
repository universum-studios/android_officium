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
 * Interface for objects that may be associated with services and theirs requests. A service to which
 * is a specific service object associated may be identified by {@link #getServiceId()}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public interface ServiceObject {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Constant used to identify that a specific ServiceObject is not associated with any service.
	 */
	int NO_SERVICE = -1;

	/**
	 * Constant used to identify that a specific ServiceObject is not associated with any particular
	 * service request.
	 */
	String NO_REQUEST = "";

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Specifies an id of the service with which is this service object associated.
	 * <p>
	 * <b>Note, that changing of this id should not be permitted if it has been already specified,
	 * so service object implementations are allowed to throw an exception if such action is to
	 * be performed.</b>
	 *
	 * @param serviceId The desired service id.
	 * @see #getServiceId()
	 */
	void setServiceId(int serviceId);

	/**
	 * Returns the id of service with which is this service object associated.
	 *
	 * @return Service id specified for this object or {@link #NO_SERVICE} if there was no service
	 * id provided.
	 * @see #setServiceId(int)
	 */
	int getServiceId();

	/**
	 * Sets an id of the request with which is this service object associated.
	 * <p>
	 * <b>Note, that changing of this id should not be permitted if it has been already specified,
	 * so service object implementations are allowed to throw an exception if such action is to
	 * be performed.</b>
	 *
	 * @param requestId The desired request id. Should be unique among all requests made for a specific
	 *                  service or better, unique among all requests made through services API to
	 *                  really uniquely identify a desired request.
	 * @see #getRequestId()
	 */
	void setRequestId(@NonNull String requestId);

	/**
	 * Returns the unique id of the request with which is this service object associated.
	 *
	 * @return Request id specified for this object or {@link #NO_REQUEST} if there was no request
	 * id provided.
	 * @see #setRequestId(String)
	 */
	@NonNull String getRequestId();
}