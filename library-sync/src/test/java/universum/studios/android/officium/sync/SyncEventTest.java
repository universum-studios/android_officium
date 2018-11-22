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
import android.os.Bundle;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class SyncEventTest extends RobolectricTestCase {

	@Test public void testTypes() {
		// Act + Assert:
		assertThat(SyncEvent.START, is(1));
		assertThat(SyncEvent.PROGRESS, is(SyncEvent.START + 1));
		assertThat(SyncEvent.FINISH, is(SyncEvent.PROGRESS + 1));
		assertThat(SyncEvent.FAILURE, is(SyncEvent.FINISH + 1));
	}

	@Test public void testInstantiation() {
		// Act:
		final SyncEvent event = new SyncEvent.Builder(1).build();
		// Assert:
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.START));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(nullValue()));
	}

	@Test public void testInstantiationWithAccount() {
		// Arrange:
		final Account account = new Account("TestName", "TestType");
		// Act:
		final SyncEvent event = new SyncEvent.Builder(1).account(account).build();
		// Assert:
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.START));
		assertThat(event.account, is(account));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(nullValue()));
	}

	@Test public void testInstantiationWithProgress() {
		// Act:
		final SyncEvent event = new SyncEvent.Builder(1).type(SyncEvent.PROGRESS).progress(55).build();
		// Assert:
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.PROGRESS));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(55));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(nullValue()));
	}

	@Test public void testInstantiationWithError() {
		// Arrange:
		final Exception error = new IllegalStateException();
		// Act:
		final SyncEvent event = new SyncEvent.Builder(1).type(SyncEvent.FAILURE).error(error).build();
		// Assert:
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FAILURE));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(error));
		assertThat(event.extras, is(nullValue()));
	}

	@Test public void testInstantiationWithExtras() {
		// Arrange:
		final Bundle extras = new Bundle();
		// Act:
		final SyncEvent event = new SyncEvent.Builder(1).type(SyncEvent.FINISH).extras(extras).build();
		// Assert:
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FINISH));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(extras));
	}
}