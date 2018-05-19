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
 * End point that may be specified for {@link ServiceManager} to provide base url for all its
 * related services.
 *
 * @author Martin Albedinsky
 */
// todo: deprecated this and rather allow to specify raw base url for service manager instead ...
public interface EndPoint {

	/**
	 * Returns the base url of this end point.
	 *
	 * @return This end point's url.
	 */
	@NonNull String getBaseUrl();
}