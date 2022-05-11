package com.example.shortcutshaker.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.shortcutshaker.R;
import com.example.shortcutshaker.home_screen.About;
import com.example.shortcutshaker.home_screen.Disclaimer;
import com.example.shortcutshaker.home_screen.LeftRightHand;
import com.example.shortcutshaker.home_screen.OtherSettings;
import com.example.shortcutshaker.home_screen.Sensitivity;
import com.example.shortcutshaker.home_screen.SetShortcut;
import com.example.shortcutshaker.home_screen.ShakesRequired;
import com.example.shortcutshaker.home_screen.TimeoutPeriod;
import com.example.shortcutshaker.home_screen.TurnOffAds;
import com.example.shortcutshaker.home_screen.TurnOnOff;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declares all cards within the activity xml file
     */
    private CardView turnOnOff, setShortcut, sensitivity, shakesRequired, timeoutPeriod,
            leftRightHand, otherSettings, disclaimer, turnOffAds, about;

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView turnOnOff_textView, setShortcut_textView, sensitivity_textView,
            shakesRequired_textView, timeoutPeriod_textView, otherSettings_textView,
            leftRightHand_textView, disclaimer_textView, turnOffAds_textView, about_textView;

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign TextViews
        turnOnOff_textView = findViewById(R.id.turnOnOff_textView);
        setShortcut_textView = findViewById(R.id.setShortcut_textView);
        sensitivity_textView = findViewById(R.id.sensitivity_textView);
        shakesRequired_textView = findViewById(R.id.shakesRequired_textView);
        timeoutPeriod_textView = findViewById(R.id.timeoutPeriod_textView);
        otherSettings_textView = findViewById(R.id.otherSettings_textView);
        leftRightHand_textView = findViewById(R.id.leftRightHand_textView);
        disclaimer_textView = findViewById(R.id.disclaimer_textView);
        turnOffAds_textView = findViewById(R.id.turnOffAds_textView);
        about_textView = findViewById(R.id.about_textView);

        //Assign Cards
        turnOnOff = findViewById(R.id.turnOnOff);
        setShortcut = findViewById(R.id.setShortcut);
        sensitivity = findViewById(R.id.sensitivity);
        shakesRequired = findViewById(R.id.shakesRequired);
        timeoutPeriod = findViewById(R.id.timeoutPeriod);
        otherSettings = findViewById(R.id.otherSettings);
        leftRightHand = findViewById(R.id.leftRightHand);
        disclaimer = findViewById(R.id.disclaimer);
        turnOffAds = findViewById(R.id.turnOffAds);
        about = findViewById(R.id.about);


        //Add ClickListener to Cards
        turnOnOff.setOnClickListener(this);
        setShortcut.setOnClickListener(this);
        sensitivity.setOnClickListener(this);
        shakesRequired.setOnClickListener(this);
        timeoutPeriod.setOnClickListener(this);
        otherSettings.setOnClickListener(this);
        leftRightHand.setOnClickListener(this);
        disclaimer.setOnClickListener(this);
        turnOffAds.setOnClickListener(this);
        about.setOnClickListener(this);

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

            case R.id.turnOnOff:
                intent = new Intent(this, TurnOnOff.class);
                startActivity(intent);
                turnOnOff.setCardBackgroundColor(Color.WHITE);
                turnOnOff_textView.setTextColor(Color.BLACK);
                break;

            case R.id.setShortcut:
                intent = new Intent(this, SetShortcut.class);
                startActivity(intent);
                setShortcut.setCardBackgroundColor(Color.WHITE);
                setShortcut_textView.setTextColor(Color.BLACK);
                break;

            case R.id.sensitivity:
                intent = new Intent(this, Sensitivity.class);
                startActivity(intent);
                sensitivity.setCardBackgroundColor(Color.WHITE);
                sensitivity_textView.setTextColor(Color.BLACK);
                break;

            case R.id.shakesRequired:
                intent = new Intent(this, ShakesRequired.class);
                startActivity(intent);
                shakesRequired.setCardBackgroundColor(Color.WHITE);
                shakesRequired_textView.setTextColor(Color.BLACK);
                break;

            case R.id.timeoutPeriod:
                intent = new Intent(this, TimeoutPeriod.class);
                startActivity(intent);
                timeoutPeriod.setCardBackgroundColor(Color.WHITE);
                timeoutPeriod_textView.setTextColor(Color.BLACK);
                break;

            case R.id.otherSettings:
                intent = new Intent(this, OtherSettings.class);
                startActivity(intent);
                otherSettings.setCardBackgroundColor(Color.WHITE);
                otherSettings_textView.setTextColor(Color.BLACK);
                break;

            case R.id.leftRightHand:
                intent = new Intent(this, LeftRightHand.class);
                startActivity(intent);
                leftRightHand.setCardBackgroundColor(Color.WHITE);
                leftRightHand_textView.setTextColor(Color.BLACK);
                break;

            case R.id.disclaimer:
                intent = new Intent(this, Disclaimer.class);
                startActivity(intent);
                disclaimer.setCardBackgroundColor(Color.WHITE);
                disclaimer_textView.setTextColor(Color.BLACK);
                break;

            case R.id.turnOffAds:
                intent = new Intent(this, TurnOffAds.class);
                startActivity(intent);
                turnOffAds.setCardBackgroundColor(Color.WHITE);
                turnOffAds_textView.setTextColor(Color.BLACK);
                break;

            case R.id.about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                about.setCardBackgroundColor(Color.WHITE);
                about_textView.setTextColor(Color.BLACK);
                break;

            default:

                break;

        }

    }//End of onClick

}//End of class MainActivity
