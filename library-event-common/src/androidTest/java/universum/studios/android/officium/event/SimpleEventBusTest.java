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
import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.Bus;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

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
@RunWith(AndroidJUnit4.class)
public final class SimpleEventBusTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "SimpleEventBusTest";

    @Test
	public void testInstantiationWithDefaultBus() {
		final SimpleEventBus eventBus = new SimpleEventBus();
	    assertThat(eventBus.getBus(), is(notNullValue()));
	}

    @Test
	public void testInstantiationWithBus() {
	    final Bus bus = new Bus();
	    final SimpleEventBus eventBus = new SimpleEventBus(bus);
	    assertThat(eventBus.getBus(), is(bus));
	}

	@Test
	public void testRegister() {
		final Bus mockBus = mock(Bus.class);
		final SimpleEventBus eventBus = new SimpleEventBus(mockBus);
		final Object handler = new Object();
		eventBus.register(handler);
		verify(mockBus, times(1)).register(handler);
		verifyNoMoreInteractions(mockBus);
	}

	@Test
	public void testUnregister() {
		final Bus mockBus = mock(Bus.class);
		final SimpleEventBus eventBus = new SimpleEventBus(mockBus);
		final Object handler = new Object();
		eventBus.register(handler);
		eventBus.unregister(handler);
		verify(mockBus, times(1)).register(handler);
		verify(mockBus, times(1)).unregister(handler);
		verifyNoMoreInteractions(mockBus);
	}

	@Test
	public void testPost() {
		final Bus mockBus = mock(Bus.class);
		final SimpleEventBus eventBus = new SimpleEventBus(mockBus);
		final Object event = new Object();
		eventBus.post(event);
		verify(mockBus, times(1)).post(event);
		verifyNoMoreInteractions(mockBus);
	}
}
