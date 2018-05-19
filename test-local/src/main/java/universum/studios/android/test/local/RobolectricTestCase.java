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
	 * Application instance accessible via {@link RuntimeEnvironment#application}.
	 * <p>
	 * It is always valid between calls to {@link #beforeTest()} and {@link #afterTest()}.
	 */
	@NonNull protected Application application;

	/**
	 */
	@Override @CallSuper public void beforeTest() throws Exception {
		super.beforeTest();
		this.application = RuntimeEnvironment.application;
	}

	/**
	 */
	@Override @CallSuper public void afterTest() throws Exception {
		super.afterTest();
		this.application = null;
	}
}