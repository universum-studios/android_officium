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
package universum.studios.android.test;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility class for tests that provides methods for accessing test resources.
 *
 * @author Martin Albedinsky
 */
public final class TestResources {

	/**
	 * Resource type indicating <b>animation</b> resource which may be accessed as {@code R.anim.RESOURCE_NAME}.
	 */
	public static final String ANIMATION = "anim";

	/**
	 * Resource type indicating <b>animator</b> resource which may be accessed as {@code R.animator.RESOURCE_NAME}.
	 */
	public static final String ANIMATOR = "animator";

	/**
	 * Resource type indicating <b>attribute</b> resource which may be accessed as {@code R.attr.RESOURCE_NAME}.
	 */
	public static final String ATTRIBUTE = "attr";

	/**
	 * Resource type indicating <b>boolean</b> resource which may be accessed as {@code R.bool.RESOURCE_NAME}.
	 */
	public static final String BOOL = "bool";

	/**
	 * Resource type indicating <b>color</b> resource which may be accessed via {@code R.color.RESOURCE_NAME}.
	 */
	public static final String COLOR = "color";

	/**
	 * Resource type indicating <b>dimension</b> resource which may be accessed via {@code R.dimen.RESOURCE_NAME}.
	 */
	public static final String DIMENSION = "dimen";

	/**
	 * Resource type indicating <b>drawable</b> resource which may be accessed via {@code R.drawable.RESOURCE_NAME}.
	 */
	public static final String DRAWABLE = "drawable";

	/**
	 * Resource type indicating <b>integer</b> resource which may be accessed via {@code R.integer.RESOURCE_NAME}.
	 */
	public static final String INTEGER = "integer";

	/**
	 * Resource type indicating <b>layout</b> resource which may be accessed via {@code R.layout.RESOURCE_NAME}.
	 */
	public static final String LAYOUT = "layout";

	/**
	 * Resource type indicating <b>menu</b> resource which may be accessed via {@code R.menu.RESOURCE_NAME}.
	 */
	public static final String MENU = "menu";

	/**
	 * Resource type indicating <b>string</b> resource which may be accessed as {@code R.string.RESOURCE_NAME}.
	 */
	public static final String STRING = "string";

	/**
	 * Resource type indicating <b>style</b> resource which may be accessed via {@code R.style.RESOURCE_NAME}.
	 */
	public static final String STYLE = "style";

	/**
	 * Resource type indicating <b>transition</b> resource which may be accessed via {@code R.transition.RESOURCE_NAME}.
	 */
	public static final String TRANSITION = "transition";

	/**
	 * Defines an annotation for determining set of allowed resource types for {@link #resourceIdentifier(Context, String, String)}.
	 */
	@StringDef({
			ANIMATION,
			ANIMATOR,
			ATTRIBUTE,
			BOOL,
			COLOR,
			DRAWABLE,
			DIMENSION,
			INTEGER,
			LAYOUT,
			MENU,
			STRING,
			STYLE,
			TRANSITION
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ResourceType {
	}

	/**
	 */
	private TestResources() {
		// Not allowed to be instantiated publicly.
	}

	/**
	 * Resolves identifier for the resource with the specified <var>resourceName</var> and of the
	 * specified <var>resourceType</var>.
	 *
	 * @param context      Context used to access resources that are used to resolver the requested
	 *                     identifier.
	 * @param resourceType Type of the resource to resolve its identifier for.
	 * @param resourceName Name of the resource to resolve its identifier for.
	 * @return Resolved identifier which may be used to obtain value of the desired resource from
	 * resources or {@code 0} if no such resource was found.
	 * @see Resources#getIdentifier(String, String, String)
	 */
	public static int resourceIdentifier(@NonNull Context context, @ResourceType String resourceType, @NonNull String resourceName) {
		return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
	}
}
