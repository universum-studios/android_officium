/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
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
package universum.studios.android.test.instrumented;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Utility class for instrumented tests which provides methods that may be used to access test resources.
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("unused")
public final class TestResources {

	/**
	 * Constant that identifies no resource identifier.
	 */
	public static final int NO_RESOURCE = 0;

	/**
	 * Type identifying <b>animation</b> resource which may be accessed as {@code R.anim.RESOURCE_NAME}.
	 */
	public static final String ANIMATION = "anim";

	/**
	 * Type identifying <b>animator</b> resource which may be accessed as {@code R.animator.RESOURCE_NAME}.
	 */
	public static final String ANIMATOR = "animator";

	/**
	 * Type identifying <b>attribute</b> resource which may be accessed as {@code R.attr.RESOURCE_NAME}.
	 */
	public static final String ATTRIBUTE = "attr";

	/**
	 * Type identifying <b>boolean</b> resource which may be accessed as {@code R.bool.RESOURCE_NAME}.
	 */
	public static final String BOOL = "bool";

	/**
	 * Type identifying <b>color</b> resource which may be accessed as {@code R.color.RESOURCE_NAME}.
	 */
	public static final String COLOR = "color";

	/**
	 * Type identifying <b>dimension</b> resource which may be accessed as {@code R.dimen.RESOURCE_NAME}.
	 */
	public static final String DIMENSION = "dimen";

	/**
	 * Type identifying <b>drawable</b> resource which may be accessed as {@code R.drawable.RESOURCE_NAME}.
	 */
	public static final String DRAWABLE = "drawable";

	/**
	 * Type identifying <b>integer</b> resource which may be accessed as {@code R.integer.RESOURCE_NAME}.
	 */
	public static final String INTEGER = "integer";

	/**
	 * Type identifying <b>layout</b> resource which may be accessed as {@code R.layout.RESOURCE_NAME}.
	 */
	public static final String LAYOUT = "layout";

	/**
	 * Type identifying <b>menu</b> resource which may be accessed as {@code R.menu.RESOURCE_NAME}.
	 */
	public static final String MENU = "menu";

	/**
	 * Type identifying <b>string</b> resource which may be accessed as {@code R.string.RESOURCE_NAME}.
	 */
	public static final String STRING = "string";

	/**
	 * Type identifying <b>style</b> resource which may be accessed as {@code R.style.RESOURCE_NAME}.
	 */
	public static final String STYLE = "style";

	/**
	 * Type identifying <b>transition</b> resource which may be accessed as {@code R.transition.RESOURCE_NAME}.
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
	@interface ResourceType {}

	/**
	 */
	private TestResources() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/**
	 * Resolves identifier for the resource with the specified <var>resourceName</var> and of the
	 * specified <var>resourceType</var>.
	 *
	 * @param context      Context used to access resources that are used to resolve the requested identifier.
	 * @param resourceType Type of the resource for which to resolve its identifier.
	 * @param resourceName Name of the resource for which to resolve its identifier.
	 * @return Resolved identifier which may be used to obtain value of the desired resource from
	 * resources or {@link #NO_RESOURCE} if no such resource was found.
	 *
	 * @see Resources#getIdentifier(String, String, String)
	 */
	public static int resourceIdentifier(@NonNull final Context context, @ResourceType final String resourceType, @NonNull final String resourceName) {
		return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
	}
}