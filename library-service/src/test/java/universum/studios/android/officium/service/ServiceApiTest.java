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

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ServiceApiTest extends LocalTestCase {

	@Test public void testServices() {
		// Arrange:
		final ServiceManager mockManager = mock(ServiceManager.class);
		final TestServices mockServices = mock(TestServices.class);
		when(mockManager.services(TestServices.class)).thenReturn(mockServices);
		final ServiceApi<ServiceManager> api = new ServiceApi<>(mockManager);
		// Act:
		final TestServices services = api.services(TestServices.class);
		// Assert:
		assertThat(services, is(mockServices));
		verify(mockManager).services(TestServices.class);
		verifyNoMoreInteractions(mockManager);
	}

	@Test public void testServicesConfiguration() {
		// Arrange:
		final ServiceManager mockManager = mock(ServiceManager.class);
		final ServiceManager.ServicesConfiguration<TestServices> servicesConfiguration = new ServiceManager.ServicesConfiguration<>(TestServices.class);
		when(mockManager.servicesConfiguration(TestServices.class)).thenReturn(servicesConfiguration);
		final ServiceApi<ServiceManager> api = new ServiceApi<>(mockManager);
		// Act:
		final ServiceManager.ServicesConfiguration<TestServices> configuration = api.servicesConfiguration(TestServices.class);
		// Assert:
		assertThat(configuration, is(servicesConfiguration));
		verify(mockManager).servicesConfiguration(TestServices.class);
		verifyNoMoreInteractions(mockManager);
	}

	private interface TestServices {}
}