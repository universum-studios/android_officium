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

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceErrorTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ServiceErrorTest";

	@Test
	public void testInstantiationAsFailure() {
		final Throwable failure = new IOException("Failure.");
		final ServiceError error = new ServiceError(failure);
		assertThat(error.isFailure(), is(true));
		assertThat(error.isError(), is(false));
		assertThat(error.getFailure(), is(failure));
	}

	@Test
	public void testInstantiationAsError() {
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse(""), "");
		final ServiceError error = new ServiceError(400, errorBody);
		assertThat(error.isError(), is(true));
		assertThat(error.isFailure(), is(false));
		assertThat(error.getErrorCode(), is(400));
		assertThat(error.getErrorBody(), is(errorBody));
	}

	@Test
	public void testInstantiationAsFailureFromAnotherError() {
		final Throwable failure = new IOException("Failure.");
		final ServiceError error = new ServiceError(failure);
		final ServiceError secondError = new ServiceError(error);
		assertThat(secondError.getFailure(), is(error.getFailure()));
	}

	@Test
	public void testInstantiationAsErrorFromAnotherError() {
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse(""), "");
		final ServiceError error = new ServiceError(400, errorBody);
		final ServiceError secondError = new ServiceError(error);
		assertThat(secondError.getErrorCode(), is(error.getErrorCode()));
		assertThat(secondError.getErrorBody(), is(error.getErrorBody()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testGetErrorBodyAs() {
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse("application/json"), "{code: 401, message: \"Unauthorized.\"}");
		final ServiceError error = new ServiceError(400, errorBody);
		error.setErrorBodyConverter(new Converter<ResponseBody, TestError>() {

			@Override
			public TestError convert(@NonNull ResponseBody value) throws IOException {
				return new Gson().fromJson(value.string(), TestError.class);
			}
		});
		final TestError testError = error.getErrorBodyAs(TestError.class);
		assertThat(testError, is(notNullValue()));
		assertThat(testError.code, is(401));
		assertThat(testError.message, is("Unauthorized."));
	}

	@Test
	public void testGetErrorBodyAsWhereConverterThrowsIOException() {
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse("application/json"), "{code: 401, message: \"Unauthorized.\"}");
		final ServiceError error = new ServiceError(400, errorBody);
		error.setErrorBodyConverter(new Converter<ResponseBody, TestError>() {

			@Override
			public TestError convert(@NonNull ResponseBody value) throws IOException {
				throw new IOException();
			}
		});
		assertThat(error.getErrorBodyAs(TestError.class), is(nullValue()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetErrorBodyAsWithoutConverter() {
		new ServiceError(400, ResponseBody.create(MediaType.parse(""), "")).getErrorBodyAs(TestError.class);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetErrorBodyAsWhenNotError() {
		new ServiceError(new IOException("Failure.")).getErrorBodyAs(TestError.class);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetErrorCodeWhenNotError() {
		new ServiceError(new IOException("Failure.")).getErrorCode();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetErrorBodyWhenNotError() {
		new ServiceError(new IOException("Failure.")).getErrorBody();
	}

	@Test(expected = UnsupportedOperationException.class)
	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	public void testGetFailureWhenNotFailure() {
		new ServiceError(400, ResponseBody.create(MediaType.parse(""), "")).getFailure();
	}

    @Test
	public void testToStringForErrorThatIsFailure() {
		final ServiceError error = new ServiceError(new IOException("Failure."));
	    assertThat(
	    		error.toString(),
			    is("ServiceError{isFailure: true, errorCode: NONE, errorBody: NONE, failure: java.io.IOException: Failure.}")
	    );
	}

    @Test
	public void testToStringForErrorThatIsNotFailure() {
		final ServiceError error = new ServiceError(400, ResponseBody.create(MediaType.parse(""), ""));
	    assertThat(
			    error.toString(),
			    startsWith("ServiceError{isFailure: false, errorCode: 400, errorBody: ")
	    );
	}

	public static final class TestError {

		public int code;
		public String message;
	}

	public static final class SecondaryTestError {

		public int secondaryCode;
		public String secondaryMessage;
	}
}
