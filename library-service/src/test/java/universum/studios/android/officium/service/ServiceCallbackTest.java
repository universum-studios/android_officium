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

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
public final class ServiceCallbackTest extends LocalTestCase {

	@Test public void testOnResponseThatIsSuccessful() {
		// Arrange:
		final TestCallback<TestServiceResult> callback = new TestCallback<>();
		callback.setServiceId(1);
		callback.setRequestId("request:1");
		final TestServiceResult responseBody = new TestServiceResult();
		final Response<TestServiceResult> response = Response.success(responseBody);
		// Act:
		callback.onResponse(mock(TestServiceCall.class), response);
		// Assert:
		assertThat(callback.onDispatchResponseCalled, is(true));
		assertThat(callback.onDispatchErrorCalled, is(false));
		assertThat(callback.onDispatchResponseBody, is(responseBody));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is("request:1"));
	}

	@Test public void testOnResponseThatIsSuccessfulWithBodyThatIsNotServiceObject() {
		// Arrange:
		final TestCallback<TestResult> callback = new TestCallback<>();
		callback.setServiceId(1);
		callback.setRequestId("request:1");
		final TestResult responseBody = new TestResult();
		final Response<TestResult> response = Response.success(responseBody);
		// Act:
		callback.onResponse(mock(TestCall.class), response);
		// Assert:
		assertThat(callback.onDispatchResponseCalled, is(true));
		assertThat(callback.onDispatchErrorCalled, is(false));
		assertThat(callback.onDispatchResponseBody, is(responseBody));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is("request:1"));
	}

	@Test public void testOnResponseThatIsError() {
		// Arrange:
		final TestCallback<TestResult> callback = new TestCallback<>();
		callback.setServiceId(1);
		callback.setRequestId("request:1");
		final Response<TestResult> errorResponse = Response.error(400, ResponseBody.create(MediaType.parse("json"), "{}"));
		// Act:
		callback.onResponse(mock(TestCall.class), errorResponse);
		// Assert:
		assertThat(callback.onDispatchResponseCalled, is(false));
		assertThat(callback.onDispatchErrorCalled, is(true));
		assertThat(callback.onDispatchError, is(notNullValue()));
		assertThat(callback.onDispatchError.isError(), is(true));
		assertThat(callback.onDispatchError.isFailure(), is(false));
		assertThat(callback.onDispatchError.getErrorCode(), is(400));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is("request:1"));
	}

	@Test public void testOnFailure() {
		// Arrange:
		final TestCallback<TestResult> callback = new TestCallback<>();
		callback.setServiceId(1);
		callback.setRequestId("request:1");
		final Throwable mockFailure = mock(Throwable.class);
		// Act:
		callback.onFailure(mock(TestCall.class), mockFailure);
		// Assert:
		assertThat(callback.onDispatchResponseCalled, is(false));
		assertThat(callback.onDispatchErrorCalled, is(true));
		assertThat(callback.onDispatchError, is(notNullValue()));
		assertThat(callback.onDispatchError.isError(), is(false));
		assertThat(callback.onDispatchError.isFailure(), is(true));
		assertThat(callback.onDispatchError.getFailure(), is(mockFailure));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is("request:1"));
	}

	private static final class TestResult {}

	private static final class TestServiceResult extends ServiceResponse {}

	private static abstract class TestCall implements Call<TestResult> {

		@SuppressWarnings("CloneDoesntCallSuperClone")
		@Override public Call<TestResult> clone() {
			return null;
		}
	}

	private static abstract class TestServiceCall implements Call<TestServiceResult> {

		@SuppressWarnings("CloneDoesntCallSuperClone")
		@Override public Call<TestServiceResult> clone() {
			return null;
		}
	}

	private static final class TestCallback<T> extends ServiceCallback<T> {

		boolean onDispatchResponseCalled, onDispatchErrorCalled;
		T onDispatchResponseBody;
		ServiceError onDispatchError;

		@Override protected void onDispatchResponse(@NonNull final T responseBody) {
			this.onDispatchResponseCalled = true;
			this.onDispatchResponseBody = responseBody;
		}

		@Override protected void onDispatchError(@NonNull final ServiceError error) {
			this.onDispatchErrorCalled = true;
			this.onDispatchError = error;
		}
	}
}
