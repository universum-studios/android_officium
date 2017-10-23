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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertSame;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceManagerTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ServiceManagerTest";

    @Test
	public void testInstantiation() {
		assertThat(new ServiceManager().getEndPoint(), is(nullValue()));
    }

	@Test
	public void testInstantiationWithEndPoint() {
		final EndPoint endPoint = new SimpleEndPoint("");
		assertThat(new ServiceManager(endPoint).getEndPoint(), is(endPoint));
	}

	@Test
	public void testSetGetEndPoint() {
		final EndPoint endPoint = new SimpleEndPoint("");
		final ServiceManager manager = new ServiceManager();
		manager.setEndPoint(endPoint);
		assertThat(manager.getEndPoint(), is(endPoint));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSetEndPointAsBaseUrl() {
		final ServiceManager manager = new ServiceManager();
		manager.setEndPoint("https://www.google.com/");
		assertThat(manager.getEndPoint(), is(notNullValue()));
		assertThat(manager.getEndPoint().getBaseUrl(), is("https://www.google.com/"));
	}

	@Test
	public void testServices() {
		final ServiceManager manager = new ServiceManager(new SimpleEndPoint("https://www.google.com/"));
		assertThat(manager.services(TestPrimaryServices.class), is(notNullValue()));
		assertSame(manager.services(TestPrimaryServices.class), manager.services(TestPrimaryServices.class));
		assertThat(manager.services(TestSecondaryServices.class), is(notNullValue()));
		assertSame(manager.services(TestSecondaryServices.class), manager.services(TestSecondaryServices.class));
		assertThat(manager.services(TestTertiaryServices.class), is(notNullValue()));
		assertSame(manager.services(TestTertiaryServices.class), manager.services(TestTertiaryServices.class));
	}

	@Test
	public void testServicesConfiguration() {
		final ServiceManager manager = new ServiceManager(new SimpleEndPoint("https://www.google.com/"));
		assertThat(manager.servicesConfiguration(TestPrimaryServices.class), is(notNullValue()));
		assertSame(manager.servicesConfiguration(TestPrimaryServices.class), manager.servicesConfiguration(TestPrimaryServices.class));
		assertThat(manager.servicesConfiguration(TestSecondaryServices.class), is(notNullValue()));
		assertSame(manager.servicesConfiguration(TestSecondaryServices.class), manager.servicesConfiguration(TestSecondaryServices.class));
		assertThat(manager.servicesConfiguration(TestTertiaryServices.class), is(notNullValue()));
		assertSame(manager.servicesConfiguration(TestTertiaryServices.class), manager.servicesConfiguration(TestTertiaryServices.class));
	}

	@Test(expected = IllegalStateException.class)
	public void testServicesConfigurationWhenThereIsNoEndPointSpecified() {
		final ServiceManager manager = new ServiceManager();
		manager.servicesConfiguration(TestPrimaryServices.class).retrofit().baseUrl();
	}

	interface TestPrimaryServices {
	}

	interface TestSecondaryServices {
	}

	interface TestTertiaryServices {
	}
}
