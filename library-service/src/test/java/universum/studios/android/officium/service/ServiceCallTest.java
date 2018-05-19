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
package universum.studios.android.officium.service;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ServiceCallTest extends LocalTestCase {

	@Test public void testWithServiceId() {
		// Arrange:
		final ServiceCall call = new ServiceCall<>(mock(TestCall.class));
		// Act + Assert:
		assertThat(call.withServiceId(1), is(call));
		assertThat(call.serviceId, is(1));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testWithServiceIdWhenAlreadyCalled() {
		// Arrange:
		final ServiceCall call = new ServiceCall<>(mock(TestCall.class));
		call.withServiceId(1);
		// Act + Assert:
		assertThat(call.withServiceId(2), is(call));
	}

	@Test public void testExecute() throws Exception {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		// Act:
		call.execute();
		// Assert:
		verify(mockCall).execute();
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testEnqueueWithServiceCallback() {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCallback<Void> callback = new TestServiceCallback() {

			@Override protected void onDispatchResponse(@NonNull Void responseBody) {}

			@Override protected void onDispatchError(@NonNull ServiceError error) {}
		};
		final ServiceCall<Void> call = new ServiceCall<>(mockCall).withServiceId(1);
		// Act + Assert:
		assertThat(call.enqueue(callback), is(notNullValue()));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is(not(ServiceCallback.NO_REQUEST)));
		verify(mockCall).enqueue(callback);
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testNextRequestId() throws Exception {
		// Arrange:
		final ServiceCall<Void> call = new ServiceCall<>(mock(TestCall.class));
		final Set<String> requestIds = new HashSet<>(20);
		// Act + Assert:
		for (int i = 0; i < 20; i++) {
			final String requestId = call.nextRequestId();
			assertThat(requestIds.contains(requestId), is(false));
			requestIds.add(requestId);
			// Ensure that we will not hit the same execution time.
			Thread.sleep(1);
		}
	}

	@Test public void testEnqueueWithCallback() {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		final Callback<Void> mockCallback = mock(TestCallback.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall).withServiceId(1);
		// Act:
		call.enqueue(mockCallback);
		// Assert:
		verifyZeroInteractions(mockCallback);
		verify(mockCall).enqueue(mockCallback);
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testIsExecuted() {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		when(mockCall.isExecuted()).thenReturn(true);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		// Act:
		final boolean result = call.isExecuted();
		// Assert:
		assertThat(result, is(true));
		verify(mockCall).isExecuted();
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testCancel() {
		// Arrange + Act + Assert:
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		call.cancel();
		verify(mockCall, times(1)).cancel();
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testIsCanceled() {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		when(mockCall.isCanceled()).thenReturn(true);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		// Act:
		final boolean result = call.isCanceled();
		// Assert:
		assertThat(result, is(true));
		verify(mockCall).isCanceled();
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testRequest() {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		final Request request = new Request.Builder().url("http://www.google.com").build();
		when(mockCall.request()).thenReturn(request);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		// Act:
		final Request callRequest = call.request();
		// Assert:
		assertThat(callRequest, is(request));
		verify(mockCall).request();
		verifyNoMoreInteractions(mockCall);
	}

	@Test public void testClone() {
		// Arrange:
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall).withServiceId(1);
		// Act:
		final ServiceCall<Void> callClone = call.clone();
		// Assert:
		assertThat(callClone, is(notNullValue()));
		assertThat(callClone.serviceId, is(1));
		assertThat(callClone.call, is(not(mockCall)));
	}

	private interface TestCall extends Call<Void> {}

	private abstract class TestServiceCallback extends ServiceCallback<Void> {}

	private interface TestCallback extends Callback<Void> {}
}