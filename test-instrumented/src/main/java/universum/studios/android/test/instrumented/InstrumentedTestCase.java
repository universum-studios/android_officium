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

import android.app.Instrumentation;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Class that may be used to group suite of <b>Android instrumented tests</b>.
 *
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public abstract class InstrumentedTestCase {

	/**
	 * Target context obtained from the {@link InstrumentationRegistry}.
	 * <p>
	 * It is always valid between calls to {@link #beforeTest()} and {@link #afterTest()}.
	 *
	 * @see InstrumentationRegistry#getTargetContext()
	 */
	@NonNull protected Context context;

	/**
	 * Called before execution of each test method starts.
	 */
	@Before @CallSuper public void beforeTest() throws Exception {
		// Inheritance hierarchies may for example acquire here resources needed for each test.
		this.context = InstrumentationRegistry.getTargetContext();
	}

	/**
	 * Called after execution of each test method finishes.
	 */
	@After @CallSuper public void afterTest() throws Exception {
		// Inheritance hierarchies may for example release here resources acquired in beforeTest() call.
		this.context = null;
	}

	/**
	 * Delegates to {@link Instrumentation#waitForIdleSync()}.
	 */
	@WorkerThread protected static void waitForIdleSync() {
		InstrumentationRegistry.getInstrumentation().waitForIdleSync();
	}
}