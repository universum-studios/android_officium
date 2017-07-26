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

import java.net.URL;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ServiceManagerServicesConfigurationTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ServiceManagerServicesConfigurationTest";

	@Test
    public void testRetrofitBuilder() {
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		assertThat(configuration.retrofitBuilder(), is(notNullValue()));
		assertThat(configuration.retrofitBuilder(), is(configuration.retrofitBuilder()));
    }

	@Test
	public void testRetrofit() throws Exception {
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		configuration.retrofitBuilder().baseUrl("https://www.google.com/");
		assertThat(configuration.retrofit(), is(notNullValue()));
		assertThat(configuration.retrofit(), is(configuration.retrofit()));
		assertThat(configuration.retrofit().baseUrl().url(), is(new URL("https://www.google.com/")));
	}

	@Test(expected = IllegalStateException.class)
	public void testRetrofitWithoutSpecifiedBaseUrl() {
		new ServiceManager.ServicesConfiguration<>(TestServices.class).retrofit();
	}

	@Test
	public void testServices() {
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		configuration.retrofitBuilder().baseUrl("https://www.google.com/");
		assertThat(configuration.services(), is(notNullValue()));
		assertThat(configuration.services(), instanceOf(TestServices.class));
		assertSame(configuration.services(), configuration.services());
	}

	@Test
	public void testInvalidate() {
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		configuration.retrofitBuilder().baseUrl("https://www.google.com/");
		TestServices services = configuration.services();
		configuration.invalidate();
		TestServices invalidatedServices = configuration.services();
		assertNotSame(services, invalidatedServices);
		configuration.invalidate();
		assertNotSame(invalidatedServices, configuration.services());
	}

	interface TestServices {
	}
}
