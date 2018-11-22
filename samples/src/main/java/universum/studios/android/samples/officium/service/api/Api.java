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
package universum.studios.android.samples.officium.service.api;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import universum.studios.android.samples.officium.service.api.request.ApiSignInRequest;
import universum.studios.android.samples.officium.service.api.response.ApiSignInResponse;

/**
 * @author Martin Albedinsky
 */
public interface Api {

	/*
	 * CONFIGURATION ===============================================================================
	 */

	HttpUrl BASE_URL = HttpUrl.parse("https://www.android.com/api/v1/");

	/*
	 * IDS OF PROVIDED SERVICES ====================================================================
	 */

	int SIGN_IN                = 0x1001;

	/*
	 * PROVIDED SERVICES ===========================================================================
	 */

	@NonNull ApiCall<ApiSignInResponse> signIn(@NonNull ApiSignInRequest request);
}