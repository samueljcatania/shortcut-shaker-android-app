package com.example.shortcutshaker.home_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.shortcutshaker.R;
import com.example.shortcutshaker.service.ForegroundService;

public class Vibration extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declares all cards within the activity xml file
     */
    private CardView vibrationOn, vibrationOff, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView vibrationOn_textView, vibrationOff_textView, vibration_backButton;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the state of vibration
     */
    public static final String VIBRATION_STATE = "Vibration State";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of vibration
     */
    public boolean vibrationState = true;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);


        //Assign TextViews
        vibrationOn_textView = findViewById(R.id.vibrationOn_textView);
        vibrationOff_textView = findViewById(R.id.vibrationOff_textView);
        vibration_backButton = findViewById(R.id.vibration_backButton);

        //Assign Cards
        vibrationOn = findViewById(R.id.vibrationOn);
        vibrationOff = findViewById(R.id.vibrationOff);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        vibrationOn.setOnClickListener(this);
        vibrationOff.setOnClickListener(this);
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

            case R.id.vibrationOn:
                vibrationState = true;
                break;

            case R.id.vibrationOff:
                vibrationState = false;
                break;

            case R.id.back:
                intent = new Intent(this, OtherSettings.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                vibration_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

        ForegroundService.vibration = vibrationState;

        saveData();
        updateViews();

    }//End of onClick

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(VIBRATION_STATE, vibrationState);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        vibrationState = sharedPreferences.getBoolean(VIBRATION_STATE, true);
    }//End of loadData

    /**
     * Updates UI interface with previous settings
     */
    public void updateViews() {

        if (vibrationState) {
            vibrationOn.setCardBackgroundColor(Color.WHITE);
            vibrationOff.setCardBackgroundColor(0xFF2d2d2d);
            vibrationOn_textView.setTextColor(Color.BLACK);
            vibrationOff_textView.setTextColor(Color.WHITE);

        } else {
            vibrationOn.setCardBackgroundColor(0xFF2d2d2d);
            vibrationOff.setCardBackgroundColor(Color.WHITE);
            vibrationOn_textView.setTextColor(Color.WHITE);
            vibrationOff_textView.setTextColor(Color.BLACK);
        }

    }//End of updateViews

}//End of class Vibration