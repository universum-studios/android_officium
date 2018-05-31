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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class BaseSyncAdapterTest extends RobolectricTestCase {

	@Test public void testEventDispatcher() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final BaseSyncAdapter.EventDispatcher mockDispatcher = mock(BaseSyncAdapter.EventDispatcher.class);
		// Act + Assert:
		adapter.setEventDispatcher(mockDispatcher);
		assertThat(adapter.getEventDispatcher(), is(mockDispatcher));
	}

	@Test public void testGlobalSyncHandler() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final SyncHandler mockHandler = mock(SyncHandler.class);
		// Act + Assert:
		adapter.setGlobalSyncHandler(mockHandler);
		assertThat(adapter.getGlobalSyncHandler(), is(mockHandler));
	}

	@Test public void testRegisterTaskHandler() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(12);
		// Act:
		adapter.registerTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		// Assert:
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
	}

	@Test public void testRegisterTaskHandlerWhenAlreadyRegistered() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(12);
		// Act:
		adapter.registerTaskHandler(handler);
		adapter.registerTaskHandler(handler);
		adapter.registerTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		// Assert:
		assertThat(handler.onHandleSyncInvokedTimes, is(1));
	}

	@Test public void testUnregisterTaskHandler() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(12);
		adapter.registerTaskHandler(handler);
		// Act:
		adapter.unregisterTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		// Assert:
		assertThat(handler.onHandleSyncInvokedTimes, is(0));
	}

	@Test public void testUnregisterTaskHandlerWhenAlreadyUnregistered() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(12);
		adapter.registerTaskHandler(handler);
		// Act:
		adapter.unregisterTaskHandler(handler);
		adapter.unregisterTaskHandler(handler);
		adapter.onPerformSync(createSyncOperation(12));
		// Assert:
		assertThat(handler.onHandleSyncInvokedTimes, is(0));
	}

	@Test public void testUnregisterTaskHandlerWhenNotRegistered() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		// Act:
		adapter.unregisterTaskHandler(new TestSyncHandler(12));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testOnPerformSyncAsGlobal() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final Account account = new Account("TestName", "TestType");
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, SyncTask.DEFAULT_ID);
		final String authority = "test.authority";
		// Act:
		adapter.onPerformSync(account, extras, authority, null, null);
		// Assert:
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

	@SuppressWarnings("ConstantConditions")
	@Test public void testOnPerformSyncAsSecondary() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final Account account = new Account("TestName", "TestType");
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 12);
		final String authority = "test.authority";
		// Act:
		adapter.onPerformSync(account, extras, authority, null, null);
		// Assert:
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

	@Test public void testCreateTaskFromExtras() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 3);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, "{count: 15}");
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, SyncTask.CANCELED);
		// Act:
		final SyncTask task = adapter.createTaskFromExtras(extras);
		// Assert:
		assertThat(task, is(new SyncTask(extras)));
		assertThat(task.getState(), is(SyncTask.CANCELED));
	}

	@Test public void testOnPerformGlobalSyncForOperation() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(SyncTask.DEFAULT_ID);
		adapter.setGlobalSyncHandler(handler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		// Act + Assert:
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

	@Test public void testOnPerformGlobalSyncForOperationWhichFails() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(SyncTask.DEFAULT_ID);
		handler.exceptionToThrow = new IllegalStateException();
		adapter.setGlobalSyncHandler(handler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(SyncTask.DEFAULT_ID);
		// Act:
		adapter.onPerformGlobalSync(operation);
		// Assert:
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

	@Test public void testOnPerformGlobalSyncForOperationWithoutRegisteredHandler() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(SyncTask.DEFAULT_ID);
		// Act:
		adapter.onPerformGlobalSync(operation);
		// Assert:
		assertThat(adapter.onGlobalSyncFinishedInvoked, is(false));
		assertThat(adapter.onGlobalSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedStates.size(), is(0));
		assertThat(adapter.dispatchEvents.size(), is(0));
	}

	@Test public void testOnGlobalSyncFinished() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		adapter.onGlobalSyncFinished(syncOperation);
		assertThat(dispatcher.dispatchEvent, is(notNullValue()));
		// Act:
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		// Assert:
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FINISH));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, nullValue());
	}

	@Test public void testOnGlobalSyncFailed() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		// Act:
		adapter.onGlobalSyncFailed(syncOperation, new IllegalStateException());
		// Assert:
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event, is(notNullValue()));
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FAILURE));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, instanceOf(IllegalStateException.class));
	}

	@Test public void testOnPerformSyncForOperation() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler firstHandler = new TestSyncHandler(11);
		final TestSyncHandler secondHandler = new TestSyncHandler(12);
		adapter.registerTaskHandler(firstHandler);
		adapter.registerTaskHandler(secondHandler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		// Act + Assert:
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

	@Test public void testOnPerformSyncForOperationWhichFails() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestSyncHandler handler = new TestSyncHandler(12);
		handler.exceptionToThrow = new IllegalStateException();
		adapter.registerTaskHandler(handler);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(12);
		// Act:
		adapter.onPerformSync(operation);
		// Assert:
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

	@Test public void testOnPerformSyncForOperationWithoutRegisteredHandler() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestTaskStateChangeListener taskStateChangeListener = new TestTaskStateChangeListener();
		adapter.setOnTaskStateChangeListener(taskStateChangeListener);
		final SyncOperation operation = createSyncOperation(12);
		// Act:
		adapter.onPerformSync(operation);
		// Assert:
		assertThat(adapter.onSyncFinishedInvoked, is(false));
		assertThat(adapter.onSyncFailedInvoked, is(false));
		assertThat(taskStateChangeListener.changedStates.size(), is(0));
		assertThat(adapter.dispatchEvents.size(), is(0));
	}

	@Test public void testOnSyncFinished() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		// Act:
		adapter.onSyncFinished(syncOperation);
		// Assert:
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event, is(notNullValue()));
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FINISH));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, nullValue());
	}

	@Test public void testOnSyncFailed() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final TestEventDispatcher dispatcher = new TestEventDispatcher();
		adapter.setEventDispatcher(dispatcher);
		final SyncOperation syncOperation = createSyncOperation(1);
		// Act:
		adapter.onSyncFailed(syncOperation, new IllegalStateException());
		// Assert:
		final SyncEvent event = (SyncEvent) dispatcher.dispatchEvent;
		assertThat(event, is(notNullValue()));
		assertThat(event.taskId, is(1));
		assertThat(event.type, is(SyncEvent.FAILURE));
		assertThat(event.account, is(syncOperation.account));
		assertThat(event.error, instanceOf(IllegalStateException.class));
	}

	@Test public void testChangeTaskStateToAndNotify() {
		// Arrange:
		final OnSyncTaskStateChangeListener mockListener = mock(OnSyncTaskStateChangeListener.class);
		final TestAdapter adapter = new TestAdapter(application);
		adapter.setOnTaskStateChangeListener(mockListener);
		final SyncOperation syncOperation = createSyncOperation(1);
		// Act:
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		// Assert:
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
		verify(mockListener).onSyncTaskStateChanged(syncOperation.task, syncOperation.account);
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testChangeTaskStateToAndNotifyWithoutListener() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final SyncOperation syncOperation = createSyncOperation(1);
		// Act + Assert:
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
		adapter.changeTaskStateToAndNotify(syncOperation, SyncTask.PENDING);
		assertThat(syncOperation.task.getState(), is(SyncTask.PENDING));
	}

	@Test public void testDispatchSyncEvent() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		final BaseSyncAdapter.EventDispatcher mockDispatcher = mock(BaseSyncAdapter.EventDispatcher.class);
		adapter.setEventDispatcher(mockDispatcher);
		// Act:
		adapter.dispatchSyncEvent("Event");
		// Assert:
		verify(mockDispatcher).dispatch("Event");
		verifyNoMoreInteractions(mockDispatcher);
	}

	@Test public void testDispatchSyncEventWithoutDispatcher() {
		// Arrange:
		final TestAdapter adapter = new TestAdapter(application);
		// Act:
		adapter.dispatchSyncEvent("Event");
	}

	private static SyncOperation createSyncOperation(final int taskId) {
		return new SyncOperation.Builder()
				.account(new Account("TestName", "TestType"))
				.authority("test.authority")
				.task(new SyncTask.Builder<>(taskId).build())
				.build();
	}

	private static void assertThatEventCorrespondsWithOperation(final SyncEvent event, final SyncOperation operation) {
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

		TestAdapter(@NonNull final Context context) {
			super(context.getApplicationContext(), true);
		}

		@Override protected void onPerformGlobalSync(@NonNull final SyncOperation syncOperation) {
			super.onPerformGlobalSync(syncOperation);
			this.onPerformGlobalSyncInvoked = true;
			this.onPerformGlobalSyncOperation = syncOperation;
		}

		@Override protected void onGlobalSyncFinished(@NonNull final SyncOperation syncOperation) {
			super.onGlobalSyncFinished(syncOperation);
			this.onGlobalSyncFinishedInvoked = true;
			this.onGlobalSyncFinishedOperation = syncOperation;
		}

		@Override protected void onGlobalSyncFailed(@NonNull final SyncOperation syncOperation, @NonNull final Exception error) {
			super.onGlobalSyncFailed(syncOperation, error);
			this.onGlobalSyncFailedInvoked = true;
			this.onGlobalSyncFailedError = error;
		}

		@Override protected void onPerformSync(@NonNull final SyncOperation syncOperation) {
			super.onPerformSync(syncOperation);
			this.onPerformSyncInvoked = true;
			this.onPerformSyncOperation = syncOperation;
		}

		@Override protected void onSyncFinished(@NonNull final SyncOperation syncOperation) {
			super.onSyncFinished(syncOperation);
			this.onSyncFinishedInvoked = true;
			this.onSyncFinishedOperation = syncOperation;
		}

		@Override protected void onSyncFailed(@NonNull final SyncOperation syncOperation, @NonNull final Exception error) {
			super.onSyncFailed(syncOperation, error);
			this.onSyncFailedInvoked = true;
			this.onSyncFailedError = error;
		}

		@Override protected void dispatchSyncEvent(@NonNull final Object event) {
			super.dispatchSyncEvent(event);
			this.dispatchEvents.add(event);
		}
	}

	private static class TestEventDispatcher implements BaseSyncAdapter.EventDispatcher {

		Object dispatchEvent;

		@Override public void dispatch(@NonNull final Object event) {
			this.dispatchEvent = event;
		}
	}

	private static class TestTaskStateChangeListener implements OnSyncTaskStateChangeListener {

		final List<Integer> changedIds = new ArrayList<>(2);
		final List<Integer> changedStates = new ArrayList<>();

		@Override public void onSyncTaskStateChanged(@NonNull final SyncTask syncTask, @NonNull final Account account) {
			this.changedIds.add(syncTask.getId());
			this.changedStates.add(syncTask.getState());
		}
	}

	private static class TestSyncHandler extends SyncHandler<SyncTask.EmptyRequest, Void> {

		int onHandleSyncInvokedTimes;
		Exception exceptionToThrow;

		TestSyncHandler(final int taskId) {
			super(taskId);
		}

		@Override @Nullable protected Void onHandleSync(
				@NonNull final Context context,
				@NonNull final SyncOperation syncOperation,
				@Nullable final SyncTask.EmptyRequest syncRequest
		) throws Exception {
			this.onHandleSyncInvokedTimes++;
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
			return null;
		}

		@Override protected void onSyncError(
				@NonNull final Context context,
				@NonNull final SyncOperation syncOperation,
				@Nullable final SyncTask.EmptyRequest syncRequest,
				@NonNull final Exception error
		) {
			super.onSyncError(context, syncOperation, syncRequest, error);
			throw new TestException();
		}
	}

	private static class TestException extends RuntimeException {}
}