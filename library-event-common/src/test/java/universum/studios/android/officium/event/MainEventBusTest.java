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
package universum.studios.android.officium.event;

import com.squareup.otto.Bus;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class MainEventBusTest extends RobolectricTestCase {
    
	@Test public void testInstantiationWithDefaultBus() {
		// Act:
		final MainEventBus eventBus = new MainEventBus();
		// Assert:
		assertThat(eventBus.getBus(), is(notNullValue()));
	}

	@Test public void testInstantiationWithBus() {
		// Arrange:
		final Bus bus = new Bus();
		// Act:
		final MainEventBus eventBus = new MainEventBus(bus);
		// Assert:
		assertThat(eventBus.getBus(), is(bus));
	}

	@Test public void testPostOnMainThread() {
		// Arrange:
		final Bus mockBus = mock(Bus.class);
		final MainEventBus eventBus = new MainEventBus(mockBus);
		final Object event = new Object();
		// Act:
		eventBus.post(event);
		// Assert:
		verify(mockBus).post(event);
		verifyNoMoreInteractions(mockBus);
	}

	@Test public void testPostOnBackgroundThread() throws Exception {
		// Arrange:
		final Bus mockBus = mock(Bus.class);
		final MainEventBus eventBus = new MainEventBus(mockBus);
		final Object event = new Object();
		// Act:
		eventBus.post(event);
		// Assert:
		Thread.sleep(50);
		verify(mockBus).post(event);
		verifyNoMoreInteractions(mockBus);
	}
}