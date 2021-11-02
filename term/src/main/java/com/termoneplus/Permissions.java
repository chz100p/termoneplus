/*
 * Copyright (C) 2018-2021 Roumen Petrov.  All rights reserved.
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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class Permissions {
    /* MANAGE_EXTERNAL_STORAGE permission:
     * - https://stackoverflow.com/questions/65876736/how-do-you-request-manage-external-storage-permission-in-android
     * - https://developer.android.com/training/data-storage/manage-all-files
     */
    public static final int REQUEST_EXTERNAL_STORAGE = 101;

    static String[] external_storage_permissions = null;


    private static void constructExternalStoragePermissions() {
        if (external_storage_permissions != null) return;

        ArrayList<String> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN /*API Level 16*/) {
            // added in API level 16
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        external_storage_permissions = list.toArray(new String[0]);
    }

    @RequiresApi(30)
    private static void requestPermissionAllFilesAccess(AppCompatActivity activity) {
        try {
            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
            //  startActivityForResult does not work here
            activity.startActivity(intent);
            return;
        } catch (Exception ignore) {
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activity.startActivity(intent);
        } catch (Exception ignore) {
        }
    }

    public static boolean permissionExternalStorage(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R /*API Level 30*/)
            return Environment.isExternalStorageManager();

        constructExternalStoragePermissions();
        for (String permission : external_storage_permissions) {
            int status = ActivityCompat.checkSelfPermission(activity, permission);
            if (status != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean shouldShowExternalStorageRationale(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R /*API Level 30*/)
            return true;

        constructExternalStoragePermissions();
        for (String permission : external_storage_permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        }
        return false;
    }

    public static void requestPermissionExternalStorage(AppCompatActivity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R /*API Level 30*/) {
            requestPermissionAllFilesAccess(activity);
            return;
        }
        ActivityCompat.requestPermissions(activity, external_storage_permissions, requestCode);
    }

    public static boolean isPermissionGranted(int[] grantResults) {
        // Note if request is cancelled, the result arrays are empty.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return grantResults.length > 0; // i.e. false by default
    }
}
