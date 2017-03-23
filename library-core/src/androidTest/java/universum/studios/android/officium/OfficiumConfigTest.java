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
package universum.studios.android.officium;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class OfficiumConfigTest {

	@SuppressWarnings("unused")
	private static final String TAG = "OfficiumConfigTest";

	@Test
	public void testConfiguration() {
		assertThat(OfficiumConfig.LOG_ENABLED, Matchers.is(true));
		assertThat(OfficiumConfig.DEBUG_LOG_ENABLED, Matchers.is(false));
	}
}