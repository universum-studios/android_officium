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

import com.google.gson.Gson;

import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * @author Martin Albedinsky
 */
public final class ServiceErrorTest extends LocalTestCase {

	@Test public void testInstantiationAsFailure() {
		// Arrange:
		final Throwable failure = new IOException("Failure.");
		// Act:
		final ServiceError error = new ServiceError(failure);
		// Assert:
		assertThat(error.isFailure(), is(true));
		assertThat(error.isError(), is(false));
		assertThat(error.getFailure(), is(failure));
	}

	@Test public void testInstantiationAsError() {
		// Arrange:
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse(""), "");
		// Act:
		final ServiceError error = new ServiceError(400, errorBody);
		// Assert:
		assertThat(error.isError(), is(true));
		assertThat(error.isFailure(), is(false));
		assertThat(error.getErrorCode(), is(400));
		assertThat(error.getErrorBody(), is(errorBody));
	}

	@Test public void testInstantiationAsFailureFromAnotherError() {
		// Arrange:
		final Throwable failure = new IOException("Failure.");
		final ServiceError error = new ServiceError(failure);
		// Act:
		final ServiceError secondError = new ServiceError(error);
		// Assert:
		assertThat(secondError.getFailure(), is(error.getFailure()));
	}

	@Test public void testInstantiationAsErrorFromAnotherError() {
		// Arrange:
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse(""), "");
		final ServiceError error = new ServiceError(400, errorBody);
		// Act:
		final ServiceError secondError = new ServiceError(error);
		// Assert:
		assertThat(secondError.getErrorCode(), is(error.getErrorCode()));
		assertThat(secondError.getErrorBody(), is(error.getErrorBody()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testGetErrorBodyAs() {
		// Arrange:
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse("application/json"), "{code: 401, message: \"Unauthorized.\"}");
		final ServiceError error = new ServiceError(400, errorBody);
		error.setErrorBodyConverter(new Converter<ResponseBody, TestError>() {

			@Override public TestError convert(@NonNull final ResponseBody value) throws IOException {
				return new Gson().fromJson(value.string(), TestError.class);
			}
		});
		// Act:
		final TestError testError = error.getErrorBodyAs(TestError.class);
		// Assert:
		assertThat(testError, is(notNullValue()));
		assertThat(testError.code, is(401));
		assertThat(testError.message, is("Unauthorized."));
	}

	@Test public void testGetErrorBodyAsWhereConverterThrowsIOException() {
		// Arrange:
		final ResponseBody errorBody = ResponseBody.create(MediaType.parse("application/json"), "{code: 401, message: \"Unauthorized.\"}");
		final ServiceError error = new ServiceError(400, errorBody);
		error.setErrorBodyConverter(new Converter<ResponseBody, TestError>() {

			@Override public TestError convert(@NonNull final ResponseBody value) throws IOException {
				throw new IOException();
			}
		});
		// Act:
		final TestError testError = error.getErrorBodyAs(TestError.class);
		// Assert:
		assertThat(testError, is(nullValue()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetErrorBodyAsWithoutConverter() {
		// Arrange:
		final ServiceError error = new ServiceError(400, ResponseBody.create(MediaType.parse(""), ""));
		// Act:
		error.getErrorBodyAs(TestError.class);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetErrorBodyAsWhenNotError() {
		// Arrange:
		final ServiceError error = new ServiceError(new IOException("Failure."));
		// Act:
		error.getErrorBodyAs(TestError.class);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetErrorCodeWhenNotError() {
		// Arrange:
		final ServiceError error = new ServiceError(new IOException("Failure."));
		// Act:
		error.getErrorCode();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetErrorBodyWhenNotError() {
		// Arrange:
		final ServiceError error = new ServiceError(new IOException("Failure."));
		// Act:
		error.getErrorBody();
	}

	@Test(expected = UnsupportedOperationException.class)
	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	public void testGetFailureWhenNotFailure() {
		// Arrange:
		final ServiceError error = new ServiceError(400, ResponseBody.create(MediaType.parse(""), ""));
		// Act:
		error.getFailure();
	}

	@Test public void testToStringForErrorThatIsFailure() {
		// Arrange:
		final ServiceError error = new ServiceError(new IOException("Failure."));
		// Act:
		final String stringValue = error.toString();
		// Arrange:
		assertThat(stringValue, is("ServiceError{isFailure: true, errorCode: NONE, errorBody: NONE, failure: java.io.IOException: Failure.}"));
	}

	@Test public void testToStringForErrorThatIsNotFailure() {
		// Arrange:
		final ServiceError error = new ServiceError(400, ResponseBody.create(MediaType.parse(""), ""));
		// Act:
		final String stringValue = error.toString();
		// Arrange:
		assertThat(stringValue, startsWith("ServiceError{isFailure: false, errorCode: 400, errorBody: "));
	}

	public static final class TestError {

		int code;
		String message;
	}
}