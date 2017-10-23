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
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ErrorResponseTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ErrorResponseTest";

    @Test
	public void testInstantiationViaGson() {
		final ErrorResponse response = new Gson().fromJson("{error: {code: 400, message: \"Unauthorized.\"}}", ErrorResponse.class);
	    assertThat(response, is(notNullValue()));
	    assertThat(response.getErrorCode(), is(400));
	    assertThat(response.getErrorMessage(), is("Unauthorized."));
	}

	@Test
	public void testInstantiationWithError() {
		final ErrorResponse.Error error = new ErrorResponse.Error();
		error.code = 400;
		error.message = "Unauthorized.";
		final ErrorResponse response = new ErrorResponse(error);
		assertThat(response.getErrorCode(), is(error.code));
		assertThat(response.getErrorMessage(), is(error.message));
	}

	@Test
	public void testToString() {
		final ErrorResponse.Error error = new ErrorResponse.Error();
		error.code = 400;
		error.message = "Unauthorized.";
		assertThat(
				new ErrorResponse(error).toString(),
				is("ErrorResponse{error: Error{code: 400, message: Unauthorized.}}")
		);
	}

	@Test
	public void testGetErrorCodeWithoutErrorPresented() {
		assertThat(new ErrorResponse().getErrorCode(), is(0));
	}

	@Test
	public void testGetErrorMessageWithoutErrorPresented() {
		assertThat(new ErrorResponse().getErrorMessage(), is(""));
	}
}
