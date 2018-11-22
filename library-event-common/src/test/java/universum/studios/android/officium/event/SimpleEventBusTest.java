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

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class SimpleEventBusTest extends LocalTestCase {

	@Test public void testInstantiationWithDefaultBus() {
		// Act:
		final SimpleEventBus eventBus = new SimpleEventBus();
		// Assert:
		assertThat(eventBus.getBus(), is(notNullValue()));
	}

	@Test public void testInstantiationWithBus() {
		// Arrange:
		final Bus bus = new Bus();
		// Act:
		final SimpleEventBus eventBus = new SimpleEventBus(bus);
		// Assert:
		assertThat(eventBus.getBus(), is(bus));
	}

	@Test public void testRegister() {
		// Arrange:
		final Bus mockBus = mock(Bus.class);
		final SimpleEventBus eventBus = new SimpleEventBus(mockBus);
		final Object handler = new Object();
		// Act:
		eventBus.register(handler);
		// Assert:
		verify(mockBus).register(handler);
		verifyNoMoreInteractions(mockBus);
	}

	@Test public void testUnregister() {
		// Arrange:
		final Bus mockBus = mock(Bus.class);
		final SimpleEventBus eventBus = new SimpleEventBus(mockBus);
		final Object handler = new Object();
		eventBus.register(handler);
		// Act:
		eventBus.unregister(handler);
		// Assert:
		verify(mockBus).register(handler);
		verify(mockBus).unregister(handler);
		verifyNoMoreInteractions(mockBus);
	}

	@Test public void testPost() {
		// Arrange:
		final Bus mockBus = mock(Bus.class);
		final SimpleEventBus eventBus = new SimpleEventBus(mockBus);
		final Object event = new Object();
		// Act:
		eventBus.post(event);
		// Assert:
		verify(mockBus).post(event);
		verifyNoMoreInteractions(mockBus);
	}
}