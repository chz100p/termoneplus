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

package com.termoneplus.utils;

import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import com.termoneplus.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import androidx.appcompat.app.AppCompatActivity;
import jackpal.androidterm.emulatorview.TermSession;


public class ScriptImporter {

    public static void paste(AppCompatActivity activity, Uri uri, TermSession session) {
        if (uri == null) return;

        new Thread(() -> {
            try {
                InputStream inraw = activity.getContentResolver().openInputStream(uri);
                if (inraw == null) throw new IOException("null script input stream");

                /* Android note: The Android platform default is always UTF-8. */
                Charset cs = Charset.defaultCharset();
                CharsetDecoder csdec = cs.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);
                InputStreamReader in = new InputStreamReader(inraw, csdec);

                OutputStreamWriter out = new OutputStreamWriter(session.getTermOut());

                char[] buf = new char[2048];
                while (true) {
                    int count = in.read(buf, 0, buf.length);
                    if (count < 0) break;
                    out.write(buf, 0, count);
                }
                out.flush();
            } catch (IOException e) {
                activity.runOnUiThread(() -> {
                    Toast toast = Toast.makeText(activity.getApplicationContext(),
                            R.string.script_import_error, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                });
                e.printStackTrace();
            }
        }).start();
    }
}
