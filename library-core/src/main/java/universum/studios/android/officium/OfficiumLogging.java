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
package universum.studios.android.officium;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;

/**
 * Utility class used by the <b>Officium</b> library for logging purpose.
 * <p>
 * Custom {@link Logger} may be specified via {@link #setLogger(Logger)} which may be used to control
 * logging outputs of the library.
 * <p>
 * Default logger used by this class has specified {@link Log#ASSERT} log level which means the the
 * library by default does not print out any logs.
 *
 * @author Martin Albedinsky
 * @since 1.1
 */
public final class OfficiumLogging {

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Default logger used by the library for logging purpose.
	 */
	private static final Logger LOGGER = new SimpleLogger(Log.ASSERT);

	/**
	 * Logger to which is this logging utility class delegating all log related requests.
	 */
	@NonNull private static Logger sLogger = LOGGER;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 */
	private OfficiumLogging() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a logger to be used by this logging class to print out logs into console.
	 *
	 * @param logger The desired logger. May by {@code null} to use default logger.
	 * @see #getLogger()
	 */
	public static void setLogger(@Nullable final Logger logger) {
		sLogger = logger == null ? LOGGER : logger;
	}

	/**
	 * Returns the logger used by this logging class.
	 *
	 * @return Either default or custom logger.
	 * @see #setLogger(Logger)
	 */
	@NonNull public static Logger getLogger() {
		return sLogger;
	}

	/**
	 * Delegates to {@link Logger#d(String, String)}.
	 */
	public static void d(@NonNull final String tag, @NonNull final String msg) {
		sLogger.d(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#d(String, String, Throwable)}.
	 */
	public static void d(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.d(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#v(String, String)}.
	 */
	public static void v(@NonNull final String tag, @NonNull final String msg) {
		sLogger.d(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#v(String, String, Throwable)}.
	 */
	public static void v(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.v(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#i(String, String)}.
	 */
	public static void i(@NonNull final String tag, @NonNull final String msg) {
		sLogger.i(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#i(String, String, Throwable)}.
	 */
	public static void i(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.i(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#w(String, String)}.
	 */
	public static void w(@NonNull final String tag, @NonNull final String msg) {
		sLogger.w(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#w(String, Throwable)}.
	 */
	public static void w(@NonNull final String tag, @Nullable final Throwable tr) {
		sLogger.w(tag, tr);
	}

	/**
	 * Delegates to {@link Logger#w(String, String, Throwable)}.
	 */
	public static void w(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.w(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#e(String, String)}.
	 */
	public static void e(@NonNull final String tag, @NonNull final String msg) {
		sLogger.e(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#e(String, String, Throwable)}.
	 */
	public static void e(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.e(tag, msg, tr);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, String)}.
	 */
	public static void wtf(@NonNull final String tag, @NonNull final String msg) {
		sLogger.wtf(tag, msg);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, Throwable)}.
	 */
	public static void wtf(@NonNull final String tag, @Nullable final Throwable tr) {
		sLogger.wtf(tag, tr);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, String, Throwable)}.
	 */
	public static void wtf(@NonNull final String tag, @NonNull final String msg, @Nullable final Throwable tr) {
		sLogger.wtf(tag, msg, tr);
	}
}