/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
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
	 * Id of the TestActivity's content view.
	 */
	public static final int CONTENT_VIEW_ID = android.R.id.custom;

	/**
	 */
	@Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final FrameLayout contentView = new FrameLayout(this);
		contentView.setId(CONTENT_VIEW_ID);
		setContentView(contentView);
	}
}