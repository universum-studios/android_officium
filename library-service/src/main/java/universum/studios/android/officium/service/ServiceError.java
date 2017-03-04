/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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
 */
public class ServiceError extends BaseServiceObject {

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ServiceError";

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Throwable failure specified for this service error. May be {@code null} if this error is not
	 * a failure.
	 *
	 * @see #isFailure()
	 */
	private final Throwable mFailure;

	/**
	 * Error code specified for this service error along with error body.
	 *
	 * @see #isFailure()
	 */
	private final int mErrorCode;

	/**
	 * Error body specified for this service error along with error code. May be {@code null} if this
	 * error represents a failure.
	 *
	 * @see #isFailure()
	 */
	private final ResponseBody mErrorBody;

	/**
	 * Converter used to convert error body to the desired object.
	 *
	 * @see #getErrorBodyAs(Class)
	 */
	private Converter<ResponseBody, ?> mErrorBodyConverter;

	/**
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
	public ServiceError(int errorCode, @NonNull ResponseBody errorBody) {
		this.mErrorCode = errorCode;
		this.mErrorBody = errorBody;
		this.mFailure = null;
	}

	/**
	 * Creates a new instance of ServiceError with the specified <var>failure</var>.
	 *
	 * @param failure The failure that has caused the associated service and request to fail.
	 * @see #isFailure()
	 * @see #getFailure()
	 */
	public ServiceError(@NonNull Throwable failure) {
		this.mErrorCode = 0;
		this.mErrorBody = null;
		this.mFailure = failure;
	}

	/**
	 * Creates a new instance of ServiceError with data of the given one.
	 * <p>
	 * <b>Note</b>, that this is same as creating <b>shallow</b> copy of the error object.
	 *
	 * @param other The other service error of which data to copy to the new one.
	 */
	public ServiceError(@NonNull ServiceError other) {
		this.mErrorCode = other.mErrorCode;
		this.mErrorBody = other.mErrorBody;
		this.mFailure = other.mFailure;
		this.mErrorBodyConverter = other.mErrorBodyConverter;
	}

	/**
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
		return mErrorBody != null;
	}

	/**
	 * Returns the error code of the response send by the server as result to call to the associated
	 * service and request.
	 *
	 * @return Error code specified for this service error.
	 * @throws IllegalStateException If this error is not an error response but a failure.
	 * @see #getErrorBody()
	 */
	public int getErrorCode() {
		if (mErrorBody == null) throw new IllegalStateException("Not an error but a failure!");
		return mErrorCode;
	}

	/**
	 * Returns the error body of the response send by the server as result to call to the associated
	 * service and request.
	 *
	 * @return Error body specified for this service error.
	 * @throws IllegalStateException If this error is not an error response but a failure.
	 * @see #getErrorCode()
	 */
	@NonNull
	public ResponseBody getErrorBody() {
		if (mErrorBody == null) throw new IllegalStateException("Not an error but a failure!");
		return mErrorBody;
	}

	/**
	 * Sets a converter that should be used to covert error body of this service error to the desired
	 * object when {@link #getErrorBodyAs(Class)} is called.
	 *
	 * @param converter The desired converter to be used for conversion. May be {@code null} to clear
	 *                  the current one.
	 */
	public void setErrorBodyConverter(@Nullable Converter<ResponseBody, ?> converter) {
		this.mErrorBodyConverter = converter;
	}

	/**
	 * Returns the error body of this service error as the desired type.
	 *
	 * @param classOfT Class ot the type to which to convert the error body.
	 * @param <T>      The desired type as which to return error body or {@code null} if conversion fails.
	 * @return Error body converted to the requested type.
	 * @throws IllegalStateException If this error is not an error response but a failure.
	 * @throws IllegalStateException If no converter has been specified.
	 */
	@Nullable
	public <T> T getErrorBodyAs(@NonNull Class<T> classOfT) {
		if (mErrorBody == null) throw new IllegalStateException("Not an error but a failure!");
		if (mErrorBodyConverter == null) throw new IllegalStateException("No error body converter specified!");
		T errorBody = null;
		try {
			errorBody = (T) mErrorBodyConverter.convert(mErrorBody);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Specified class(" + classOfT + ") does not match that specified for converter!", e);
		} finally {
			mErrorBody.close();
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
		return mFailure != null;
	}

	/**
	 * Returns the failure that has caused the associated service and request to fail.
	 *
	 * @return Throwable failure specified for this service error.
	 * @throws IllegalStateException If this error is not a failure but an error response.
	 */
	@NonNull
	public Throwable getFailure() {
		if (mFailure == null) throw new IllegalStateException("Not a failure but an error!");
		return mFailure;
	}

	/**
	 */
	@Override
	@SuppressWarnings("StringBufferReplaceableByString")
	public String toString() {
		final boolean isFailure = isFailure();
		final StringBuilder builder = new StringBuilder(64);
		builder.append(getClass().getSimpleName());
		builder.append("{isFailure: ");
		builder.append(isFailure);
		builder.append(", errorCode: ");
		builder.append(isFailure ? "NONE" : mErrorCode);
		builder.append(", errorBody: ");
		builder.append(isFailure ? "NONE" : mErrorBody);
		builder.append(", failure: ");
		builder.append(isFailure ? mFailure : "NONE");
		return builder.append("}").toString();
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
