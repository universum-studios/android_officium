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

import android.support.annotation.CallSuper;

import org.junit.After;
import org.junit.Before;

/**
 * Class that may be used to group <b>suite of tests</b> to be executed on a local <i>JVM</i>.
 *
 * @author Martin Albedinsky
 */
public abstract class LocalTestCase {

	/**
	 * Called before execution of each test method starts.
	 */
	@Before @CallSuper public void beforeTest() throws Exception {
		// Inheritance hierarchies may for example acquire here resources needed for each test.
	}

	/**
	 * Called after execution of each test method finishes.
	 */
	@After @CallSuper public void afterTest() throws Exception {
		// Inheritance hierarchies may for example release here resources acquired in beforeTest() call.
	}
}