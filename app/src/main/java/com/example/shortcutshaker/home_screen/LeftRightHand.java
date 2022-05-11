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

public class LeftRightHand extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declares all cards within the activity xml file
     */
    private CardView left, right, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView left_textView, right_textView, leftRightHand_backButton;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the state of the hand chosen
     */
    public static final String HAND_STATE = "Hand State";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of the hand chosen
     */
    public int handState = 2;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_right_hand);

        //Assign TextViews
        left_textView = findViewById(R.id.left_textView);
        right_textView = findViewById(R.id.right_textView);
        leftRightHand_backButton = findViewById(R.id.leftRightHand_backButton);

        //Assign Cards
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        left.setOnClickListener(this);
        right.setOnClickListener(this);
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

            case R.id.left:
                handState = 1;
                break;

            case R.id.right:
                handState = 2;
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                leftRightHand_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

        ForegroundService.leftHanded = handState;

        saveData();
        updateViews();

    }//End of onClick

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HAND_STATE, handState);

        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        handState = sharedPreferences.getInt(HAND_STATE, 2);
    }//End of loadData

    /**
     * Updates UI interface with previous settings
     */
    public void updateViews() {

        if (handState == 1) {
            left.setCardBackgroundColor(Color.WHITE);
            right.setCardBackgroundColor(0xFF2d2d2d);
            left_textView.setTextColor(Color.BLACK);
            right_textView.setTextColor(Color.WHITE);

        } else {
            left.setCardBackgroundColor(0xFF2d2d2d);
            right.setCardBackgroundColor(Color.WHITE);
            left_textView.setTextColor(Color.WHITE);
            right_textView.setTextColor(Color.BLACK);
        }

    }//End of updateViews

}//End of class LeftRightHand