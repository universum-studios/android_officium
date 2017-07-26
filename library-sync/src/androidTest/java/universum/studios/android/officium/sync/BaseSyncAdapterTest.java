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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseSyncAdapterTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "BaseSyncAdapterTest";

	@Test
	public void testSetGetEventDispatcher() {
	    final TestAdapter adapter = new TestAdapter(mContext);
	    final BaseSyncAdapter.EventDispatcher mockDispatcher = mock(BaseSyncAdapter.EventDispatcher.class);
	    adapter.setEventDispatcher(mockDispatcher);
	    assertThat(adapter.getEventDispatcher(), is(mockDispatcher));
	}

	@Test
	public void testSetGetGlobalSyncHandler() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final SyncHandler mockHandler = mock(SyncHandler.class);
		adapter.setGlobalSyncHandler(mockHandler);
		assertThat(adapter.getGlobalSyncHandler(), is(mockHandler));
	}

	@Test
	public void testRegisterTaskHandler() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(12);
		adapter.registerTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
	}

	@Test
	public void testRegisterTaskHandlerWhenAlreadyRegistered() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(12);
		adapter.registerTaskHandler(handler);
		adapter.registerTaskHandler(handler);
		adapter.registerTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
	}

	@Test
	public void testUnregisterTaskHandler() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(12);
		adapter.registerTaskHandler(handler);
		adapter.unregisterTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		assertThat(handler.onHandleSyncInvokedTimes, is(0));
	}

	@Test
	public void testUnregisterTaskHandlerWhenAlreadyUnregistered() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(12);
		adapter.registerTaskHandler(handler);
		adapter.unregisterTaskHandler(handler);
		adapter.unregisterTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		assertThat(handler.onHandleSyncInvokedTimes, is(0));
	}

	@Test
	public void testUnregisterTaskHandlerWhenNotRegistered() {
		new TestAdapter(mContext).unregisterTaskHandler(new TestSyncHandler(12));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testOnPerformSyncAsGlobal() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final Account account = new Account("TestName", "TestType");
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, SyncTask.DEFAULT_ID);
		final String authority = "test.authority";
		adapter.onPerformSync(account, extras, authority, null, null);
		assertThat(adapter.onPerformGlobalSyncInvoked, is(true));
		assertThat(adapter.onPerformGlobalSyncOperation, is(notNullValue()));
		assertThat(adapter.onPerformGlobalSyncOperation.account, is(account));
		assertThat(adapter.onPerformGlobalSyncOperation.authority, is(authority));
		assertThat(adapter.onPerformGlobalSyncOperation.task, is(notNullValue()));
		assertThat(adapter.onPerformGlobalSyncOperation.task.getId(), is(SyncTask.DEFAULT_ID));
		assertThat(adapter.onPerformGlobalSyncOperation.task.getState(), is(not(SyncTask.PENDING)));
		assertThat(taskStateChangeListener.changedIds.isEmpty(), is(false));
		assertThat(taskStateChangeListener.changedIds.get(0), is(SyncTask.DEFAULT_ID));
		assertThat(taskStateChangeListener.changedStates.isEmpty(), is(false));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.RUNNING));
		assertThat(adapter.onPerformSyncInvoked, is(false));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testOnPerformSyncAsSecondary() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final Account account = new Account("TestName", "TestType");
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 12);
		final String authority = "test.authority";
		adapter.onPerformSync(account, extras, authority, null, null);
		assertThat(adapter.onPerformSyncInvoked, is(true));
		assertThat(adapter.onPerformSyncOperation, is(notNullValue()));
		assertThat(adapter.onPerformSyncOperation.account, is(account));
		assertThat(adapter.onPerformSyncOperation.authority, is(authority));
		assertThat(adapter.onPerformSyncOperation.task, is(notNullValue()));
		assertThat(adapter.onPerformSyncOperation.task.getId(), is(12));
		assertThat(adapter.onPerformSyncOperation.task.getState(), is(not(SyncTask.PENDING)));
		assertThat(taskStateChangeListener.changedIds.isEmpty(), is(false));
		assertThat(taskStateChangeListener.changedIds.get(0), is(12));
		assertThat(taskStateChangeListener.changedStates.isEmpty(), is(false));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.RUNNING));
		assertThat(adapter.onPerformGlobalSyncInvoked, is(false));
	}

	@Test
	public void testCreateTaskFromExtras() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 3);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, "{count: 15}");
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, SyncTask.CANCELED);
		final SyncTask task = adapter.createTaskFromExtras(extras);
		assertThat(task, is(new SyncTask(extras)));
		assertThat(task.getState(), is(SyncTask.CANCELED));
	}

	@Test
	public void testOnPerformGlobalSyncForOperation() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(SyncTask.DEFAULT_ID);
		adapter.setGlobalSyncHandler(handler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		// FIRST ROUND -----------------------------------------------------------------------------
		SyncOperation operation = createSyncOperation(SyncTask.DEFAULT_ID);
		adapter.onPerformGlobalSync(operation);
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
		assertThat(adapter.onGlobalSyncFinishedInvoked, is(true));
		assertThat(adapter.onGlobalSyncFinishedOperation, is(operation));
		assertThat(adapter.onGlobalSyncFailedInvoked, is(false));
		assertThat(adapter.onGlobalSyncFailedError, is(nullValue()));
		assertThat(adapter.onSyncFinishedInvoked, is(false));
		assertThat(adapter.onSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedIds.size(), is(1));
		assertThat(taskStateChangeListener.changedIds.get(0), is(SyncTask.DEFAULT_ID));
		assertThat(taskStateChangeListener.changedStates.size(), is(1));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FINISHED));
		assertThat(adapter.dispatchEvents.size(), is(2));
		assertThat(adapter.dispatchEvents.get(0), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(0), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(0)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(1), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(1), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(1)).type, is(SyncEvent.FINISH));
		// SECOND ROUND ----------------------------------------------------------------------------
		operation = createSyncOperation(SyncTask.DEFAULT_ID);
		adapter.onPerformGlobalSync(operation);
		assertThat(handler.onHandleSyncInvokedTimes, is(2));
		assertThat(adapter.onGlobalSyncFinishedOperation, is(operation));
		assertThat(adapter.onGlobalSyncFailedError, is(nullValue()));
		assertThat(taskStateChangeListener.changedIds.size(), is(2));
		assertThat(taskStateChangeListener.changedIds.get(0), is(SyncTask.DEFAULT_ID));
		assertThat(taskStateChangeListener.changedIds.get(1), is(SyncTask.DEFAULT_ID));
		assertThat(taskStateChangeListener.changedStates.size(), is(2));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FINISHED));
		assertThat(taskStateChangeListener.changedStates.get(1), is(SyncTask.FINISHED));
		assertThat(adapter.dispatchEvents.size(), is(4));
		assertThat(adapter.dispatchEvents.get(2), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(2), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(2)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(3), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(3), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(3)).type, is(SyncEvent.FINISH));
	}

	@Test
	public void testOnPerformGlobalSyncForOperationWhichFails() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(SyncTask.DEFAULT_ID);
		handler.exceptionToThrow = new IllegalStateException();
		adapter.setGlobalSyncHandler(handler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(SyncTask.DEFAULT_ID);
		adapter.onPerformGlobalSync(operation);
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
		assertThat(adapter.onGlobalSyncFinishedInvoked, is(false));
		assertThat(adapter.onGlobalSyncFinishedOperation, is(nullValue()));
		assertThat(adapter.onGlobalSyncFailedInvoked, is(true));
		assertThat(adapter.onGlobalSyncFailedError, instanceOf(TestException.class));
		assertThat(adapter.onSyncFinishedInvoked, is(false));
		assertThat(adapter.onSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedIds.size(), is(1));
		assertThat(taskStateChangeListener.changedIds.get(0), is(operation.task.getId()));
		assertThat(taskStateChangeListener.changedStates.size(), is(1));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FAILED));
		assertThat(adapter.dispatchEvents.size(), is(2));
		assertThat(adapter.dispatchEvents.get(0), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(0), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(0)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(1), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(1), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(1)).type, is(SyncEvent.FAILURE));
		assertThat(((SyncEvent) adapter.dispatchEvents.get(1)).error, is(adapter.onGlobalSyncFailedError));
	}

	@Test
	public void testOnPerformGlobalSyncForOperationWithoutRegisteredHandler() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(SyncTask.DEFAULT_ID);
		adapter.onPerformGlobalSync(operation);
		assertThat(adapter.onGlobalSyncFinishedInvoked, is(false));
		assertThat(adapter.onGlobalSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedStates.size(), is(0));
		assertThat(adapter.dispatchEvents.size(), is(0));
	}

	@Test
	public void testOnGlobalSyncFinished() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.onGlobalSyncFinished(syncOperation);
		assertThat(dispatcher.dispatchEvent, is(notNullValue()));
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FINISH));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, nullValue());
	}

	@Test
	public void testOnGlobalSyncFailed() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.onGlobalSyncFailed(syncOperation, new IllegalStateException());
		assertThat(dispatcher.dispatchEvent, is(notNullValue()));
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FAILURE));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, instanceOf(IllegalStateException.class));
	}

	@Test
	public void testOnPerformSyncForOperation() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler firstHandler = new TestSyncHandler(11);
		final TestSyncHandler secondHandler = new TestSyncHandler(12);
		adapter.registerTaskHandler(firstHandler);
		adapter.registerTaskHandler(secondHandler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		// FIRST ROUND -----------------------------------------------------------------------------
		SyncOperation operation = createSyncOperation(12);
		adapter.onPerformSync(operation);
		assertThat(firstHandler.onHandleSyncInvokedTimes, is(0));
		assertThat(secondHandler.onHandleSyncInvokedTimes, is(1));
		assertThat(adapter.onSyncFinishedInvoked, is(true));
		assertThat(adapter.onSyncFinishedOperation, is(operation));
		assertThat(adapter.onSyncFailedInvoked, is(false));
		assertThat(adapter.onSyncFailedError, is(nullValue()));
		assertThat(adapter.onGlobalSyncFinishedInvoked, is(false));
		assertThat(adapter.onGlobalSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedIds.size(), is(1));
		assertThat(taskStateChangeListener.changedIds.get(0), is(12));
		assertThat(taskStateChangeListener.changedStates.size(), is(1));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FINISHED));
		assertThat(adapter.dispatchEvents.size(), is(2));
		assertThat(adapter.dispatchEvents.get(0), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(0), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(0)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(1), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(1), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(1)).type, is(SyncEvent.FINISH));
		// SECOND ROUND ----------------------------------------------------------------------------
		operation = createSyncOperation(12);
		adapter.onPerformSync(operation);
		assertThat(firstHandler.onHandleSyncInvokedTimes, is(0));
		assertThat(secondHandler.onHandleSyncInvokedTimes, is(2));
		assertThat(adapter.onSyncFinishedOperation, is(operation));
		assertThat(adapter.onSyncFailedError, is(nullValue()));
		assertThat(taskStateChangeListener.changedIds.size(), is(2));
		assertThat(taskStateChangeListener.changedIds.get(0), is(12));
		assertThat(taskStateChangeListener.changedIds.get(1), is(12));
		assertThat(taskStateChangeListener.changedStates.size(), is(2));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FINISHED));
		assertThat(taskStateChangeListener.changedStates.get(1), is(SyncTask.FINISHED));
		assertThat(adapter.dispatchEvents.size(), is(4));
		assertThat(adapter.dispatchEvents.get(2), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(2), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(2)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(3), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(3), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(3)).type, is(SyncEvent.FINISH));
		// THIRD ROUND -----------------------------------------------------------------------------
		operation = createSyncOperation(11);
		adapter.onPerformSync(operation);
		assertThat(firstHandler.onHandleSyncInvokedTimes, is(1));
		assertThat(secondHandler.onHandleSyncInvokedTimes, is(2));
		assertThat(adapter.onSyncFinishedOperation, is(operation));
		assertThat(adapter.onSyncFailedError, is(nullValue()));
		assertThat(taskStateChangeListener.changedIds.size(), is(3));
		assertThat(taskStateChangeListener.changedIds.get(0), is(12));
		assertThat(taskStateChangeListener.changedIds.get(1), is(12));
		assertThat(taskStateChangeListener.changedIds.get(2), is(11));
		assertThat(taskStateChangeListener.changedStates.size(), is(3));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FINISHED));
		assertThat(taskStateChangeListener.changedStates.get(1), is(SyncTask.FINISHED));
		assertThat(taskStateChangeListener.changedStates.get(2), is(SyncTask.FINISHED));
		assertThat(adapter.dispatchEvents.size(), is(6));
		assertThat(adapter.dispatchEvents.get(4), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(4), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(4)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(5), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(5), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(5)).type, is(SyncEvent.FINISH));
	}

	@Test
	public void testOnPerformSyncForOperationWhichFails() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestSyncHandler handler = new TestSyncHandler(12);
		handler.exceptionToThrow = new IllegalStateException();
		adapter.registerTaskHandler(handler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(12);
		adapter.onPerformSync(operation);
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
		assertThat(adapter.onSyncFinishedInvoked, is(false));
		assertThat(adapter.onSyncFinishedOperation, is(nullValue()));
		assertThat(adapter.onSyncFailedInvoked, is(true));
		assertThat(adapter.onSyncFailedError, instanceOf(TestException.class));
		assertThat(adapter.onGlobalSyncFinishedInvoked, is(false));
		assertThat(adapter.onGlobalSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedIds.size(), is(1));
		assertThat(taskStateChangeListener.changedIds.get(0), is(operation.task.getId()));
		assertThat(taskStateChangeListener.changedStates.size(), is(1));
		assertThat(taskStateChangeListener.changedStates.get(0), is(SyncTask.FAILED));
		assertThat(adapter.dispatchEvents.size(), is(2));
		assertThat(adapter.dispatchEvents.get(0), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(0), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(0)).type, is(SyncEvent.START));
		assertThat(adapter.dispatchEvents.get(1), instanceOf(SyncEvent.class));
		assertThatEventCorrespondsWithOperation((SyncEvent) adapter.dispatchEvents.get(1), operation);
		assertThat(((SyncEvent) adapter.dispatchEvents.get(1)).type, is(SyncEvent.FAILURE));
		assertThat(((SyncEvent) adapter.dispatchEvents.get(1)).error, is(adapter.onSyncFailedError));
	}

	@Test
	public void testOnPerformSyncForOperationWithoutRegisteredHandler() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(12);
		adapter.onPerformSync(operation);
		assertThat(adapter.onSyncFinishedInvoked, is(false));
		assertThat(adapter.onSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedStates.size(), is(0));
		assertThat(adapter.dispatchEvents.size(), is(0));
	}

	@Test
	public void testOnSyncFinished() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.onSyncFinished(syncOperation);
		assertThat(dispatcher.dispatchEvent, is(notNullValue()));
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FINISH));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, nullValue());
	}

	@Test
	public void testOnSyncFailed() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.onSyncFailed(syncOperation, new IllegalStateException());
		assertThat(dispatcher.dispatchEvent, is(notNullValue()));
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FAILURE));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, instanceOf(IllegalStateException.class));
	}

	@Test
	public void testChangeTaskStateToAndNotify() {
		final OnSyncTaskStateChangeListener mockListener = mock(OnSyncTaskStateChangeListener.class);
		final TestAdapter adapter = new TestAdapter(mContext);
		adapter.setOnTaskStateChangeListener(mockListener);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
		verify(mockListener, times(1)).onSyncTaskStateChanged(syncOperation.task, syncOperation.account);
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testChangeTaskStateToAndNotifyWithoutListener() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
	}

	@Test
	public void testDispatchSyncEvent() {
		final TestAdapter adapter = new TestAdapter(mContext);
		final BaseSyncAdapter.EventDispatcher mockDispatcher = mock(BaseSyncAdapter.EventDispatcher.class);
		adapter.setEventDispatcher(mockDispatcher);
		adapter.dispatchSyncEvent("Event");
		verify(mockDispatcher, times(1)).dispatch("Event");
		verifyNoMoreInteractions(mockDispatcher);
	}

	@Test
	public void testDispatchSyncEventWithoutDispatcher() {
		new TestAdapter(mContext).dispatchSyncEvent("Event");
	}

	private static SyncOperation createSyncOperation(int taskId) {
		return new SyncOperation.Builder()
				.account(new Account("TestName", "TestType"))
				.authority("test.authority")
				.task(new SyncTask.Builder<>(taskId).build())
				.build();
	}

	private static void assertThatEventCorrespondsWithOperation(SyncEvent event, SyncOperation operation) {
		assertThat(event.taskId, is(operation.task.getId()));
		assertThat(event.account, is(operation.account));
	}

	private static final class TestAdapter extends BaseSyncAdapter {

		boolean onPerformGlobalSyncInvoked, onPerformSyncInvoked;
		SyncOperation onPerformGlobalSyncOperation, onPerformSyncOperation;
		boolean onGlobalSyncFinishedInvoked, onGlobalSyncFailedInvoked;
		boolean onSyncFinishedInvoked, onSyncFailedInvoked;
		SyncOperation onGlobalSyncFinishedOperation, onSyncFinishedOperation;
		Exception onGlobalSyncFailedError, onSyncFailedError;
		final List<Object> dispatchEvents = new ArrayList<>(2);

		TestAdapter(@NonNull Context context) {
			super(context.getApplicationContext(), true);
		}

		@Override
		protected void onPerformGlobalSync(@NonNull SyncOperation syncOperation) {
			super.onPerformGlobalSync(syncOperation);
			this.onPerformGlobalSyncInvoked = true;
			this.onPerformGlobalSyncOperation = syncOperation;
		}

		@Override
		protected void onGlobalSyncFinished(@NonNull SyncOperation syncOperation) {
			super.onGlobalSyncFinished(syncOperation);
			this.onGlobalSyncFinishedInvoked = true;
			this.onGlobalSyncFinishedOperation = syncOperation;
		}

		@Override
		protected void onGlobalSyncFailed(@NonNull SyncOperation syncOperation, @NonNull Exception error) {
			super.onGlobalSyncFailed(syncOperation, error);
			this.onGlobalSyncFailedInvoked = true;
			this.onGlobalSyncFailedError = error;
		}

		@Override
		protected void onPerformSync(@NonNull SyncOperation syncOperation) {
			super.onPerformSync(syncOperation);
			this.onPerformSyncInvoked = true;
			this.onPerformSyncOperation = syncOperation;
		}

		@Override
		protected void onSyncFinished(@NonNull SyncOperation syncOperation) {
			super.onSyncFinished(syncOperation);
			this.onSyncFinishedInvoked = true;
			this.onSyncFinishedOperation = syncOperation;
		}

		@Override
		protected void onSyncFailed(@NonNull SyncOperation syncOperation, @NonNull Exception error) {
			super.onSyncFailed(syncOperation, error);
			this.onSyncFailedInvoked = true;
			this.onSyncFailedError = error;
		}

		@Override
		protected void dispatchSyncEvent(@NonNull Object event) {
			super.dispatchSyncEvent(event);
			this.dispatchEvents.add(event);
		}
	}

	private static class TestEventDispatcher implements BaseSyncAdapter.EventDispatcher {

		Object dispatchEvent;

		@Override
		public void dispatch(@NonNull Object event) {
			this.dispatchEvent = event;
		}
	}

	private static class TestTaskStateChangeListener implements OnSyncTaskStateChangeListener {

		final List<Integer> changedIds = new ArrayList<>(2);
		final List<Integer> changedStates = new ArrayList<>();

		@Override
		public void onSyncTaskStateChanged(@NonNull SyncTask syncTask, @NonNull Account account) {
			this.changedIds.add(syncTask.getId());
			this.changedStates.add(syncTask.getState());
		}
	}

	private static class TestSyncHandler extends SyncHandler<SyncTask.EmptyRequest, Void> {

		int onHandleSyncInvokedTimes;
		Exception exceptionToThrow;

		TestSyncHandler(int taskId) {
			super(taskId);
		}

		TestSyncHandler(int taskId, @Nullable Class<SyncTask.EmptyRequest> classOfRequest) {
			super(taskId, classOfRequest);
		}

		@Nullable
		@Override
		protected Void onHandleSync(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable SyncTask.EmptyRequest syncRequest) throws Exception {
			this.onHandleSyncInvokedTimes++;
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
			return null;
		}

		@Override
		protected void onSyncError(@NonNull Context context, @NonNull SyncOperation syncOperation, @Nullable SyncTask.EmptyRequest syncRequest, @NonNull Exception error) {
			super.onSyncError(context, syncOperation, syncRequest, error);
			throw new TestException();
		}
	}

	private static class TestException extends RuntimeException {
	}
}
