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
package universum.studios.android.officium.sync; 
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SyncExtrasTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "SyncExtrasTest";

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		SyncExtras.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<SyncExtras> constructor = SyncExtras.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

    @Test
	public void testConstants() {
		assertThat(SyncExtras.EXTRA_TASK_ID, is(SyncExtras.class.getPackage().getName() + ".EXTRA.Task.Id"));
		assertThat(SyncExtras.EXTRA_TASK_STATE, is(SyncExtras.class.getPackage().getName() + ".EXTRA.Task.State"));
		assertThat(SyncExtras.EXTRA_TASK_REQUEST_BODY, is(SyncExtras.class.getPackage().getName() + ".EXTRA.Task.RequestBody"));
    }
}
