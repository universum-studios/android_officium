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
package universum.studios.android.officium.sync;

import android.accounts.Account;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
public final class SyncOperationTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final Account account = new Account("TestName", "TestType");
		final String authority = "TestAuthority";
		final SyncTask task = new SyncTask.Builder<>(1).build();
		// Act:
		final SyncOperation operation = new SyncOperation.Builder()
				.account(account)
				.authority(authority)
				.task(task)
				.build();
		// Assert:
		assertThat(operation.account, is(account));
		assertThat(operation.authority, is(authority));
		assertThat(operation.task, is(task));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstantiationWithoutAccount() {
		// Arrange;
		final SyncOperation.Builder builder = new SyncOperation.Builder().authority("TestAuthority").task(new SyncTask.Builder<>(1).build());
		// Act:
		builder.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstantiationWithoutAuthority() {
		// Arrange:
		final SyncOperation.Builder builder = new SyncOperation.Builder().account(new Account("TestName", "TestType")).task(new SyncTask.Builder<>(1).build());
		// Act:
		builder.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBuilderBuildWithEmptyAuthority() {
		// Arrange:
		final SyncOperation.Builder builder = new SyncOperation.Builder().account(new Account("TestName", "TestType")).authority("").task(new SyncTask.Builder<>(1).build());
		// Act:
		builder.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBuilderBuildWithoutTask() {
		// Arrange:
		final SyncOperation.Builder builder = new SyncOperation.Builder().account(new Account("TestName", "TestType")).authority("TestAuthority");
		// Act:
		builder.build();
	}
}