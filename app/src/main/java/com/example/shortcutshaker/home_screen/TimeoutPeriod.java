package com.example.shortcutshaker.home_screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class TimeoutPeriod extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declares all cards within the activity xml file
     */
    private CardView oneMinute, twoMinutes, threeMinutes, fourMinutes, fiveMinutes,
            sevenMinutes, tenMinutes, fifteenMinutes, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView oneMinute_textView, twoMinutes_textView, threeMinutes_textView,
            fourMinutes_textView, fiveMinutes_textView, sevenMinutes_textView,
            tenMinutes_textView, fifteenMinutes_textView, timeout_period_backButton;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the state of the timeout period
     */
    public static final String TIMEOUT_STATE = "Timeout State";

    /**
     * Name of the SharedPreferences variable that tracks the timeout period pop-up warning
     */
    public static final String TIMEOUT_WARNING = "Timeout Warning";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of the timeout period
     */
    public int timeoutState = 3;

    /**
     * Controls the pop-up warning, only allowing it to show once
     */
    public boolean warned = false;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_period);

        //Assign TextViews
        oneMinute_textView = findViewById(R.id.oneMinute_textView);
        twoMinutes_textView = findViewById(R.id.twoMinutes_textView);
        threeMinutes_textView = findViewById(R.id.threeMinutes_textView);
        fourMinutes_textView = findViewById(R.id.fourMinutes_textView);
        fiveMinutes_textView = findViewById(R.id.fiveMinutes_textView);
        sevenMinutes_textView = findViewById(R.id.sevenMinutes_textView);
        tenMinutes_textView = findViewById(R.id.tenMinutes_textView);
        fifteenMinutes_textView = findViewById(R.id.fifteenMinutes_textView);
        timeout_period_backButton = findViewById(R.id.timeout_period_backButton);

        //Assign Cards
        oneMinute = findViewById(R.id.oneMinute);
        twoMinutes = findViewById(R.id.twoMinutes);
        threeMinutes = findViewById(R.id.threeMinutes);
        fourMinutes = findViewById(R.id.fourMinutes);
        fiveMinutes = findViewById(R.id.fiveMinutes);
        sevenMinutes = findViewById(R.id.sevenMinutes);
        tenMinutes = findViewById(R.id.tenMinutes);
        fifteenMinutes = findViewById(R.id.fifteenMinutes);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        oneMinute.setOnClickListener(this);
        twoMinutes.setOnClickListener(this);
        threeMinutes.setOnClickListener(this);
        fourMinutes.setOnClickListener(this);
        fiveMinutes.setOnClickListener(this);
        sevenMinutes.setOnClickListener(this);
        tenMinutes.setOnClickListener(this);
        fifteenMinutes.setOnClickListener(this);
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

            case R.id.oneMinute:
                timeoutState = 1;
                break;

            case R.id.twoMinutes:
                timeoutState = 2;
                break;

            case R.id.threeMinutes:
                timeoutState = 3;
                break;

            case R.id.fourMinutes:
                timeoutState = 4;
                break;

            case R.id.fiveMinutes:
                timeoutState = 5;
                break;

            case R.id.sevenMinutes:
                timeoutState = 7;
                break;

            case R.id.tenMinutes:
                timeoutState = 10;
                break;

            case R.id.fifteenMinutes:
                timeoutState = 15;
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                timeout_period_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

        ForegroundService.failSafeThreshold = timeoutState;

        saveData();
        updateViews();

    }//End of onClick

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TIMEOUT_STATE, timeoutState);
        editor.putBoolean(TIMEOUT_WARNING, warned);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        timeoutState = sharedPreferences.getInt(TIMEOUT_STATE, 3);
        warned = sharedPreferences.getBoolean(TIMEOUT_WARNING, false);
    }//End of loadData

    /**
     * Updates UI interface with previous settings
     */
    public void updateViews() {

        switch (timeoutState) {

            case 1:
                resetViews();
                oneMinute.setCardBackgroundColor(Color.WHITE);
                oneMinute_textView.setTextColor(Color.BLACK);
                break;
            case 2:
                resetViews();
                twoMinutes.setCardBackgroundColor(Color.WHITE);
                twoMinutes_textView.setTextColor(Color.BLACK);
                break;
            case 3:
                resetViews();
                threeMinutes.setCardBackgroundColor(Color.WHITE);
                threeMinutes_textView.setTextColor(Color.BLACK);
                break;
            case 4:
                resetViews();
                fourMinutes.setCardBackgroundColor(Color.WHITE);
                fourMinutes_textView.setTextColor(Color.BLACK);
                break;
            case 5:
                resetViews();
                fiveMinutes.setCardBackgroundColor(Color.WHITE);
                fiveMinutes_textView.setTextColor(Color.BLACK);
                break;
            case 7:
                resetViews();
                sevenMinutes.setCardBackgroundColor(Color.WHITE);
                sevenMinutes_textView.setTextColor(Color.BLACK);
                break;
            case 10:
                resetViews();
                tenMinutes.setCardBackgroundColor(Color.WHITE);
                tenMinutes_textView.setTextColor(Color.BLACK);
                break;
            case 15:
                resetViews();
                fifteenMinutes.setCardBackgroundColor(Color.WHITE);
                fifteenMinutes_textView.setTextColor(Color.BLACK);
                break;

        }

        if (!warned) {
            new AlertDialog.Builder(this)
                    .setTitle("Notice")
                    .setMessage("These settings only apply to the flashlight shortcut. When the " +
                            "flashlight is turned on, the program will wait a specific \"timeout " +
                            "period\" until it asks the user if they are still using the flashlight. " +
                            "(If the flashlight is still on at that point) If the " +
                            "user doesn't respond, the program will turn the flashlight off " +
                            "automatically to save battery.")
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
     * Resets all views
     */
    public void resetViews() {
        oneMinute.setCardBackgroundColor(0xFF2d2d2d);
        twoMinutes.setCardBackgroundColor(0xFF2d2d2d);
        threeMinutes.setCardBackgroundColor(0xFF2d2d2d);
        fourMinutes.setCardBackgroundColor(0xFF2d2d2d);
        fiveMinutes.setCardBackgroundColor(0xFF2d2d2d);
        sevenMinutes.setCardBackgroundColor(0xFF2d2d2d);
        tenMinutes.setCardBackgroundColor(0xFF2d2d2d);
        fifteenMinutes.setCardBackgroundColor(0xFF2d2d2d);
        oneMinute_textView.setTextColor(Color.WHITE);
        twoMinutes_textView.setTextColor(Color.WHITE);
        threeMinutes_textView.setTextColor(Color.WHITE);
        fourMinutes_textView.setTextColor(Color.WHITE);
        fiveMinutes_textView.setTextColor(Color.WHITE);
        sevenMinutes_textView.setTextColor(Color.WHITE);
        tenMinutes_textView.setTextColor(Color.WHITE);
        fifteenMinutes_textView.setTextColor(Color.WHITE);
    }//End of resetViews

}//End of class TimeoutPeriod
