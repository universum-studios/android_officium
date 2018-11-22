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

import androidx.annotation.NonNull;

/**
 * <b>This interface is deprecated and will be removed in the final production release of version 2.0.0.</b>
 * <p>
 * End point that may be specified for {@link ServiceManager} to provide base url for all its
 * related services.
 *
 * @author Martin Albedinsky
 *
 * @deprecated Use simple base url instead.
 */
@Deprecated public interface EndPoint {

	/**
	 * Returns the base url of this end point.
	 *
	 * @return This end point's url.
	 */
	@NonNull String getBaseUrl();
}