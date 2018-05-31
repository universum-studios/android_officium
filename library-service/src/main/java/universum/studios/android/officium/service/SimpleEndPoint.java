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
package universum.studios.android.officium.service;

import android.support.annotation.NonNull;

/**
 * <b>This class is deprecated and will be removed in the final production release of version 2.0.0.</b>
 * <p>
 * Simple implementation of {@link EndPoint}.
 *
 * @author Martin Albedinsky
 *
 * @deprecated Use simple base url instead.
 */
@Deprecated public class SimpleEndPoint implements EndPoint {

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