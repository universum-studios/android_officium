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
package universum.studios.android.officium.event;

import com.squareup.otto.Bus;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class MainEventBusTest extends RobolectricTestCase {
    
	@Test
	public void testInstantiationWithDefaultBus() {
		final MainEventBus eventBus = new MainEventBus();
		assertThat(eventBus.getBus(), is(notNullValue()));
	}

	@Test
	public void testInstantiationWithBus() {
		final Bus bus = new Bus();
		final MainEventBus eventBus = new MainEventBus(bus);
		assertThat(eventBus.getBus(), is(bus));
	}

	@Test
	public void testPostOnMainThread() {
		final Bus mockBus = mock(Bus.class);
		final MainEventBus eventBus = new MainEventBus(mockBus);
		final Object event = new Object();
		eventBus.post(event);
		verify(mockBus, times(1)).post(event);
		verifyNoMoreInteractions(mockBus);
	}

	@Test
	public void testPostOnBackgroundThread() throws Exception {
		final Bus mockBus = mock(Bus.class);
		final MainEventBus eventBus = new MainEventBus(mockBus);
		final Object event = new Object();
		eventBus.post(event);
		Thread.sleep(50);
		verify(mockBus, times(1)).post(event);
		verifyNoMoreInteractions(mockBus);
	}
}
