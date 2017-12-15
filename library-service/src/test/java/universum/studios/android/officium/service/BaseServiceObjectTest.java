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
package universum.studios.android.officium.service; 
import org.junit.Test;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
public final class BaseServiceObjectTest extends LocalTestCase {
    
	@Test
	public void testAssociateWith() {
		final TestObject object = new TestObject();
		BaseServiceObject.associateWith(object, 1, "2");
		assertThat(object.getServiceId(), is(1));
		assertThat(object.getRequestId(), is("2"));
	}

	@Test
	public void testAssociateWithWhenAlreadyAssociated() {
		final TestObject object = new TestObject();
		BaseServiceObject.associateWith(object, 1, "2");
		BaseServiceObject.associateWith(object, 3, "4");
		assertThat(object.getServiceId(), is(1));
		assertThat(object.getRequestId(), is("2"));
	}

	@Test
	public void testAssociateWithNullValues() {
		final TestObject object = new TestObject();
		BaseServiceObject.associateWith(object, null, null);
		assertThat(object.getServiceId(), is(BaseServiceObject.NO_SERVICE));
		assertThat(object.getRequestId(), is(BaseServiceObject.NO_REQUEST));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetServiceIdWhenAlreadyAssociated() {
		final TestObject object = new TestObject();
		BaseServiceObject.associateWith(object, 1, "2");
		object.setServiceId(3);
	}

    @Test
	public void testGetServiceIdDefault() {
	    assertThat(new TestObject().getServiceId(), is(BaseServiceObject.NO_SERVICE));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetRequestIdWhenAlreadyAssociated() {
		final TestObject object = new TestObject();
		BaseServiceObject.associateWith(object, 1, "2");
		object.setRequestId("4");
	}

    @Test
	public void testGetRequestIdDefault() {
	    assertThat(new TestObject().getRequestId(), is(BaseServiceObject.NO_REQUEST));
	}

	private static final class TestObject extends BaseServiceObject {
	}
}
