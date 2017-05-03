package com.tangshan.gui.util;

import android.util.Log;

import com.tangshan.gui.BuildConfig;

public class CMLog {

    public static void i(String tag, String msg) {
        if (BuildConfig.LogDebug) {
            Log.i("CM " + tag, msg);
        }
    }
}
