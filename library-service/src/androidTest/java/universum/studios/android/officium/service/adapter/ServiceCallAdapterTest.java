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
package universum.studios.android.officium.service.adapter;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Call;
import universum.studios.android.officium.service.ServiceCall;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceCallAdapterTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "ServiceCallAdapterTest";

	@Test
	public void testInstantiation() {
		assertEquals(new ServiceCallAdapter<>(String.class).responseType(), String.class);
	}

	@Test
	public void testAdapt() {
		final ServiceCall<String> serviceCall = new ServiceCallAdapter<String>(String.class).adapt(mock(TestCall.class));
		assertThat(serviceCall, is(notNullValue()));
		assertThat(serviceCall.isExecuted(), is(false));
		assertThat(serviceCall.isCanceled(), is(false));
	}

	private static abstract class TestCall implements Call<String> {

		@Override
		@SuppressWarnings("MethodDoesntCallSuperMethod")
		public Call<String> clone() {
			return mock(TestCall.class);
		}
	}
}
