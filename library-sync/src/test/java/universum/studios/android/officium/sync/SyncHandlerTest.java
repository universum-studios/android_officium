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

	@Test public void testInstantiationWithId() {
		// Act:
		final TestHandler handler = new TestHandler(1);
		// Assert:
		assertThat(handler.getTaskId(), is(1));
	}

	@Test public void testInstantiationWithIdAndRequestClass() {
		// Act:
		final TestHandler handler = new TestHandler(1, TestRequest.class);
		// Assert:
		assertThat(handler.getTaskId(), is(1));
	}

	@Test public void testHandleSync() {
		// Arrange:
		final TestHandler handler = new TestHandler(1, TestRequest.class);
		final SyncOperation operation = new SyncOperation.Builder()
				.account(new Account("TestAccount", "TestType"))
				.authority("test.authority")
				.task(new SyncTask.Builder<TestRequest>(1).request(new TestRequest(12)).build())
				.build();
		// Act:
		handler.handleSync(application, operation);
		// Assert:
		assertThat(handler.hasBeenOnHandleSyncInvoked(), is(true));
		assertThat(handler.hasBeenOnSyncErrorInvoked(), is(false));
		final TestRequest request = handler.getOnHandleSyncRequest();
		assertThat(request, is(notNullValue()));
		assertThat(request.count, is(12));
	}

	@Test public void testHandleSyncWithoutRequest() {
		// Arrange:
		final TestHandler handler = new TestHandler(1);
		final SyncOperation operation = new SyncOperation.Builder()
				.account(new Account("TestAccount", "TestType"))
				.authority("test.authority")
				.task(SyncTask.EMPTY)
				.build();
		// Act:
		handler.handleSync(application, operation);
		// Assert:
		assertThat(handler.hasBeenOnHandleSyncInvoked(), is(true));
		assertThat(handler.hasBeenOnSyncErrorInvoked(), is(false));
		assertThat(handler.getOnHandleSyncRequest(), is(nullValue()));
	}

	@Test public void testHandleRequestWithOccurredError() {
		// Arrange:
		final TestHandler handler = new TestHandler(1, TestRequest.class);
		handler.setExceptionToThrow(new IllegalStateException());
		final SyncOperation operation = new SyncOperation.Builder()
				.account(new Account("TestAccount", "TestType"))
				.authority("test.authority")
				.task(SyncTask.EMPTY)
				.build();
		// Act:
		handler.handleSync(application, operation);
		// Assert:
		assertThat(handler.hasBeenOnHandleSyncInvoked(), is(true));
		assertThat(handler.hasBeenOnSyncErrorInvoked(), is(true));
		assertThat(handler.isOnSyncErrorEqualExceptionToThrow(), is(true));
	}

	private static final class TestRequest implements SyncTask.Request {

		int count;

		TestRequest() {}

		TestRequest(final int count) {
			this.count = count;
		}
	}

	private static final class TestHandler extends SyncHandler<TestRequest, Void> {

		private TestRequest onHandleSyncRequest;
		private boolean onHandleSyncInvoked;
		private Exception exceptionToThrow;
		private Exception onSyncError;

		TestHandler(final int taskId) {
			super(taskId);
		}

		TestHandler(final int taskId, @Nullable final Class<TestRequest> classOfRequest) {
			super(taskId, classOfRequest);
		}

		void setExceptionToThrow(Exception toThrow) {
			this.exceptionToThrow = toThrow;
		}

		@Override @Nullable protected Void onHandleSync(
				@NonNull final Context context,
				@NonNull final SyncOperation syncOperation,
				@Nullable final TestRequest syncRequest
		) throws Exception {
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

		@Override protected void onSyncError(
				@NonNull final Context context,
				@NonNull final SyncOperation syncOperation,
				@Nullable final TestRequest syncRequest,
				@NonNull final Exception error
		) {
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