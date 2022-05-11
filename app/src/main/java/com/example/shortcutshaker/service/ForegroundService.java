package com.example.shortcutshaker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.shortcutshaker.main.MainActivity;
import com.example.shortcutshaker.R;

import java.util.Objects;

import static com.example.shortcutshaker.service.Notification.SERVICE_CHANNEL_ID;

public class ForegroundService extends Service {

    /**
     * Controls location of LogCat Output
     */
    private static final String TAG = "MainActivity";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    /**
     * Name of the SharedPreferences variable that tracks the state of the shakes
     */
    public static final String SHAKES_REQUIRED = "Shakes Required";

    /**
     * Name of the SharedPreferences variable that tracks the state of the timeout period
     */
    public static final String TIMEOUT_STATE = "Timeout State";

    /**
     * Name of the SharedPreferences variable that tracks the app chosen
     */
    public static final String APP_STATE = "App State";

    /**
     * Name of the SharedPreferences variable that tracks the state of the shakes
     */
    public static final String SHORTCUT_STATE = "Shortcut State";

    /**
     * Name of the SharedPreferences variable that tracks the state of vibration
     */
    public static final String VIBRATION_STATE = "Vibration State";

    /**
     * Name of the SharedPreferences variable that tracks the state of the hand chosen
     */
    public static final String HAND_STATE = "Hand State";

    /**
     * Message inside the service notification
     */
    private static final String NOTIFICATION_MESSAGE = "Your shakable shortcut is active!";

    public static final String KEY = "key";

    //**********************************************************************************************

    /**
     * Gives program access to phone's physical sensors
     */
    private SensorManager sensorManager;

    /**
     * Gives program access to phone's accelerometer sensor
     */
    private Sensor linearAccelerometer;

    //**********************************************************************************************

    /**
     * Gives program access to phone's vibrating motor
     */
    private Vibrator vibrator;

    //**********************************************************************************************

    /**
     * Gives program access to phone's camera sensors, including flashlight
     */
    private CameraManager cameraManager;

    /**
     * Used to access the phone's flashlight, as the camera has a certain ID which the flashlight
     * is linked to
     */
    private String cameraId;

    /**
     * Used to track whether the phone's flashlight is currently on or off
     */
    private boolean flashlight = false;

    //**********************************************************************************************

    /**
     * Stores the x vector value of the linear accelerometer from any change in the sensor
     */
    private float deltaX = 0;

    /**
     * Stores the y vector value of the linear accelerometer from any change in the sensor
     */
    private float deltaY = 0;

    /**
     * Stores the z vector value of the linear accelerometer from any change in the sensor
     */
    private float deltaZ = 0;

    //**********************************************************************************************

    /**
     * Sets the minimum force threshold the shake must reach to be registered
     */
    public static int forceThreshold = 80;

    /**
     * Sets the maximum amount of time that must pass between each individual shakes
     */
    private static final int timeThreshold = 100;

    /**
     * Sets the maximum amount of time before individual shakes are considered too far apart
     */
    private static final int shakeTimeout = 500;

    /**
     * Sets the amount of shakes required to activate a shortcut
     */
    public static int requiredShakes = 2;

    /**
     * Controls the type of shortcut that is being called
     */
    public static int shortcutFunction = 1;

    /**
     * Controls the app to be opened
     */
    public static String app = "com.example.chrome";

    /**
     * Tracks the amount of time between shakes
     */
    private long timeOfLastShake;

    /**
     * Tracks the amount of shakes in a sequence from the user
     */
    private long shakeCount = 0;

    //**********************************************************************************************

    /**
     * Tracks the amount of time passes since the flashlight turned on, set to -1 if it is off
     */
    private long failSafeStart = -1;

    /**
     * Sets the amount of time allowed to pass before the flashlight is toggled off if left on
     */
    public static int failSafeThreshold = 300;

    /**
     * Controls if vibration is on or off
     */
    public static boolean vibration = true;

    /**
     * Controls if vibration is on or off
     */
    public static int leftHanded = 2;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     */
    @Override
    public void onCreate() {

        super.onCreate();
    } //End of onCreate

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * Context.startService(Intent), providing the arguments it supplied and a unique integer
     * token representing the start request
     *
     * @param intent  Messaging object used to request the start to the service
     * @param flags   Used alongside intent to create the new service
     * @param startId Pass the ID of the requested service
     * @return Behaviour on destruction
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Initializes intent requests for notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        //Basic notification in collapsed form that displays an icon, a title,
        //and a small amount of content text, required to run a foreground service
        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
                .setContentTitle(NOTIFICATION_MESSAGE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();

        //Creates service as foreground activity using notification
        startForeground(1, notification);

        //Creates new thread to run foreground service
        ServiceRunnable runnable = new ServiceRunnable();

        //Create new instance of a external thread to run the service off of the UI thread
        new Thread(runnable).start();

        return START_NOT_STICKY;

    }//End of onStartCommand

    /**
     * Destroys the service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    } //End of onDestroy

    /**
     * Default method used to bind the service to the app activity if neede
     *
     * @param intent Messaging object used to request the start to the service
     * @return Null as it isn't used
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    } //End of onBind

    /**
     * New thread to handle heavy workload of shakable service function
     */
    class ServiceRunnable implements Runnable {

        /**
         * Create sensors and add shakable shortcut function to thread
         */
        @Override
        public void run() {

            //Loads previous user preferences
            loadData();

            //Creates the flashlight sensor
            flashlightActivity();

            //Creates the phone vibrator
            vibratorActivity();

            //Creates the accelerometer sensor and accelerometer methods
            sensorActivity();

        } //End of run


        /**
         * Loads all user input from last time this activity was opened
         */
        private void loadData() {

            SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            requiredShakes = sharedPreferences.getInt(SHAKES_REQUIRED, 2);
            failSafeThreshold = sharedPreferences.getInt(TIMEOUT_STATE, 3) * 60;
            app = sharedPreferences.getString(APP_STATE, "com.example.chrome");
            shortcutFunction = sharedPreferences.getInt(SHORTCUT_STATE, 1);
            vibration = sharedPreferences.getBoolean(VIBRATION_STATE, true);
            leftHanded = sharedPreferences.getInt(HAND_STATE, 2);

        }//End of loadData

        /**
         * Creates the flashlight sensor for the program to access and toggle
         */
        private void flashlightActivity() {

            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            cameraId = null;

        }//End of flashlightActivity Method


        /**
         * Creates the vibrator for the program to access and toggle
         */
        private void vibratorActivity() {

            vibrator = (Vibrator) getSystemService
                    (Context.VIBRATOR_SERVICE);

        } //End of vibratorActivity Method


        /**
         * Creates the linear accelerometer for the program to access
         */
        private void sensorActivity() {

            //Logcat output that the sensor is being created
            Log.d(TAG, "SensorActivity: Creating Sensor Services");

            //Creates new sensorManager to access linear accelerometer
            sensorManager = (SensorManager) getSystemService
                    (Context.SENSOR_SERVICE);

            //Creates new linear accelerometer to track linear acceleration of phone
            linearAccelerometer = Objects.requireNonNull(sensorManager).getDefaultSensor
                    (Sensor.TYPE_LINEAR_ACCELERATION);

            SensorListener sensor = new SensorListener();

            sensorManager.registerListener(sensor, linearAccelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);

        }//End of sensorActivity Method

        /**
         * Responsible for listening to accelerometer data and calling shortcuts
         */
        private class SensorListener implements SensorEventListener {

            /**
             * Called when the accuracy of a sensor has changed.
             *
             * @param sensor   The linear accelerometer sensor
             * @param accuracy The new accuracy of this sensor, one of SensorManager.SENSOR_STATUS_*
             */
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            } //End of onAccuracyChanged

            /**
             * Called when the data from the linear accelerometer changes
             *
             * @param event The linear accelerometer event
             */
            public void onSensorChanged(SensorEvent event) {

                //Tracks amount of time flashlight is left turned on
                long failSafeElapsed = System.currentTimeMillis() - failSafeStart;

                //Fail safe timer, turns off flashlight if time surpasses threshold
                if ((failSafeElapsed / 1000) > failSafeThreshold && failSafeStart != -1) {

                    checkFlashlight();

                    failSafeStart = -1;

                }

                //Logcat of the current x,y and z vector values
                //Log.i(TAG, "onSensorChanged: X: " + event.values[0] + " Y: " + event.values[1] +
                //" Z: " + event.values[2]);

                //Confirms linear accelerometer is being listened to
                if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

                    //Records time of last data fluctuation
                    long currentTimeMillis = System.currentTimeMillis();

                    //Timeout - if the last shake was too long ago, reset the shake count
                    if (currentTimeMillis - timeOfLastShake > shakeTimeout) {

                        shakeCount = 0;

                    }

                    //The following shake must be recorded after a certain time threshold due to data fluctuations
                    if (currentTimeMillis - timeOfLastShake > timeThreshold) {

                        //Stores the x vector value into an easier-to-manage variable
                        deltaX = (event.values[0]);

                        //Stores the y vector value into an easier-to-manage variable
                        deltaY = (event.values[1]);

                        //Stores the z vector value into an easier-to-manage variable
                        deltaZ = (event.values[2]);

                        float totalForce = (event.values[0] + event.values[1] + event.values[2]);

                        //Logcat of the current x,y and z vector values
                        //Log.i(TAG, "onSensorChanged: Total Force: " + totalForce);

                        if (leftHanded == 1) {

                            totalForce = totalForce * -1;

                        }


                        //If the force of the last registered shake surpasses the required threshold, register a shake
                        if (totalForce > forceThreshold) {

                            timeOfLastShake = currentTimeMillis;

                            shakeCount++;

                            //If the number of shakes needed for a shortcut is needed, call a shortcut
                            if ((shakeCount >= requiredShakes)) {

                                shakeCount = 0;

                                //Test to see if change in vector values represents a shake, and if so deploy a shortcut
                                shortcut();
                            }
                        }
                    }
                    //If the vector is below 1, ignore it, as it is just plain noise
                    if (deltaX < 1) {
                        deltaX = 0;
                    }
                    if (deltaY < 1) {
                        deltaY = 0;
                    }
                    if (deltaZ < 1) {
                        deltaZ = 0;
                    }
                }
            }//End of onSensorChanged Method


            /**
             * Controls what shortcut is activated after a shake
             */
            private void shortcut() {

                //Controls the vibration when a shortcut is called
                if (vibration) {
                    //Vibrates phone for half a second
                    vibrator.vibrate(500);
                }

                switch (shortcutFunction) {

                    case 1:

                        flashlightShortcut();

                        break;

                    case 2:

                        appShortcut();

                        break;

                    case 3:

                        break;

                    case 4:

                        break;

                    default:
                        break;

                }


            }//End of shortcut Method

            /**
             * Calls for the phone's flashlight to be turned on or off, depending on its current state
             */
            private void flashlightShortcut() {

                cameraManager.registerTorchCallback(torchCallback, null);

                //Checks if flashlight is off
                if (!flashlight) {

                    turnFlashlightOn();

                }
                //If flashlight wasn't off, it must be on
                else {

                    turnFlashlightOff();

                }

            }//End of flashlightShortcut

            /**
             * Checks flashlight to see if it needs to be turned off if left on
             */
            private void checkFlashlight() {

                cameraManager.registerTorchCallback(torchCallback, null);

                //Checks if flashlight is off
                if (flashlight) {

                    checkIfStillUsing();

                    turnFlashlightOff();
                }


            }//End of flashlightShortcut

            /**
             * Checks if the user is still using the flashlight after timeout period is up
             */
            private void checkIfStillUsing() {

                vibrator.vibrate(500);

                //Initializes intent requests for notification
                Intent keepOnIntent = new Intent(ForegroundService.this, ActionReceiver.class);
                keepOnIntent.setAction("on");
                PendingIntent keepOnPendingIntent = PendingIntent.getBroadcast(ForegroundService.this,
                        1, keepOnIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //Initializes intent requests for notification
                Intent turnOffIntent = new Intent(ForegroundService.this, ActionReceiver.class);
                turnOffIntent.setAction("off");
                PendingIntent turnOffPendingIntent = PendingIntent.getBroadcast(ForegroundService.this,
                        1, turnOffIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ForegroundService.this, SERVICE_CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Are you still using your flashlight?")
                        .setContentText("It's about to be turned off automatically.")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setVibrate(new long[0])
                        .addAction(R.drawable.ic_access_alarm_black_24dp, "Keep On", keepOnPendingIntent)
                        .addAction(R.drawable.ic_pan_tool_black_24dp, "Turn Off", turnOffPendingIntent)
                        .setOngoing(true)
                        .setFullScreenIntent(turnOffPendingIntent,true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);  // heads-up


                Notification notification = builder.build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ForegroundService.this);
                notificationManager.notify(0, notification);
            }

            /**
             * A callback for camera flash torch modes becoming unavailable, disabled, or enabled
             */
            CameraManager.TorchCallback torchCallback = new CameraManager.TorchCallback() {
                /**
                 * Called if a camera's torch mode has become unavailable
                 * @param cameraId Id of the camera
                 */
                @Override
                public void onTorchModeUnavailable(String cameraId) {
                    super.onTorchModeUnavailable(cameraId);
                }

                /**
                 *
                 * @param cameraId Id of the camera
                 * @param enabled Boolean variable containing the current state of the flashlight
                 */
                @Override
                public void onTorchModeChanged(String cameraId, boolean enabled) {
                    super.onTorchModeChanged(cameraId, enabled);
                    flashlight = enabled;
                }
            };

            /**
             * When called, turns the phone's flashlight on
             */
            private void turnFlashlightOn() {

                //Tries to turn flashlight on if flashlight is off
                try {
                    cameraId = cameraManager.getCameraIdList()[0];

                    cameraManager.setTorchMode(cameraId, true);

                } catch (CameraAccessException e) {

                    e.printStackTrace();

                }

                //Tracks that flashlight was turned on
                flashlight = true;

                failSafeStart = System.currentTimeMillis();

            }//End of turnFlashlightOn

            /**
             * When called, turns the phone's flashlight off
             */
            private void turnFlashlightOff() {

                //Tries to turn flashlight off if flashlight is on
                try {

                    cameraId = cameraManager.getCameraIdList()[0];

                    cameraManager.setTorchMode(cameraId, false);

                } catch (CameraAccessException e) {

                    e.printStackTrace();

                }

                //Tracks that flashlight was turned off
                flashlight = false;

            }//End of turnFlashlightOff

            /**
             * When called, opens a specified app
             */
            private void appShortcut() {

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app);
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(ForegroundService.this, "There is no package available in android", Toast.LENGTH_LONG).show();
                }

            }//End of appShortcut

        }//End of SensorListener

    }//End of exampleRunnable

}//End of foregroundService
