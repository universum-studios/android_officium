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
package universum.studios.android.officium.service;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceCallTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ServiceCallTest";

    @Test
	public void testWithServiceId() {
	    assertThat(new ServiceCall<>(mock(TestCall.class)).withServiceId(1).mServiceId, is(1));
	}

    @Test(expected = UnsupportedOperationException.class)
	public void testWithServiceIdWhenAlreadyCalled() {
	    new ServiceCall<>(mock(TestCall.class)).withServiceId(1).withServiceId(2);
	}

	@Test
	public void testExecute() throws Exception {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		call.execute();
		verify(mockCall, times(1)).execute();
	}

	@Test
	public void testEnqueueWithServiceCallback() {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCallback<Void> callback = new TestServiceCallback() {

			@Override
			protected void onDispatchResponse(@NonNull Void responseBody) {
			}

			@Override
			protected void onDispatchError(@NonNull ServiceError error) {
			}
		};
		final ServiceCall<Void> call = new ServiceCall<>(mockCall).withServiceId(1);
		assertThat(call.enqueue(callback), is(notNullValue()));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is(not(ServiceCallback.NO_REQUEST)));
		verify(mockCall, times(1)).enqueue(callback);
	}

	@Test
	public void testNextRequestId() throws Exception {
		final ServiceCall<Void> call = new ServiceCall<>(mock(TestCall.class));
		final Set<String> requestIds = new HashSet<>(20);
		for (int i = 0; i < 20; i++) {
			final String requestId = call.nextRequestId();
			assertThat(requestIds.contains(requestId), is(false));
			requestIds.add(requestId);
			// Ensure that we will not hit the same execution time.
			Thread.sleep(1);
		}
	}

	@Test
	public void testEnqueueWithCallback() {
		final Call<Void> mockCall = mock(TestCall.class);
		final Callback<Void> mockCallback = mock(TestCallback.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall).withServiceId(1);
		call.enqueue(mockCallback);
		verifyZeroInteractions(mockCallback);
		verify(mockCall, times(1)).enqueue(mockCallback);
	}

	@Test
	public void testIsExecuted() {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		call.isExecuted();
		verify(mockCall, times(1)).isExecuted();
		verifyNoMoreInteractions(mockCall);
	}

	@Test
	public void testCancel() {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		call.cancel();
		verify(mockCall, times(1)).cancel();
		verifyNoMoreInteractions(mockCall);
	}

	@Test
	public void testIsCanceled() {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		call.isCanceled();
		verify(mockCall, times(1)).isCanceled();
		verifyNoMoreInteractions(mockCall);
	}

	@Test
	public void testRequest() {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall);
		call.request();
		verify(mockCall, times(1)).request();
		verifyNoMoreInteractions(mockCall);
	}

	@Test
	public void testClone() {
		final Call<Void> mockCall = mock(TestCall.class);
		final ServiceCall<Void> call = new ServiceCall<>(mockCall).withServiceId(1);
		final ServiceCall<Void> callClone = call.clone();
		assertThat(callClone, is(notNullValue()));
		assertThat(callClone.mServiceId, is(1));
		assertThat(callClone.mCall, is(not(mockCall)));
	}

	private interface TestCall extends Call<Void> {
	}

	private abstract class TestServiceCallback extends ServiceCallback<Void> {
	}

	private interface TestCallback extends Callback<Void> {
	}
}
