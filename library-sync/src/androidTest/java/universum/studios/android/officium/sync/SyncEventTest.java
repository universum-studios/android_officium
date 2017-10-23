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
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class SyncEventTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "SyncEventTest";

    @Test
	public void testTypes() {
		assertThat(SyncEvent.START, is(1));
		assertThat(SyncEvent.PROGRESS, is(SyncEvent.START + 1));
		assertThat(SyncEvent.FINISH, is(SyncEvent.PROGRESS + 1));
		assertThat(SyncEvent.FAILURE, is(SyncEvent.FINISH + 1));
    }

	@Test
	public void testBuilderBuild() {
		final SyncEvent event = new SyncEvent.Builder(1).build();
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.START));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(nullValue()));
	}

	@Test
	public void testBuilderBuildWithAccount() {
		final Account account = new Account("TestName", "TestType");
		final SyncEvent event = new SyncEvent.Builder(1).account(account).build();
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.START));
		assertThat(event.account, is(account));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(nullValue()));
	}

	@Test
	public void testBuilderBuildWithProgress() {
		final SyncEvent event = new SyncEvent.Builder(1).type(SyncEvent.PROGRESS).progress(55).build();
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.PROGRESS));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(55));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(nullValue()));
	}

	@Test
	public void testBuilderBuildWithError() {
		final Exception error = new IllegalStateException();
		final SyncEvent event = new SyncEvent.Builder(1).type(SyncEvent.FAILURE).error(error).build();
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FAILURE));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(error));
		assertThat(event.extras, is(nullValue()));
	}

	@Test
	public void testBuilderBuildWithExtras() {
		final Bundle extras = new Bundle();
		final SyncEvent event = new SyncEvent.Builder(1).type(SyncEvent.FINISH).extras(extras).build();
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FINISH));
		assertThat(event.account, is(nullValue()));
		assertThat(event.progress, is(0));
		assertThat(event.error, is(nullValue()));
		assertThat(event.extras, is(extras));
	}
}
