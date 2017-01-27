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

/**
 * A {@link ServiceResponse} that may be used to parse body of response send by a server that
 * contains an {@link Error} object.
 *
 * <h3>Response body</h3>
 * <pre>
 * {
 *     "error": {
 *         "code": 486,
 *         "message": "Item not found on the server!"
 *     }
 * }
 * </pre>
 *
 * @author Martin Albedinsky
 */
public class ErrorResponse extends ServiceResponse {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ErrorResponse";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Error specified for this response during its initialization or parsed using <b>Gson</b>.
	 */
	public Error error;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ErrorResponse without error.
	 * <p>
	 * This constructor should be used only by <b>Gson</b> library.
	 */
	public ErrorResponse() {
	}

	/**
	 * Creates a new instance of ErrorResponse with the specified <var>error</var>.
	 *
	 * @param error The error for the new error response.
	 */
	public ErrorResponse(@NonNull Error error) {
		this.error = error;
	}

	/**
	 * Returns the code of the error (if any) of this response.
	 *
	 * @return Error code or {@code 0} if there is no error presented.
	 */
	public int getErrorCode() {
		return error != null ? error.code : 0;
	}

	/**
	 * Returns the message of the error (if any) of this response.
	 *
	 * @return Error message or {@code ""} if there is no error presented.
	 */
	@NonNull
	public String getErrorMessage() {
		return error != null ? error.message : "";
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override
	@SuppressWarnings("StringBufferReplaceableByString")
	public String toString() {
		final StringBuilder builder = new StringBuilder(64);
		builder.append(getClass().getSimpleName());
		builder.append("{error: ");
		builder.append(error);
		return builder.append("}").toString();
	}

	/**
	 * Inner classes ===============================================================================
	 */

	/**
	 * Simple error object that can hold two attributes: {@link Error#code} and {@link Error#message}.
	 *
	 * @author Martin Albedinsky
	 */
	public static final class Error {

		/**
		 * Code of this error.
		 */
		public int code;

		/**
		 * Message of this error.
		 */
		public String message;

		/**
		 */
		@Override
		@SuppressWarnings("StringBufferReplaceableByString")
		public String toString() {
			final StringBuilder builder = new StringBuilder(64);
			builder.append(getClass().getSimpleName());
			builder.append("{code: ");
			builder.append(Integer.toString(code));
			builder.append(", message: ");
			builder.append(message);
			return builder.append("}").toString();
		}
	}
}
