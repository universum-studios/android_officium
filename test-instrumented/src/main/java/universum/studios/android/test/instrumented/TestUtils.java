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
package universum.studios.android.test.instrumented;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Utility class for instrumented tests.
 *
 * @author Martin Albedinsky
 */
public final class TestUtils {

	/**
	 * Name of the library's root package.
	 */
	private static final String LIBRARY_ROOT_PACKAGE_NAME = "universum.studios.android.crypto.test";

	/**
	 */
	private TestUtils() {
		// Not allowed to be instantiated publicly.
	}

	/**
	 * Checks whether the given <var>context</var> has package name equal to the root test package
	 * name of the library.
	 *
	 * @param context The context of which package name to check.
	 * @return {@code True} if the context's package name is the same as the library's root one,
	 * {@code false} otherwise.
	 * @see #isLibraryRootPackageName(String)
	 */
	public static boolean hasLibraryRootPackageName(@NonNull Context context) {
		return isLibraryRootPackageName(context.getPackageName());
	}

	/**
	 * Checks whether the given <var>packageName</var> is equal to the root test package name of
	 * the library.
	 * <p>
	 * <b>Note</b>, that this method will return {@code false} also for library's subpackages.
	 *
	 * @param packageName The package name to check if it is the library's root one.
	 * @return {@code True} if the package name is the same as the library's root one,
	 * {@code false} otherwise.
	 */
	public static boolean isLibraryRootPackageName(@NonNull String packageName) {
		return LIBRARY_ROOT_PACKAGE_NAME.equals(packageName);
	}
}
