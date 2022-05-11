package com.example.shortcutshaker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.example.shortcutshaker.service.ForegroundService.KEY;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

            Log.d("ACTION RECIEVER ___________________", " TURNED OFFFFFFFF");

        String action = intent.getAction();

        Log.d("INTENT INTENT INTENT INTENT___________________", action + " ");

        if (action != null) {
            if (action.equals("on")) {
                    Log.d("ACTION RECIEVER ___________________", " TURNED OFFFFFFFF");
                Log.d("ACTION RECIEVER ___________________", " TURNED Fdfs");
                Log.d("ACTION RECIEVER ___________________", " TURNED OFFdfdsfsFFFFFF");
                Log.d("ACTION RECIEVER ___________________", " TURNED OFFFFsdfsdfsFFFF");

            } else if (action.equals("off")) {
                performAction2();

            }
        }

        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void performAction1() {

    }

    public void performAction2() {



    }

}