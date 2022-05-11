package com.example.shortcutshaker.home_screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.shortcutshaker.main.MainActivity;
import com.example.shortcutshaker.R;

public class OtherSettings extends AppCompatActivity implements View.OnClickListener {


    /**
     * Declares all cards within the activity xml file
     */
    private CardView vibration, back;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView vibration_textView, otherSettings_backButton;

    //**********************************************************************************************

    /**
     * Controls the colors of the buttons depending on the state of other settings
     */
    public int otherSettingsState = 1;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);

        //Assign TextViews
        vibration_textView = findViewById(R.id.vibration_textView);
        otherSettings_backButton = findViewById(R.id.otherSettings_backButton);

        //Assign Cards
        vibration = findViewById(R.id.vibration);
        back = findViewById(R.id.back);

        //Add ClickListener to Cards
        vibration.setOnClickListener(this);
        back.setOnClickListener(this);

    }

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

            case R.id.vibration:
                intent = new Intent(this, Vibration.class);
                startActivity(intent);
                otherSettingsState = 1;
                vibration.setCardBackgroundColor(Color.WHITE);
                vibration_textView.setTextColor(Color.BLACK);
                break;

            case R.id.back:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                back.setCardBackgroundColor(Color.WHITE);
                otherSettings_backButton.setTextColor(Color.BLACK);
                break;

            default:
                break;

        }

    }//End of onClick

}//End of Class OtherSettings