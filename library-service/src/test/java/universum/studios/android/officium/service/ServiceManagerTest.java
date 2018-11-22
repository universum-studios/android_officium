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

import okhttp3.HttpUrl;
import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertSame;

/**
 * @author Martin Albedinsky
 */
public final class ServiceManagerTest extends LocalTestCase {

	private static final String BASE_URL = "https://www.android.com/";

	@SuppressWarnings("ConstantConditions")
	@Test public void testInstantiation() {
		// Arrange:
		final HttpUrl baseUrl = HttpUrl.parse(BASE_URL);
		// Act:
		final ServiceManager manager = new ServiceManager(baseUrl);
		// Assert:
		assertThat(manager.getBaseUrl(), is(baseUrl));
		assertThat(manager.getBaseUrl().toString(), is(BASE_URL));
	}

	@Test public void testInstantiationWithBaseUrlString() {
		// Act:
		final ServiceManager manager = new ServiceManager(BASE_URL);
		// Assert:
		assertThat(manager.getBaseUrl(), is(notNullValue()));
	}

	@Test public void testInstantiationWithEndPoint() {
		// Arrange:
		final EndPoint endPoint = new SimpleEndPoint(BASE_URL);
		// Act:
		final ServiceManager manager = new ServiceManager(endPoint);
		// Assert:
		assertThat(manager.getEndPoint(), is(endPoint));
		assertThat(manager.getBaseUrl(), is(notNullValue()));
		assertThat(manager.getBaseUrl().toString(), is(BASE_URL));
	}

	@Test public void testEndPoint() {
		// Arrange:
		final EndPoint endPoint = new SimpleEndPoint(BASE_URL);
		final ServiceManager manager = new ServiceManager();
		// Act + Assert:
		manager.setEndPoint(endPoint);
		assertThat(manager.getEndPoint(), is(endPoint));
		assertThat(manager.getBaseUrl(), is(notNullValue()));
		assertThat(manager.getBaseUrl().toString(), is(BASE_URL));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testEndPointAsBaseUrl() {
		// Arrange:
		final ServiceManager manager = new ServiceManager();
		// Act:
		manager.setEndPoint("https://www.android.com/");
		// Assert:
		assertThat(manager.getEndPoint(), is(notNullValue()));
		assertThat(manager.getEndPoint().getBaseUrl(), is("https://www.android.com/"));
		assertThat(manager.getBaseUrl(), is(notNullValue()));
		assertThat(manager.getBaseUrl().toString(), is(BASE_URL));
	}

	@Test public void testServices() {
		// Arrange:
		final ServiceManager manager = new ServiceManager(BASE_URL);
		// Act + Assert:
		assertThat(manager.services(TestPrimaryServices.class), is(notNullValue()));
		assertSame(manager.services(TestPrimaryServices.class), manager.services(TestPrimaryServices.class));
		assertThat(manager.services(TestSecondaryServices.class), is(notNullValue()));
		assertSame(manager.services(TestSecondaryServices.class), manager.services(TestSecondaryServices.class));
		assertThat(manager.services(TestTertiaryServices.class), is(notNullValue()));
		assertSame(manager.services(TestTertiaryServices.class), manager.services(TestTertiaryServices.class));
	}

	@Test public void testServicesConfiguration() {
		// Arrange:
		final ServiceManager manager = new ServiceManager(BASE_URL);
		// Act + Assert:
		assertThat(manager.servicesConfiguration(TestPrimaryServices.class), is(notNullValue()));
		assertSame(manager.servicesConfiguration(TestPrimaryServices.class), manager.servicesConfiguration(TestPrimaryServices.class));
		assertThat(manager.servicesConfiguration(TestSecondaryServices.class), is(notNullValue()));
		assertSame(manager.servicesConfiguration(TestSecondaryServices.class), manager.servicesConfiguration(TestSecondaryServices.class));
		assertThat(manager.servicesConfiguration(TestTertiaryServices.class), is(notNullValue()));
		assertSame(manager.servicesConfiguration(TestTertiaryServices.class), manager.servicesConfiguration(TestTertiaryServices.class));
	}

	interface TestPrimaryServices {}

	interface TestSecondaryServices {}

	interface TestTertiaryServices {}
}