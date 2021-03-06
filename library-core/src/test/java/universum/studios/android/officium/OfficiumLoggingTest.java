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

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
public final class OfficiumLoggingTest extends RobolectricTestCase {

	@SuppressWarnings("unused") private static final String TAG = "OfficiumLoggingTest";

	@Override public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the logging class has default logger.
		OfficiumLogging.setLogger(null);
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		OfficiumLogging.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<OfficiumLogging> constructor = OfficiumLogging.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testDefaultLogger() {
		// Act + Assert:
		final Logger logger = OfficiumLogging.getLogger();
		assertThat(logger, is(IsNot.not(IsNull.nullValue())));
		assertThat(logger.getLogLevel(), is(Log.ASSERT));
	}

	@Test public void testLogger() {
		// Act + Assert:
		OfficiumLogging.setLogger(null);
		assertThat(OfficiumLogging.getLogger(), is(IsNot.not(IsNull.nullValue())));
		final Logger logger = new SimpleLogger(Log.DEBUG);
		OfficiumLogging.setLogger(logger);
		assertThat(OfficiumLogging.getLogger(), is(logger));
	}

	@Test public void testV() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		OfficiumLogging.setLogger(mockLogger);
		// Act:
		OfficiumLogging.v(TAG, "");
		OfficiumLogging.v(TAG, "", null);
	}

	@Test public void testD() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		OfficiumLogging.setLogger(mockLogger);
		// Act:
		OfficiumLogging.d(TAG, "message.debug");
		verify(mockLogger, times(1)).d(TAG, "message.debug");
		OfficiumLogging.d(TAG, "message.debug", null);
		verify(mockLogger, times(1)).d(TAG, "message.debug", null);
	}

	@Test public void testI() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		OfficiumLogging.setLogger(mockLogger);
		// Act:
		OfficiumLogging.i(TAG, "message.info");
		verify(mockLogger, times(1)).i(TAG, "message.info");
		OfficiumLogging.i(TAG, "message.info", null);
		verify(mockLogger, times(1)).i(TAG, "message.info", null);
	}

	@Test public void testW() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		OfficiumLogging.setLogger(mockLogger);
		// Act:
		OfficiumLogging.w(TAG, "message.warn");
		verify(mockLogger, times(1)).w(TAG, "message.warn");
		OfficiumLogging.w(TAG, "message.warn", null);
		verify(mockLogger, times(1)).w(TAG, "message.warn", null);
		OfficiumLogging.w(TAG, (Throwable) null);
		verify(mockLogger, times(1)).w(TAG, (Throwable) null);
	}

	@Test public void testE() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		OfficiumLogging.setLogger(mockLogger);
		// Act:
		OfficiumLogging.e(TAG, "message.error");
		verify(mockLogger, times(1)).e(TAG, "message.error");
		OfficiumLogging.e(TAG, "message.error", null);
		verify(mockLogger, times(1)).e(TAG, "message.error", null);
	}

	@Test public void testWTF() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		OfficiumLogging.setLogger(mockLogger);
		// Act:
		OfficiumLogging.wtf(TAG, "message.wtf");
		verify(mockLogger, times(1)).wtf(TAG, "message.wtf");
		OfficiumLogging.wtf(TAG, "message.wtf", null);
		verify(mockLogger, times(1)).wtf(TAG, "message.wtf", null);
		OfficiumLogging.wtf(TAG, (Throwable) null);
		verify(mockLogger, times(1)).wtf(TAG, (Throwable) null);
	}
}