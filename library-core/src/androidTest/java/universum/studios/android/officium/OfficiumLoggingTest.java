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
package universum.studios.android.officium;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class OfficiumLoggingTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "OfficiumLoggingTest";

	private Logger mMockLogger;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockLogger = mock(Logger.class);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the logging class has default logger.
		OfficiumLogging.setLogger(null);
	}

	@Test
	public void testGetDefaultLogger() {
		final Logger logger = OfficiumLogging.getLogger();
		assertThat(logger, is(IsNot.not(IsNull.nullValue())));
		assertThat(logger.getLogLevel(), is(Log.ASSERT));
	}

	@Test
	public void testSetLogger() {
		OfficiumLogging.setLogger(null);
		assertThat(OfficiumLogging.getLogger(), is(IsNot.not(IsNull.nullValue())));
		final Logger logger = new SimpleLogger(Log.DEBUG);
		OfficiumLogging.setLogger(logger);
		assertThat(OfficiumLogging.getLogger(), is(logger));
	}

	@Test
	public void testV() {
		OfficiumLogging.setLogger(mMockLogger);
		OfficiumLogging.v(TAG, "");
		OfficiumLogging.v(TAG, "", null);
	}

	@Test
	public void testD() {
		OfficiumLogging.setLogger(mMockLogger);
		OfficiumLogging.d(TAG, "message.debug");
		verify(mMockLogger, times(1)).d(TAG, "message.debug");
		OfficiumLogging.d(TAG, "message.debug", null);
		verify(mMockLogger, times(1)).d(TAG, "message.debug", null);
	}

	@Test
	public void testI() {
		OfficiumLogging.setLogger(mMockLogger);
		OfficiumLogging.i(TAG, "message.info");
		verify(mMockLogger, times(1)).i(TAG, "message.info");
		OfficiumLogging.i(TAG, "message.info", null);
		verify(mMockLogger, times(1)).i(TAG, "message.info", null);
	}

	@Test
	public void testW() {
		OfficiumLogging.setLogger(mMockLogger);
		OfficiumLogging.w(TAG, "message.warn");
		verify(mMockLogger, times(1)).w(TAG, "message.warn");
		OfficiumLogging.w(TAG, "message.warn", null);
		verify(mMockLogger, times(1)).w(TAG, "message.warn", null);
		OfficiumLogging.w(TAG, (Throwable) null);
		verify(mMockLogger, times(1)).w(TAG, (Throwable) null);
	}

	@Test
	public void testE() {
		OfficiumLogging.setLogger(mMockLogger);
		OfficiumLogging.e(TAG, "message.error");
		verify(mMockLogger, times(1)).e(TAG, "message.error");
		OfficiumLogging.e(TAG, "message.error", null);
		verify(mMockLogger, times(1)).e(TAG, "message.error", null);
	}

	@Test
	public void testWTF() {
		OfficiumLogging.setLogger(mMockLogger);
		OfficiumLogging.wtf(TAG, "message.wtf");
		verify(mMockLogger, times(1)).wtf(TAG, "message.wtf");
		OfficiumLogging.wtf(TAG, "message.wtf", null);
		verify(mMockLogger, times(1)).wtf(TAG, "message.wtf", null);
		OfficiumLogging.wtf(TAG, (Throwable) null);
		verify(mMockLogger, times(1)).wtf(TAG, (Throwable) null);
	}
}
