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
    
    @Test
	public void testGetApi() {
		final ServiceApiProvider<TestApiImpl> provider = new TestProvider();
	    assertThat(provider.getApi(), is(notNullValue()));
	    assertThat(provider.getApi(), is(provider.getApi()));
	}

    @Test
	public void testInvalidateApi() {
		final ServiceApiProvider<TestApiImpl> provider = new TestProvider();
	    final TestApi api = provider.getApi();
	    provider.invalidateApi();
	    assertThat(provider.getApi(), is(not(api)));
	}

    @Test
	public void testInvalidateApiNotInitialized() {
	    new TestProvider().invalidateApi();
	}

	private interface TestApi {
	}

	private final class TestApiImpl implements TestApi {
	}

	private final class TestProvider extends ServiceApiProvider<TestApiImpl> {

		@NonNull
		@Override
		protected TestApiImpl onCreateApi() {
			return new TestApiImpl();
		}
	}
}
