package com.example.shortcutshaker.home_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.shortcutshaker.main.MainActivity;
import com.example.shortcutshaker.R;
import com.example.shortcutshaker.service.ForegroundService;

public class ShakesRequired extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declares all cards within the activity xml file
     */
    private CardView one, two, three, four, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView one_textView, two_textView, three_textView, four_textView, shakesRequired_backButton;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the state of the shakes
     */
    public static final String SHAKE_STATE = "Shake State";

    /**
     * Name of the SharedPreferences variable that tracks the state of the shakes
     */
    public static final String SHAKES_REQUIRED = "Shakes Required";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of the shakes
     */
    public int shakeState = 2;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakes_required);

        //Assign TextViews
        one_textView = findViewById(R.id.one_textView);
        two_textView = findViewById(R.id.two_textView);
        three_textView = findViewById(R.id.three_textView);
        four_textView = findViewById(R.id.four_textView);
        shakesRequired_backButton = findViewById(R.id.shakesRequired_backButton);

        //Assign Cards
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        back.setOnClickListener(this);

        loadData();
        updateViews();
    }

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

            case R.id.one:
                shakeState = 1;
                break;

            case R.id.two:
                shakeState = 2;
                break;

            case R.id.three:
                shakeState = 3;
                break;

            case R.id.four:
                shakeState = 4;
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                shakesRequired_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

        ForegroundService.requiredShakes = shakeState;

        saveData();
        updateViews();

    }//End of onClick

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHAKE_STATE, shakeState);
        editor.putInt(SHAKES_REQUIRED, shakeState);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        shakeState = sharedPreferences.getInt(SHAKE_STATE, 2);
    }//End of loadData

    /**
     * Updates UI interface with previous settings
     */
    public void updateViews() {

        if (shakeState == 1) {
            resetViews();
            one.setCardBackgroundColor(Color.WHITE);
            one_textView.setTextColor(Color.BLACK);
        } else if (shakeState == 2) {
            resetViews();
            two.setCardBackgroundColor(Color.WHITE);
            two_textView.setTextColor(Color.BLACK);
        } else if (shakeState == 3) {
            resetViews();
            three.setCardBackgroundColor(Color.WHITE);
            three_textView.setTextColor(Color.BLACK);
        } else {
            resetViews();
            four.setCardBackgroundColor(Color.WHITE);
            four_textView.setTextColor(Color.BLACK);
        }

    }//End of updateViews

    /**
     * Resets all views
     */
    public void resetViews() {
        one.setCardBackgroundColor(0xFF2d2d2d);
        two.setCardBackgroundColor(0xFF2d2d2d);
        three.setCardBackgroundColor(0xFF2d2d2d);
        four.setCardBackgroundColor(0xFF2d2d2d);
        one_textView.setTextColor(Color.WHITE);
        two_textView.setTextColor(Color.WHITE);
        three_textView.setTextColor(Color.WHITE);
        four_textView.setTextColor(Color.WHITE);
    }//End of resetViews

}//End of class shakesRequired
