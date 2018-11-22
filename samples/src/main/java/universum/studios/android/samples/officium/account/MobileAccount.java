/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
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
package universum.studios.android.samples.officium.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.officium.account.UserAccount;

/**
 * @author Martin Albedinsky
 */
public final class MobileAccount extends UserAccount {

	public MobileAccount(@NonNull final String name) {
		this(name, null);
	}

	public MobileAccount(@NonNull final String name, @Nullable final String password) {
		super(name, password);
	}
}