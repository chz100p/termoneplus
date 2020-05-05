/*
 * Copyright (C) 2018-2020 Roumen Petrov.  All rights reserved.
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

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import jackpal.androidterm.emulatorview.ColorScheme;


public class Settings {
    // foreground and background as ARGB color pair
    /* Note keep synchronized with names in @array.entries_color_preference
    and index in @array.entryvalues_color_preference. */
    public static final ColorScheme[] color_schemes = {
            new ColorScheme(0xFF000000, 0xFFFFFFFF) /*black on white*/,
            new ColorScheme(0xFFFFFFFF, 0xFF000000) /*white on black*/,
            new ColorScheme(0xFFFFFFFF, 0xFF344EBD) /*white on blue*/,
            new ColorScheme(0xFF00FF00, 0xFF000000) /*green on black*/,
            new ColorScheme(0xFFFFB651, 0xFF000000) /*amber on black*/,
            new ColorScheme(0xFFFF0113, 0xFF000000) /*red on black*/,
            new ColorScheme(0xFF33B5E5, 0xFF000000) /*holo-blue on black*/,
            new ColorScheme(0xFF657B83, 0xFFFDF6E3) /*solarized light*/,
            new ColorScheme(0xFF839496, 0xFF002B36) /*solarized dark*/,
            new ColorScheme(0xFFAAAAAA, 0xFF000000) /*linux console*/,
            new ColorScheme(0xFFDCDCCC, 0xFF2C2C2C) /*dark pastels*/
    };

    private static final String SOURCE_SYS_SHRC_KEY = "source_sys_shrc";

    private boolean source_sys_shrc;


    public Settings(Resources r, SharedPreferences preferences) {
        source_sys_shrc = parseBoolean(preferences, SOURCE_SYS_SHRC_KEY,
                r.getBoolean(R.bool.pref_source_sys_shrc_default));
    }

    public boolean parsePreference(SharedPreferences preferences, String key) {
        if (TextUtils.isEmpty(key)) return true;

        //noinspection SwitchStatementWithTooFewBranches
        switch (key) {
            case SOURCE_SYS_SHRC_KEY:
                boolean value = parseBoolean(preferences, key, source_sys_shrc);
                if (value != source_sys_shrc) {
                    source_sys_shrc = value;
                    Installer.installAppScriptFile();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean sourceSystemShellStartupFile() {
        return source_sys_shrc;
    }

    private boolean parseBoolean(SharedPreferences preferences, String key, boolean def) {
        try {
            return preferences.getBoolean(key, def);
        } catch (Exception ignored) {
        }
        return def;
    }
}
