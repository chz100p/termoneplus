/*
 * Copyright (C) 2021 Roumen Petrov.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.termoneplus;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


@RequiresApi(30)
public class PermissionManageExternal {
    /* MANAGE_EXTERNAL_STORAGE permission:
     * - https://stackoverflow.com/questions/65876736/how-do-you-request-manage-external-storage-permission-in-android
     * - https://developer.android.com/training/data-storage/manage-all-files
     * Remark: Looks like there is no way native file management to pass Google policy!
     */

    public static boolean isGranted() {
        return Environment.isExternalStorageManager();
    }

    public static boolean request(AppCompatActivity activity) {
        try {
            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
            // startActivityForResult does not work here
            activity.startActivity(intent);
            return true;
        } catch (Exception ignore) {
        }
        try {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activity.startActivity(intent);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }
}
