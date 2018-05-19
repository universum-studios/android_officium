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

import android.support.annotation.NonNull;

import org.junit.Test;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Martin Albedinsky
 */
public final class ServiceApiProviderTest extends LocalTestCase {

	@Test public void testApi() {
		// Arrange:
		final ServiceApiProvider<TestApiImpl> provider = new TestProvider();
		// Act + Assert:
		assertThat(provider.getApi(), is(notNullValue()));
		assertThat(provider.getApi(), is(provider.getApi()));
	}

	@Test public void testInvalidateApi() {
		// Arrange:
		final ServiceApiProvider<TestApiImpl> provider = new TestProvider();
		final TestApi api = provider.getApi();
		// Act:
		provider.invalidateApi();
		// Assert:
		assertThat(provider.getApi(), is(not(api)));
	}

	@Test public void testInvalidateApiWhenNoApiIsInitialized() {
		// Arrange:
		final TestProvider provider = new TestProvider();
		// Act:
		provider.invalidateApi();
	}

	private interface TestApi {}

	private final class TestApiImpl implements TestApi {}

	private final class TestProvider extends ServiceApiProvider<TestApiImpl> {

		@Override @NonNull protected TestApiImpl onCreateApi() {
			return new TestApiImpl();
		}
	}
}