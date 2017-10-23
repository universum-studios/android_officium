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
package universum.studios.android.officium.service; 
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceApiTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ServiceApiTest";

    @Test
	public void testServices() {
		final ServiceManager mockManager = mock(ServiceManager.class);
	    final ServiceApi api = new ServiceApi<>(mockManager);
	    api.services(TestServices.class);
	    verify(mockManager, times(1)).services(TestServices.class);
	    verifyNoMoreInteractions(mockManager);
	}

    @Test
	public void testServicesConfiguration() {
	    final ServiceManager mockManager = mock(ServiceManager.class);
	    final ServiceApi api = new ServiceApi<>(mockManager);
	    api.servicesConfiguration(TestServices.class);
	    verify(mockManager, times(1)).servicesConfiguration(TestServices.class);
	    verifyNoMoreInteractions(mockManager);
	}

	private interface TestServices {
	}
}
