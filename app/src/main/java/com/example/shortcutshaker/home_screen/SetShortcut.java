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
import com.example.shortcutshaker.app_list.AllAppsActivity;
import com.example.shortcutshaker.service.ForegroundService;

public class SetShortcut extends AppCompatActivity implements View.OnClickListener {


    /**
     * Declares all cards within the activity xml file
     */
    private CardView flashlight, app, command, other, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView flashlight_textView, app_textView, command_textView, other_textView,
            setShortcut_backButton;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the state of the shortcut
     */
    public static final String SHORTCUT_STATE = "Shortcut State";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of the shortcut
     */
    public int shortcutState = 1;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_shortcut);

        //Assign TextViews
        flashlight_textView = findViewById(R.id.flashlight_textView);
        app_textView = findViewById(R.id.app_textView);
        command_textView = findViewById(R.id.command_textView);
        other_textView = findViewById(R.id.other_textView);
        setShortcut_backButton = findViewById(R.id.setShortcut_backButton);

        //Assign Cards
        flashlight = findViewById(R.id.flashlight);
        app = findViewById(R.id.app);
        command = findViewById(R.id.command);
        other = findViewById(R.id.other);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        flashlight.setOnClickListener(this);
        app.setOnClickListener(this);
        command.setOnClickListener(this);
        other.setOnClickListener(this);
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

            case R.id.flashlight:
                shortcutState = 1;
                break;

            case R.id.app:
                intent = new Intent(this, AllAppsActivity.class);
                startActivity(intent);
                shortcutState = 2;
                break;

            case R.id.command:
                shortcutState = 3;
                break;

            case R.id.other:
                shortcutState = 4;
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                setShortcut_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

        ForegroundService.shortcutFunction = shortcutState;

        saveData();
        updateViews();

    }//End of onClick

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHORTCUT_STATE, shortcutState);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        shortcutState = sharedPreferences.getInt(SHORTCUT_STATE, 1);
    }//End of loadData

    /**
     * Updates UI interface with previous settings
     */
    public void updateViews() {

        if (shortcutState == 1) {
            resetViews();
            flashlight.setCardBackgroundColor(Color.WHITE);
            flashlight_textView.setTextColor(Color.BLACK);
        } else if (shortcutState == 2) {
            resetViews();
            app.setCardBackgroundColor(Color.WHITE);
            app_textView.setTextColor(Color.BLACK);
        } else if (shortcutState == 3) {
            resetViews();
            command.setCardBackgroundColor(Color.WHITE);
            command_textView.setTextColor(Color.BLACK);
        } else {
            resetViews();
            other.setCardBackgroundColor(Color.WHITE);
            other_textView.setTextColor(Color.BLACK);
        }

    }//End of updateViews

    /**
     * Resets all views
     */
    public void resetViews() {
        flashlight.setCardBackgroundColor(0xFF2d2d2d);
        app.setCardBackgroundColor(0xFF2d2d2d);
        command.setCardBackgroundColor(0xFF2d2d2d);
        other.setCardBackgroundColor(0xFF2d2d2d);
        flashlight_textView.setTextColor(Color.WHITE);
        app_textView.setTextColor(Color.WHITE);
        command_textView.setTextColor(Color.WHITE);
        other_textView.setTextColor(Color.WHITE);
    }//End of resetViews

}//End of class SetShortcut
