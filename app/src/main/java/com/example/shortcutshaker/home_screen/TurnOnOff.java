package com.example.shortcutshaker.home_screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.shortcutshaker.main.MainActivity;
import com.example.shortcutshaker.R;
import com.example.shortcutshaker.service.ForegroundService;

public class TurnOnOff extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declares all cards within the activity xml file
     */
    private CardView startService, stopService, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView startService_textView, stopService_textView, turnOnOff_backButton;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the state of the service
     */
    private static final String SERVICE_STATE = "Service State";

    /**
     * Name of the SharedPreferences variable that tracks the service pop-up warning
     */
    private static final String SERVICE_WARNING = "Service Warning";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    private static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of the service
     */
    private boolean serviceState = false;

    /**
     * Controls the pop-up warning, only allowing it to show once
     */
    private boolean warned = false;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_on_off);

        //Assign TextViews
        startService_textView = findViewById(R.id.startService_textView);
        stopService_textView = findViewById(R.id.stopService_textView);
        turnOnOff_backButton = findViewById(R.id.turnOnOff_backButton);

        //Assign Cards
        startService = findViewById(R.id.startService);
        stopService = findViewById(R.id.stopService);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        back.setOnClickListener(this);

        loadData();
        updateViews();

    }//End of onCreate

    /**
     * Disables back button
     */
    @Override
    public void onBackPressed() {
    }//End on onBackPressed


    /**
     * Controls the outcome of clicking the buttons
     *
     * @param v A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
     */
    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.startService:
                startService();
                serviceState = true;
                break;

            case R.id.stopService:
                stopService();
                serviceState = false;
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                turnOnOff_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

        saveData();
        updateViews();

    }//End of onClick

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SERVICE_STATE, serviceState);
        editor.putBoolean(SERVICE_WARNING, warned);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        serviceState = sharedPreferences.getBoolean(SERVICE_STATE, false);
        warned = sharedPreferences.getBoolean(SERVICE_WARNING, false);
    }//End of loadData

    /**
     * Updates UI interface with previous settings
     */
    public void updateViews() {

        if (serviceState) {
            startService.setCardBackgroundColor(Color.WHITE);
            stopService.setCardBackgroundColor(0xFF2d2d2d);
            startService_textView.setTextColor(Color.BLACK);
            stopService_textView.setTextColor(Color.WHITE);

        } else {
            stopService.setCardBackgroundColor(Color.WHITE);
            startService.setCardBackgroundColor(0xFF2d2d2d);
            startService_textView.setTextColor(Color.WHITE);
            stopService_textView.setTextColor(Color.BLACK);
        }

        if (!warned) {
            new AlertDialog.Builder(this)
                    .setTitle("Notice")
                    .setMessage("When stopping the shortcut service by tapping the \"Off\" " +
                            "button, you may notice the service doesn't end right away and may " +
                            "keep running. This is a result of Android System Design, " +
                            "and the service isn't killed by the system until the system needs " +
                            "more resources. \n\nIt will stop shortly, though you can end it " +
                            "immediately by closing the app after turning it off.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

            warned = true;

            saveData();
        }
    }//End of updateViews

    /**
     * Creates a request to start the service and starts the foreground service
     */
    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);

        ContextCompat.startForegroundService(this, serviceIntent);
    }//End of startService

    /**
     * Takes the request to start the service (intent) and calls a default method to stop the service
     */
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }//End of stopService

}//End of class TurnOnOff
