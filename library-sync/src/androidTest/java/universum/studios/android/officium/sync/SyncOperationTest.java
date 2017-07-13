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
package universum.studios.android.officium.sync; 
import android.accounts.Account;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SyncOperationTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "SyncOperationTest";

    @Test
	public void testBuilderBuild() {
	    final Account account = new Account("TestName", "TestType");
	    final String authority = "TestAuthority";
	    final SyncTask task = new SyncTask.Builder<>(1).build();
		final SyncOperation operation = new SyncOperation.Builder()
				.account(account)
				.authority(authority)
				.task(task)
				.build();
	    assertThat(operation.account, is(account));
	    assertThat(operation.authority, is(authority));
	    assertThat(operation.task, is(task));
    }

	@Test(expected = IllegalArgumentException.class)
	public void testBuilderBuildWithoutAccount() {
	    new SyncOperation.Builder().authority("TestAuthority").task(new SyncTask.Builder<>(1).build()).build();
    }

	@Test(expected = IllegalArgumentException.class)
	public void testBuilderBuildWithoutAuthority() {
	    new SyncOperation.Builder().account(new Account("TestName", "TestType")).task(new SyncTask.Builder<>(1).build()).build();
    }

	@Test(expected = IllegalArgumentException.class)
	public void testBuilderBuildWithEmptyAuthority() {
	    new SyncOperation.Builder().account(new Account("TestName", "TestType")).authority("").task(new SyncTask.Builder<>(1).build()).build();
    }

    @Test(expected = IllegalArgumentException.class)
	public void testBuilderBuildWithoutTask() {
	    new SyncOperation.Builder().account(new Account("TestName", "TestType")).authority("TestAuthority").build();
	}
}
