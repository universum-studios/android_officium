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
    
    @Test
	public void testOnResponseThatIsSuccessful() {
	    final TestCallback<TestServiceResult> callback = new TestCallback<>();
	    callback.setServiceId(1);
	    callback.setRequestId("request:1");
	    final TestServiceResult responseBody = new TestServiceResult();
	    final Response<TestServiceResult> response = Response.success(responseBody);
	    callback.onResponse(mock(TestServiceCall.class), response);
	    assertThat(callback.onDispatchResponseCalled, is(true));
	    assertThat(callback.onDispatchErrorCalled, is(false));
	    assertThat(callback.onDispatchResponseBody, is(responseBody));
	    assertThat(callback.getServiceId(), is(1));
	    assertThat(callback.getRequestId(), is("request:1"));
    }

    @Test
	public void testOnResponseThatIsSuccessfulWithBodyThatIsNotServiceObject() {
	    final TestCallback<TestResult> callback = new TestCallback<>();
	    callback.setServiceId(1);
	    callback.setRequestId("request:1");
	    final TestResult responseBody = new TestResult();
	    final Response<TestResult> response = Response.success(responseBody);
	    callback.onResponse(mock(TestCall.class), response);
	    assertThat(callback.onDispatchResponseCalled, is(true));
	    assertThat(callback.onDispatchErrorCalled, is(false));
	    assertThat(callback.onDispatchResponseBody, is(responseBody));
	    assertThat(callback.getServiceId(), is(1));
	    assertThat(callback.getRequestId(), is("request:1"));
    }

	@Test
	public void testOnResponseThatIsError() {
		final TestCallback<TestResult> callback = new TestCallback<>();
		callback.setServiceId(1);
		callback.setRequestId("request:1");
		final Response<TestResult> errorResponse = Response.error(400, ResponseBody.create(MediaType.parse("json"), "{}"));
		callback.onResponse(mock(TestCall.class), errorResponse);
		assertThat(callback.onDispatchResponseCalled, is(false));
		assertThat(callback.onDispatchErrorCalled, is(true));
		assertThat(callback.onDispatchError, is(notNullValue()));
		assertThat(callback.onDispatchError.isError(), is(true));
		assertThat(callback.onDispatchError.isFailure(), is(false));
		assertThat(callback.onDispatchError.getErrorCode(), is(400));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is("request:1"));
	}

	@Test
	public void testOnFailure() {
		final TestCallback<TestResult> callback = new TestCallback<>();
		callback.setServiceId(1);
		callback.setRequestId("request:1");
		final Throwable mockFailure = mock(Throwable.class);
		callback.onFailure(mock(TestCall.class), mockFailure);
		assertThat(callback.onDispatchResponseCalled, is(false));
		assertThat(callback.onDispatchErrorCalled, is(true));
		assertThat(callback.onDispatchError, is(notNullValue()));
		assertThat(callback.onDispatchError.isError(), is(false));
		assertThat(callback.onDispatchError.isFailure(), is(true));
		assertThat(callback.onDispatchError.getFailure(), is(mockFailure));
		assertThat(callback.getServiceId(), is(1));
		assertThat(callback.getRequestId(), is("request:1"));
	}

	private static final class TestResult {
	}

	private static final class TestServiceResult extends ServiceResponse {
	}

	private static abstract class TestCall implements Call<TestResult> {

		@Override
		@SuppressWarnings("CloneDoesntCallSuperClone")
		public Call<TestResult> clone() {
			return null;
		}
	}

	private static abstract class TestServiceCall implements Call<TestServiceResult> {

		@Override
		@SuppressWarnings("CloneDoesntCallSuperClone")
		public Call<TestServiceResult> clone() {
			return null;
		}
	}

	private static final class TestCallback<T> extends ServiceCallback<T> {

		boolean onDispatchResponseCalled, onDispatchErrorCalled;
		T onDispatchResponseBody;
		ServiceError onDispatchError;

		@Override
		protected void onDispatchResponse(@NonNull T responseBody) {
			this.onDispatchResponseCalled = true;
			this.onDispatchResponseBody = responseBody;
		}

		@Override
		protected void onDispatchError(@NonNull ServiceError error) {
			this.onDispatchErrorCalled = true;
			this.onDispatchError = error;
		}
	}
}

