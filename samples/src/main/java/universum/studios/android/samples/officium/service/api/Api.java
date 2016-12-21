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
package universum.studios.android.samples.officium.service.api;

import android.support.annotation.NonNull;

import universum.studios.android.samples.officium.service.request.SignInRequest;
import universum.studios.android.samples.officium.service.response.SignInResponse;
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
	ApiCall<SignInResponse> signIn(@NonNull SignInRequest request);
}
