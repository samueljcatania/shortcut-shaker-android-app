package com.example.shortcutshaker.home_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.shortcutshaker.main.MainActivity;
import com.example.shortcutshaker.R;
import com.example.shortcutshaker.service.ForegroundService;

public class Sensitivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    /**
     * Name of the SharedPreferences variable that tracks the state of sensitivity
     */
    public static final String SENSITIVITY_STATE = "Sensitivity State";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Declares all cards within the activity xml file
     */
    private CardView back;

    /**
     * SeekBar used to control the sensitivity of the shakes
     */
    private SeekBar seekBar;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView sensitivity_backButton;

    /**
     * Stores seekBar progress
     */
    private int progressGlobal = 100;


    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensitivity);

        //Assign TextViews
        sensitivity_backButton = findViewById(R.id.sensitivity_backButton);

        //Assign Cards
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        back.setOnClickListener(this);

        //Assigns SeekBar
        seekBar = findViewById(R.id.seekBar);

        //Add SeekBarChangeListener to SeekBar
        seekBar.setOnSeekBarChangeListener(this);

        loadData();

        seekBar.setProgress(progressGlobal);

    }//End of onCreate

    @Override
    public void onBackPressed() {
    }

    /**
     * Controls the outcome of clicking the buttons
     *
     * @param v A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
     */
    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                sensitivity_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }


    }//End of onClick

    /**
     * Called when the seekBar is changed, gets the new value and the particular seekBar
     *
     * @param seekBar  The particular seekBar that has been changed
     * @param progress The new value the seekBar has been changed to
     * @param fromUser If the user has changed the seekBar or not
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        progressGlobal = progress;

        ForegroundService.forceThreshold = progressGlobal;

        saveData();

    }//End of onProgressChanged

    /**
     * Called when the user's finger is currently controlling the slider on the seekBar
     *
     * @param seekBar The particular seekBar that has been changed
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }//End of onStartTrackingTouch

    /**
     * Called when the user's finger is lifted from the slider on the seekBar
     *
     * @param seekBar The particular seekBar that has been changed
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }//End of onStopTrackingTouch

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SENSITIVITY_STATE, progressGlobal);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        progressGlobal = sharedPreferences.getInt(SENSITIVITY_STATE, 100);
    }//End of loadData

}//End of Class Sensitivity