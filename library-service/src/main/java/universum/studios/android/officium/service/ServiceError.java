/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
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
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * A {@link BaseServiceObject} implementation that represents an error occurred during service execution.
 * Service error may be defined as error response send by the server where in such case the error/response
 * code may be obtained via {@link #getErrorCode()} along with the error body via {@link #getErrorBody()}
 * respectively. Or it may be defined as a failure that has caused the associated service and request
 * to fail. In such case {@link #isFailure()} will return {@code true} and the occurred failure may
 * be obtained via {@link #getFailure()}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class ServiceError extends BaseServiceObject {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceError";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Throwable failure specified for this service error. May be {@code null} if this error is not
	 * a failure.
	 *
	 * @see #isFailure()
	 */
	private final Throwable failure;

	/**
	 * Error code specified for this service error along with error body.
	 *
	 * @see #isFailure()
	 */
	private final int errorCode;

	/**
	 * Error body specified for this service error along with error code. May be {@code null} if this
	 * error represents a failure.
	 *
	 * @see #isFailure()
	 */
	private final ResponseBody errorBody;

	/**
	 * Converter used to convert error body to the desired object.
	 *
	 * @see #getErrorBodyAs(Class)
	 */
	private Converter<ResponseBody, ?> errorBodyConverter;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ServiceError with the specified <var>errorCode</var> and <var>errorBody</var>
	 * received from the server as error response for the associated service and request.
	 *
	 * @param errorCode The error/response code returned by the server.
	 * @param errorBody The body of error response returned by the server.
	 * @see #getErrorCode()
	 * @see #getErrorBody()
	 */
	public ServiceError(final int errorCode, @NonNull final ResponseBody errorBody) {
		super();
		this.errorCode = errorCode;
		this.errorBody = errorBody;
		this.failure = null;
	}

	/**
	 * Creates a new instance of ServiceError with the specified <var>failure</var>.
	 *
	 * @param failure The failure that has caused the associated service and request to fail.
	 * @see #isFailure()
	 * @see #getFailure()
	 */
	public ServiceError(@NonNull final Throwable failure) {
		super();
		this.errorCode = 0;
		this.errorBody = null;
		this.failure = failure;
	}

	/**
	 * Creates a new instance of ServiceError with data of the given one.
	 * <p>
	 * <b>Note</b>, that this is same as creating <b>shallow</b> copy of the error object.
	 *
	 * @param other The other service error of which data to copy to the new one.
	 */
	public ServiceError(@NonNull final ServiceError other) {
		super();
		this.errorCode = other.errorCode;
		this.errorBody = other.errorBody;
		this.failure = other.failure;
		this.errorBodyConverter = other.errorBodyConverter;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Checks whether this service error represents an error response that has been received from
	 * the server for the associated service and request.
	 *
	 * @return {@code True} if this error is an error response send by the server where the response
	 * code may be obtained via {@link #getErrorCode()} and the error body via {@link #getErrorBody()}
	 * respectively.
	 * @see #isFailure()
	 */
	public final boolean isError() {
		return errorBody != null;
	}

	/**
	 * Returns the error code of the response send by the server as result to call to the associated
	 * service and request.
	 *
	 * @return Error code specified for this service error.
	 * @throws UnsupportedOperationException If this error is not an error response but a failure.
	 * @see #getErrorBody()
	 */
	public int getErrorCode() {
		if (errorBody == null)
			throw new UnsupportedOperationException("Not an error but a failure!");
		return errorCode;
	}

	/**
	 * Returns the error body of the response send by the server as result to call to the associated
	 * service and request.
	 *
	 * @return Error body specified for this service error.
	 * @throws UnsupportedOperationException If this error is not an error response but a failure.
	 * @see #getErrorCode()
	 */
	@NonNull public ResponseBody getErrorBody() {
		if (errorBody == null)
			throw new UnsupportedOperationException("Not an error but a failure!");
		return errorBody;
	}

	/**
	 * Sets a converter that should be used to covert error body of this service error to the desired
	 * object when {@link #getErrorBodyAs(Class)} is called.
	 *
	 * @param converter The desired converter to be used for conversion. May be {@code null} to clear
	 *                  the current one.
	 */
	public void setErrorBodyConverter(@Nullable final Converter<ResponseBody, ?> converter) {
		this.errorBodyConverter = converter;
	}

	/**
	 * Returns the error body of this service error as the desired type.
	 *
	 * @param classOfT Class ot the type to which to convert the error body.
	 * @param <T>      The desired type as which to return error body or {@code null} if conversion fails.
	 * @return Error body converted to the requested type.
	 * @throws UnsupportedOperationException If this error is not an error response but a failure.
	 * @throws IllegalStateException         If no converter has been specified.
	 */
	@SuppressWarnings("unchecked")
	@Nullable public <T> T getErrorBodyAs(@NonNull final Class<T> classOfT) {
		if (errorBody == null)
			throw new UnsupportedOperationException("Not an error but a failure!");
		if (errorBodyConverter == null)
			throw new IllegalStateException("No error body converter specified!");
		T errorBody = null;
		try {
			errorBody = (T) errorBodyConverter.convert(this.errorBody);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Specified class(" + classOfT + ") does not match that specified for converter!", e);
		} finally {
			this.errorBody.close();
		}
		return errorBody;
	}

	/**
	 * Checks whether this service error represents a failure that has caused the associated service
	 * and request to fail during its execution.
	 *
	 * @return {@code True} if this error is a failure that may be obtained via {@link #getFailure()},
	 * {@code false} if it is an error response.
	 * @see #isError()
	 */
	public final boolean isFailure() {
		return failure != null;
	}

	/**
	 * Returns the failure that has caused the associated service and request to fail.
	 *
	 * @return Throwable failure specified for this service error.
	 * @throws UnsupportedOperationException If this error is not a failure but an error response.
	 */
	@NonNull public Throwable getFailure() {
		if (failure == null)
			throw new UnsupportedOperationException("Not a failure but an error!");
		return failure;
	}

	/**
	 */
	@SuppressWarnings("StringBufferReplaceableByString")
	@Override public String toString() {
		final boolean isFailure = isFailure();
		final StringBuilder builder = new StringBuilder(64);
		builder.append(getClass().getSimpleName());
		builder.append("{isFailure: ");
		builder.append(isFailure);
		builder.append(", errorCode: ");
		builder.append(isFailure ? "NONE" : errorCode);
		builder.append(", errorBody: ");
		builder.append(isFailure ? "NONE" : errorBody);
		builder.append(", failure: ");
		builder.append(isFailure ? failure : "NONE");
		return builder.append("}").toString();
	}

	/*
	 * Inner classes ===============================================================================
	 */
}