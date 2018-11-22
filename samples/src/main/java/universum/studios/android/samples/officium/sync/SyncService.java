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
package universum.studios.android.samples.officium.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

/**
 * @author Martin Albedinsky
 */
public final class SyncService extends Service {

    private static final Object LOCK = new Object();
    private static SyncAdapter syncAdapter;

    @Override public void onCreate() {
        super.onCreate();
        synchronized (LOCK) {
            if (syncAdapter == null) syncAdapter = new SyncAdapter(getApplicationContext());
        }
    }

    @Override public IBinder onBind(@NonNull final Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}