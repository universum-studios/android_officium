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
package universum.studios.android.test.local;

import android.app.Application;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Class that may be used to group <b>suite of Android tests</b> to be executed on a local <i>JVM</i>
 * with shadowed <i>Android environment</i> using {@link RobolectricTestRunner}.
 *
 * @author Martin Albedinsky
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public abstract class RobolectricTestCase extends LocalTestCase {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "RobolectricTestCase";

	/**
	 * Application instance accessible via {@link RuntimeEnvironment#application}.
	 * <p>
	 * It is always valid between calls to {@link #beforeTest()} and {@link #afterTest()}.
	 */
	@NonNull
	protected Application mApplication;

	/**
	 */
	@Override
	@CallSuper
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mApplication = RuntimeEnvironment.application;
	}

	/**
	 */
	@Override
	@CallSuper
	public void afterTest() throws Exception {
		super.afterTest();
		this.mApplication = null;
	}
}
