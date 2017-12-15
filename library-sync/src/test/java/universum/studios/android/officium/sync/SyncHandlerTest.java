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
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class SyncHandlerTest extends RobolectricTestCase {
    
    @Test
	public void testInstantiationWithId() {
		assertThat(new TestHandler(1).getTaskId(), is(1));
    }

    @Test
	public void testInstantiationWithIdAndRequestClass() {
	    assertThat(new TestHandler(1, TestRequest.class).getTaskId(), is(1));
    }

	@Test
	public void testHandleSync() {
		final TestHandler handler = new TestHandler(1, TestRequest.class);
		handler.handleSync(mApplication, new SyncOperation.Builder()
				.account(new Account("TestAccount", "TestType"))
				.authority("test.authority")
				.task(new SyncTask.Builder<TestRequest>(1).request(new TestRequest(12)).build())
				.build()
		);
		assertThat(handler.hasBeenOnHandleSyncInvoked(), is(true));
		assertThat(handler.hasBeenOnSyncErrorInvoked(), is(false));
		final TestRequest request = handler.getOnHandleSyncRequest();
		assertThat(request, is(notNullValue()));
		assertThat(request.count, is(12));
	}

	@Test
	public void testHandleSyncWithoutRequest() {
		final TestHandler handler = new TestHandler(1);
		handler.handleSync(mApplication, new SyncOperation.Builder()
				.account(new Account("TestAccount", "TestType"))
				.authority("test.authority")
				.task(SyncTask.EMPTY)
				.build()
		);
		assertThat(handler.hasBeenOnHandleSyncInvoked(), is(true));
		assertThat(handler.hasBeenOnSyncErrorInvoked(), is(false));
		assertThat(handler.getOnHandleSyncRequest(), is(nullValue()));
	}

	@Test
	public void testHandleRequestWithOccurredError() {
		final TestHandler handler = new TestHandler(1, TestRequest.class);
		handler.setExceptionToThrow(new IllegalStateException());
		handler.handleSync(mApplication, new SyncOperation.Builder()
				.account(new Account("TestAccount", "TestType"))
				.authority("test.authority")
				.task(SyncTask.EMPTY)
				.build()
		);
		assertThat(handler.hasBeenOnHandleSyncInvoked(), is(true));
		assertThat(handler.hasBeenOnSyncErrorInvoked(), is(true));
		assertThat(handler.isOnSyncErrorEqualExceptionToThrow(), is(true));
	}

    private static final class TestRequest implements SyncTask.Request {

	    int count;

	    TestRequest() {
	    }

	    TestRequest(int count) {
		    this.count = count;
	    }
    }

	private static final class TestHandler extends SyncHandler<TestRequest, Void> {

		private TestRequest onHandleSyncRequest;
		private boolean onHandleSyncInvoked;
		private Exception exceptionToThrow;
		private Exception onSyncError;

		TestHandler(int taskId) {
			super(taskId);
		}

		TestHandler(int taskId, @Nullable Class<TestRequest> classOfRequest) {
			super(taskId, classOfRequest);
		}

		void setExceptionToThrow(Exception toThrow) {
			this.exceptionToThrow = toThrow;
		}

		@Nullable
		@Override
		protected Void onHandleSync(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable TestRequest syncRequest) throws Exception {
			this.onHandleSyncInvoked = true;
			this.onHandleSyncRequest = syncRequest;
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
			return null;
		}

		boolean hasBeenOnHandleSyncInvoked() {
			return onHandleSyncInvoked;
		}

		TestRequest getOnHandleSyncRequest() {
			return onHandleSyncRequest;
		}

		@Override
		protected void onSyncError(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable TestRequest syncRequest, @NonNull Exception error) {
			super.onSyncError(context, syncOperation, syncRequest, error);
			this.onSyncError = error;
		}

		boolean hasBeenOnSyncErrorInvoked() {
			return onSyncError != null;
		}

		boolean isOnSyncErrorEqualExceptionToThrow() {
			return onSyncError == exceptionToThrow;
		}
	}
}
