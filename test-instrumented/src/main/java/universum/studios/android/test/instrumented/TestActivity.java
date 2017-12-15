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
package universum.studios.android.test.instrumented;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.widget.FrameLayout;

/**
 * Simple activity that may be used in <b>Android instrumented tests</b> in order to set up
 * {@link ActivityTestRule}.
 *
 * @author Martin Albedinsky
 */
public class TestActivity extends Activity {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "TestActivity";

	/**
	 * Id of the TestActivity's content view.
	 */
	public static final int CONTENT_VIEW_ID = android.R.id.custom;

	/**
	 */
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final FrameLayout contentView = new FrameLayout(this);
		contentView.setId(CONTENT_VIEW_ID);
		setContentView(contentView);
	}
}
