/*
 * Copyright (C) 2022 Roumen Petrov.  All rights reserved.
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

package com.termoneplus.compat;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class SoftInputCompat {

    public static void toggle(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // TODO: to use only API 31-33 compatible code
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S /*API level 31*/)
            Compat.toggle(imm);
        else
            Compat31.toggle(view, imm);
    }

    private static boolean isSoftInputVisible(View view) {
        View root = view.getRootView();

        Rect r = new Rect();
        root.getWindowVisibleDisplayFrame(r);
        /* "root view" height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        int delta = root.getBottom() - r.bottom;

        /* threshold size: dp to pixels, multiply with display density */
        DisplayMetrics metrics = root.getResources().getDisplayMetrics();
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        return delta > 128 /*threshold*/ * metrics.density;
    }


    private static class Compat {
        @SuppressWarnings({"deprecation", "RedundantSuppression"})
        private static void toggle(InputMethodManager imm) {
            // Toggle method was deprecated in API level 31.
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private static class Compat31 {
        private static void toggle(View view, InputMethodManager imm) {
            if (isSoftInputVisible(view)) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else {
                if (!view.requestFocus())
                    return;
                // NOTE: SHOW_FORCED was deprecated in API level 33.
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
