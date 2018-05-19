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

import android.support.annotation.NonNull;

import universum.studios.android.samples.officium.service.request.ApiSignInRequest;
import universum.studios.android.samples.officium.service.response.ApiSignInResponse;
import universum.studios.android.officium.service.EndPoint;
import universum.studios.android.officium.sync.SyncTask;

/**
 * @author Martin Albedinsky
 */
public interface Api {

	/*
	 * CONFIGURATION ===============================================================================
	 */

	EndPoint END_POINT = ApiEndPoint.get();

	/*
	 * IDS OF PROVIDED SYNCHRONIZATION SERVICES ====================================================
	 */

	int SYNCHRONIZE_GLOBAL      = SyncTask.DEFAULT_ID;
	int SYNCHRONIZE_MESSAGES    = 0x0001;

	/*
	 * IDS OF PROVIDED SERVICES ====================================================================
	 */

	int SIGN_UP                = 0x1001;
	int SIGN_IN                = 0x1002;
	int FORGOT_PASSWORD        = 0x1003;

	/*
	 * PROVIDED SERVICES ===========================================================================
	 */

	@NonNull
	ApiCall<ApiSignInResponse> signIn(@NonNull ApiSignInRequest request);
}