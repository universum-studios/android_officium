/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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
package universum.studios.android.officium.service.adapter;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import universum.studios.android.officium.service.ServiceCall;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Albedinsky
 */
public final class ServiceCallAdapterFactoryTest extends LocalTestCase {

	private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

	private Retrofit retrofit;

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		this.retrofit = new Retrofit.Builder().baseUrl("https://www.google.com/").build();
	}

	@Test public void testCreate() {
		// Act:
		final ServiceCallAdapterFactory factory = ServiceCallAdapterFactory.create();
		// Assert:
		assertThat(factory, is(notNullValue()));
		assertThat(factory, is(not(ServiceCallAdapterFactory.create())));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testGetForParametrizedServiceCallType() {
		// Arrange:
		final ServiceCallAdapterFactory factory = ServiceCallAdapterFactory.create();
		// Act:
		final CallAdapter<?, ?> adapter = factory.get(
				obtainMethodReturnType(TestServices.class, "parametrizedServiceCall"),
				EMPTY_ANNOTATIONS,
				retrofit
		);
		// Assert:
		assertThat(adapter, is(notNullValue()));
		assertEquals(adapter.responseType(), String.class);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetForNotParametrizedServiceCallType() {
		// Arrange:
		final ServiceCallAdapterFactory factory = ServiceCallAdapterFactory.create();
		// Act:
		final CallAdapter<?, ?> adapter = factory.get(
				obtainMethodReturnType(TestServices.class, "notParametrizedServiceCall"),
				EMPTY_ANNOTATIONS,
				retrofit
		);
		// Assert:
		assertThat(adapter, is(nullValue()));
	}

	@Test public void testGetForNotServiceCallType() {
		// Arrange:
		final ServiceCallAdapterFactory factory = ServiceCallAdapterFactory.create();
		// Act:
		final CallAdapter<?, ?> adapter = factory.get(
				obtainMethodReturnType(TestServices.class, "notServiceCall"),
				EMPTY_ANNOTATIONS,
				retrofit
		);
		// Assert:
		assertThat(adapter, is(nullValue()));
	}

	@SuppressWarnings("unchecked")
	@NonNull private static Type obtainMethodReturnType(Class ownerClass, String methodName) {
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