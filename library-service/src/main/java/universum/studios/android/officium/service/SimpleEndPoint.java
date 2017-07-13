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
package universum.studios.android.officium.service;

import android.support.annotation.NonNull;

/**
 * Simple implementation of {@link EndPoint}.
 *
 * @author Martin Albedinsky
 */
public class SimpleEndPoint implements EndPoint {

    /*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SimpleEndPoint";

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
	 * Base url of this end point.
	 */
	private final String mBaseUrl;
	 
	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of SimpleEndPoint with the specified <var>baseUrl</var>.
	 *
	 * @param baseUrl The desired base url for the new end point.
	 */
	public SimpleEndPoint(@NonNull String baseUrl) {
		this.mBaseUrl = baseUrl;
	}
	 
	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@NonNull
	@Override
	public String getBaseUrl() {
		return mBaseUrl;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
