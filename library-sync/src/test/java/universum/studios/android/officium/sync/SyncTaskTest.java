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

import android.os.Bundle;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class SyncTaskTest extends RobolectricTestCase {

	@Test public void testStates() {
		// Act + Assert:
		assertThat(SyncTask.IDLE, is(0));
		assertThat(SyncTask.PENDING, is(SyncTask.IDLE + 1));
		assertThat(SyncTask.RUNNING, is(SyncTask.PENDING + 1));
		assertThat(SyncTask.FINISHED, is(SyncTask.RUNNING + 1));
		assertThat(SyncTask.FAILED, is(SyncTask.FINISHED + 1));
		assertThat(SyncTask.CANCELED, is(SyncTask.FAILED + 1));
	}

	@Test public void testDefaultId() {
		// Act + Assert:
		assertThat(SyncTask.DEFAULT_ID, is(0));
	}

	@Test(expected = IllegalAccessException.class)
	public void testEmptyRequestInstantiation() throws Exception {
		// Act:
		SyncTask.EmptyRequest.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testEmptyRequestInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<SyncTask.EmptyRequest> constructor = SyncTask.EmptyRequest.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testEmpty() {
		// Act + Assert:
		assertThat(SyncTask.EMPTY, is(notNullValue()));
	}

	@Test public void testInstantiation() {
		// Arrange:
		final TestRequest request = new TestRequest();
		request.count = 10;
		// Act:
		final SyncTask<TestRequest> task = new SyncTask.Builder<TestRequest>(1).request(request).build();
		// Assert:
		assertThat(task, is(notNullValue()));
		assertThat(task.getId(), is(1));
		assertThat(task.getState(), is(SyncTask.IDLE));
		assertThat(task.getRequestBody(), is("{\"count\":10}"));
		assertThat(task.getRequest(TestRequest.class), Is.<SyncTask.Request>is(request));
	}

	@Test public void testInstantiationWithoutRequest() {
		// Act:
		final SyncTask<TestRequest> task = new SyncTask.Builder<TestRequest>(1).build();
		// Assert:
		assertThat(task, is(notNullValue()));
		assertThat(task.getId(), is(1));
		assertThat(task.getState(), is(SyncTask.IDLE));
		assertThat(task.getRequestBody(), is(nullValue()));
		assertThat(task.getRequest(TestRequest.class), is(nullValue()));
	}

	@Test public void testInstantiationFromExtras() {
		// Arrange:
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 5);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, "{\"count\":15}");
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, SyncTask.CANCELED);
		// Act:
		final SyncTask<TestRequest> task = new SyncTask<>(extras);
		// Assert:
		assertThat(task.getId(), is(5));
		assertThat(task.getState(), is(SyncTask.CANCELED));
		assertThat(task.getRequestBody(), is("{\"count\":15}"));
		assertThat(task.getRequest(TestRequest.class), is(notNullValue()));
	}

	@Test public void testInstantiationFromOther() {
		// Arrange:
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 5);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, "{\"count\":15}");
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, SyncTask.CANCELED);
		final SyncTask otherTask = new SyncTask(extras);
		otherTask.setState(SyncTask.RUNNING);
		// Act:
		final SyncTask task = new SyncTask(otherTask);
		// Assert:
		assertThat(task, is(otherTask));
		assertThat(task.getState(), is(SyncTask.RUNNING));
	}

	@Test public void testState() {
		// Arrange:
		final SyncTask<TestRequest> task = new SyncTask.Builder<TestRequest>(1).build();
		// Act:
		task.setState(SyncTask.PENDING);
		// Assert:
		assertThat(task.getState(), is(SyncTask.PENDING));
	}

	@Test public void testIntoExtras() {
		// Arrange:
		final TestRequest request = new TestRequest();
		request.count = 15;
		final SyncTask<TestRequest> task = new SyncTask.Builder<TestRequest>(1).request(request).build();
		task.setState(SyncTask.PENDING);
		final Bundle extras = new Bundle();
		// Act:
		task.intoExtras(extras);
		// Assert:
		assertThat(extras.size(), is(3));
		assertThat(extras.getInt(SyncExtras.EXTRA_TASK_ID, -1), is(1));
		assertThat(extras.getInt(SyncExtras.EXTRA_TASK_STATE, -1), is(SyncTask.PENDING));
		assertThat(extras.getString(SyncExtras.EXTRA_TASK_REQUEST_BODY), is("{\"count\":15}"));
	}

	@Test public void testHashCode() {
		// Arrange:
		final Set<SyncTask> tasks = new HashSet<>();
		// Act:
		tasks.add(createTaskWithRequest(createRequestWithCount(1)));
		tasks.add(createTaskWithRequest(createRequestWithCount(1)));
		tasks.add(createTaskWithRequest(createRequestWithCount(2)));
		tasks.add(createTaskWithRequest(createRequestWithCount(3)));
		tasks.add(createTaskWithRequest(createRequestWithCount(4)));
		tasks.add(createTaskWithRequest(createRequestWithCount(4)));
		tasks.add(createTaskWithRequest(createRequestWithCount(4)));
		tasks.add(createTaskWithRequest(null));
		// Assert:
		assertThat(tasks.size(), is(5));
	}

	@Test public void testEquals() {
		// Arrange:
		final SyncTask first = new SyncTask.Builder<TestRequest>(1).build();
		final SyncTask second = new SyncTask.Builder<TestRequest>(1).build();
		// Act + Assert:
		assertThat(first.equals(second), is(true));
	}

	@SuppressWarnings("EqualsWithItself")
	@Test public void testEqualsWithSameInstances() {
		// Arrange:
		final SyncTask task = new SyncTask.Builder<TestRequest>(1).build();
		// Act + Assert:
		assertThat(task.equals(task), is(true));
	}

	@SuppressWarnings("EqualsBetweenInconvertibleTypes")
	@Test public void testEqualsWithDifferentTypes() {
		// Arrange:
		final SyncTask task = new SyncTask.Builder<TestRequest>(1).build();
		// Act + Assert:
		assertThat(task.equals("Nope"), is(false));
	}

	@Test public void testEqualsWithDifferentIds() {
		// Arrange:
		final SyncTask first = new SyncTask.Builder<TestRequest>(1).build();
		final SyncTask second = new SyncTask.Builder<TestRequest>(2).build();
		// Act + Assert:
		assertThat(first.equals(second), is(false));
	}

	@Test public void testEqualsWithRequests() {
		// Arrange:
		final SyncTask first = new SyncTask.Builder<TestRequest>(1).request(createRequestWithCount(0)).build();
		final SyncTask second = new SyncTask.Builder<TestRequest>(1).request(createRequestWithCount(0)).build();
		// Act + Assert:
		assertThat(first.equals(second), is(true));
	}

	@Test public void testEqualsWithDifferentRequests() {
		// Arrange:
		final SyncTask first = new SyncTask.Builder<TestRequest>(1).request(createRequestWithCount(0)).build();
		final SyncTask second = new SyncTask.Builder<TestRequest>(1).request(createRequestWithCount(1)).build();
		// Act + Assert:
		assertThat(first.equals(second), is(false));
	}

	@Test public void testToString() {
		// Arrange:
		final TestRequest request = new TestRequest();
		request.count = 10;
		final SyncTask<TestRequest> task = new SyncTask.Builder<TestRequest>(1).request(request).build();
		task.setState(SyncTask.CANCELED);
		// Act + Assert:
		assertThat(task.toString(), is("SyncTask{id: 1, state: CANCELED, request: {\"count\":10}}"));
	}

	@Test public void testGetStateName() {
		// Act + Assert:
		assertThat(SyncTask.getStateName(SyncTask.IDLE), is("IDLE"));
		assertThat(SyncTask.getStateName(SyncTask.PENDING), is("PENDING"));
		assertThat(SyncTask.getStateName(SyncTask.RUNNING), is("RUNNING"));
		assertThat(SyncTask.getStateName(SyncTask.FINISHED), is("FINISHED"));
		assertThat(SyncTask.getStateName(SyncTask.FAILED), is("FAILED"));
		assertThat(SyncTask.getStateName(SyncTask.CANCELED), is("CANCELED"));
		assertThat(SyncTask.getStateName(-1), is("UNKNOWN"));
	}

	@Test public void testToStringWithRequestBody() {
		// Arrange:
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 5);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, "{\"count\":15}");
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, SyncTask.CANCELED);
		final SyncTask<TestRequest> task = new SyncTask<>(extras);
		// Act + Assert:
		assertThat(task.toString(), is("SyncTask{id: 5, state: CANCELED, request: {\"count\":15}}"));
	}

	@Test public void testClone() {
		// Arrange:
		final Bundle extras = new Bundle();
		extras.putInt(SyncExtras.EXTRA_TASK_ID, 5);
		extras.putString(SyncExtras.EXTRA_TASK_REQUEST_BODY, "{\"count\":15}");
		extras.putInt(SyncExtras.EXTRA_TASK_STATE, SyncTask.CANCELED);
		final SyncTask<TestRequest> task = new SyncTask<>(extras);
		// Act:
		final SyncTask<TestRequest> taskClone = task.clone();
		// Assert:
		assertThat(taskClone, is(notNullValue()));
		assertThat(taskClone, is(task));
		assertThat(taskClone.getState(), is(SyncTask.IDLE));
	}

	private static SyncTask<TestRequest> createTaskWithRequest(final TestRequest request) {
		return new SyncTask.Builder<TestRequest>(1).request(request).build();
	}

	private static TestRequest createRequestWithCount(final int count) {
		final TestRequest request = new TestRequest();
		request.count = count;
		return request;
	}

	public static final class TestRequest implements SyncTask.Request {

		int count;
	}
}