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

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import universum.studios.android.officium.service.ServiceCall;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceCallAdapterFactoryTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "ServiceCallAdapterFactoryTest";
	private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

	private Retrofit mRetrofit;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mRetrofit = new Retrofit.Builder().baseUrl("https://www.google.com/").build();
	}

	@Test
	public void testCreate() {
		final ServiceCallAdapterFactory factory = ServiceCallAdapterFactory.create();
		assertThat(factory, is(notNullValue()));
		assertThat(factory, is(not(ServiceCallAdapterFactory.create())));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testGetForParametrizedServiceCallType() {
		final CallAdapter<?, ?> adapter = ServiceCallAdapterFactory.create().get(
				obtainMethodReturnType(TestServices.class, "parametrizedServiceCall"),
				EMPTY_ANNOTATIONS,
				mRetrofit
		);
		assertThat(adapter, is(notNullValue()));
		assertEquals(adapter.responseType(), String.class);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetForNotParametrizedServiceCallType() {
		assertThat(ServiceCallAdapterFactory.create().get(
				obtainMethodReturnType(TestServices.class, "notParametrizedServiceCall"),
				EMPTY_ANNOTATIONS,
				mRetrofit
		), is(nullValue()));
	}

	@Test
	public void testGetForNotServiceCallType() {
		assertThat(ServiceCallAdapterFactory.create().get(
				obtainMethodReturnType(TestServices.class, "notServiceCall"),
				EMPTY_ANNOTATIONS,
				mRetrofit
		), is(nullValue()));
	}

	@NonNull
	@SuppressWarnings("unchecked")
	private static Type obtainMethodReturnType(Class ownerClass, String methodName) {
		try {
			return ownerClass.getMethod(methodName).getGenericReturnType();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException("Failed to obtain method's return type!");
	}

	@SuppressWarnings("unused")
	interface TestServices {

		ServiceCall<String> parametrizedServiceCall();

		ServiceCall notParametrizedServiceCall();

		Call notServiceCall();
	}
}
