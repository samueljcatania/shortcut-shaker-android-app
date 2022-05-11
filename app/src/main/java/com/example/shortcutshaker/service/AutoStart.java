package com.example.shortcutshaker.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AutoStart extends BroadcastReceiver {

    /**
     * Automatically starts foreground service when phone restarts
     *
     * @param context Interface to global information about an application environment
     * @param arg1 Passive data structure holding an abstract description of an action to be performed
     */
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    public void onReceive(Context context, Intent arg1) {

        Intent intent = new Intent(context, ForegroundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.startForegroundService(intent);
        } else {

            context.startService(intent);
        }
    }//End of OnReceive
}//End of AutoStart