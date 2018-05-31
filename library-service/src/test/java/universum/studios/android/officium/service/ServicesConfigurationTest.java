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
package universum.studios.android.officium.service;

import org.junit.Test;

import java.net.URL;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * @author Martin Albedinsky
 */
public final class ServicesConfigurationTest extends LocalTestCase {

	@Test public void testRetrofitBuilder() {
		// Arrange:
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		// Act + Assert:
		assertThat(configuration.retrofitBuilder(), is(notNullValue()));
		assertThat(configuration.retrofitBuilder(), is(configuration.retrofitBuilder()));
	}

	@Test public void testRetrofit() throws Exception {
		// Arrange:
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		configuration.retrofitBuilder().baseUrl("https://www.google.com/");
		// Act + Assert:
		assertThat(configuration.retrofit(), is(notNullValue()));
		assertThat(configuration.retrofit(), is(configuration.retrofit()));
		assertThat(configuration.retrofit().baseUrl().url(), is(new URL("https://www.google.com/")));
	}

	@Test(expected = IllegalStateException.class)
	public void testRetrofitWithoutSpecifiedBaseUrl() {
		// Arrange:
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		// Act:
		configuration.retrofit();
	}

	@Test public void testServices() {
		// Arrange:
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		configuration.retrofitBuilder().baseUrl("https://www.google.com/");
		// Act + Assert:
		assertThat(configuration.services(), is(notNullValue()));
		assertThat(configuration.services(), instanceOf(TestServices.class));
		assertSame(configuration.services(), configuration.services());
	}

	@Test public void testInvalidate() {
		// Arrange:
		final ServiceManager.ServicesConfiguration<TestServices> configuration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		configuration.retrofitBuilder().baseUrl("https://www.google.com/");
		TestServices services = configuration.services();
		// Act + Assert:
		configuration.invalidate();
		TestServices invalidatedServices = configuration.services();
		assertNotSame(services, invalidatedServices);
		configuration.invalidate();
		assertNotSame(invalidatedServices, configuration.services());
	}

	interface TestServices {}
}