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

import com.google.gson.Gson;

import org.junit.Test;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class ErrorResponseTest extends LocalTestCase {

	@Test public void testInstantiation() {
		// Act:
		final ErrorResponse response = new ErrorResponse();
		// Assert:
		assertThat(response.getErrorCode(), is(0));
		assertThat(response.getErrorMessage(), is(""));
	}

	@Test public void testInstantiationViaGson() {
		// Arrange:
		final String errorJson = "{error: {code: 400, message: \"Unauthorized.\"}}";
		// Act:
		final ErrorResponse response = new Gson().fromJson(errorJson, ErrorResponse.class);
		// Assert:
		assertThat(response, is(notNullValue()));
		assertThat(response.getErrorCode(), is(400));
		assertThat(response.getErrorMessage(), is("Unauthorized."));
	}

	@Test public void testInstantiationWithError() {
		// Arrange:
		final ErrorResponse.Error error = new ErrorResponse.Error();
		error.code = 400;
		error.message = "Unauthorized.";
		// Act:
		final ErrorResponse response = new ErrorResponse(error);
		// Assert:
		assertThat(response.getErrorCode(), is(error.code));
		assertThat(response.getErrorMessage(), is(error.message));
	}

	@Test public void testToString() {
		// Arrange:
		final ErrorResponse.Error error = new ErrorResponse.Error();
		error.code = 400;
		error.message = "Unauthorized.";
		// Act + Assert:
		assertThat(
				new ErrorResponse(error).toString(),
				is("ErrorResponse{error: Error{code: 400, message: Unauthorized.}}")
		);
	}
}