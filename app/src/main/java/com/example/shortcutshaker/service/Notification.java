package com.example.shortcutshaker.service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class Notification extends Application {

    /**
     * Create channel ID to link Notification to Notification Manager
     */
    public static final String SERVICE_CHANNEL_ID = "serviceChannel";

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     */
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    } //End of onCreate

    /**
     * Creates a notification channel to run the notification required for a foreground service
     */
    private void createNotificationChannel() {
        //Creates new notification channel for the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    SERVICE_CHANNEL_ID,
                    "Shortcut Shaker",
                    NotificationManager.IMPORTANCE_HIGH
            );

            //Creates notification manager to notify the user of any notifications for the app
            NotificationManager manager = getSystemService(NotificationManager.class);

            //Adds notification channels with notification to the notification manager
            manager.createNotificationChannel(serviceChannel);
        }
    } //End of createNotificationChannel
}
