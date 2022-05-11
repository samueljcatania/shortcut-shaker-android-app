package com.example.shortcutshaker.introduction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.shortcutshaker.main.MainActivity;
import com.example.shortcutshaker.R;

public class Introduction extends AppCompatActivity implements View.OnClickListener {

    /**
     * Used to slide between different xml fragments
     */
    private ViewPager viewPager;

    /**
     * Controls the layout that holds the dots at the bottom of the screen
     */
    private LinearLayout dotLayout;

    /**
     * The dots that show the user what slide they are on
     */
    private TextView[] dots;

    /**
     * Controls the different xml slider layouts
     */
    private int[] layouts;

    /**
     * Controls next button of all slides
     */
    private Button next;

    /**
     * Controls skip button of all slides
     */
    private Button skip;

    /**
     * Used to interact with class MyViewPageAdapter
     */
    private MyViewPagerAdapter myViewPagerAdapter;

    /**
     * Name of the SharedPreferences variable that tracks the state of the hand chosen
     */
    public static final String FIRST_TIME_START_STATE = "FirstTimeStartState";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    //**********************************************************************************************

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        if (!isFirstTimeStartup()) {

            startMainActivity();
            finish();
        }

        //Assign Components
        viewPager = findViewById(R.id.viewPager);
        dotLayout = findViewById(R.id.dotLayout);
        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip);

        //Add ClickListener to Buttons
        next.setOnClickListener(this);
        skip.setOnClickListener(this);

        setStatusBarTransparent();

        layouts = new int[]{R.layout.activity_slide_1, R.layout.activity_slide_2,
                R.layout.activity_slide_3, R.layout.activity_slide_4};

        myViewPagerAdapter = new MyViewPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.addOnPageChangeListener(onPageChangeListener);
        setDotStatus(0);

    }//End of onCreate

    /**
     * Callback interface for responding to changing state of the selected page
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //If on last page
            if (position == layouts.length - 1) {

                next.setText(R.string.start);
                skip.setVisibility(View.GONE);
            } else {

                next.setText(R.string.next);
                skip.setVisibility(View.VISIBLE);
            }
            setDotStatus(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * Controls the outcome of clicking the buttons
     *
     * @param v A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
     */
    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {

            case R.id.next:
                int currentPage = viewPager.getCurrentItem() + 1;
                if (currentPage < layouts.length) {

                    //Move to next page
                    viewPager.setCurrentItem(currentPage);

                } else {

                    startMainActivity();
                }

                break;

            case R.id.skip:

                startMainActivity();
                break;

            default:

                break;

        }

    }//End of onClick

    private boolean isFirstTimeStartup() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        return sharedPreferences.getBoolean(FIRST_TIME_START_STATE, true);
    }

    private void setFirstTimeStartStatus(boolean status) {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_START_STATE, status);

        editor.apply();
    }

    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private void setDotStatus(int page) {

        dotLayout.removeAllViews();
        dots = new TextView[layouts.length];

        for (int a = 0; a < dots.length; a++) {

            dots[a] = new TextView(this);
            dots[a].setText(Html.fromHtml("&#8226;"));
            dots[a].setTextSize(35);
            dots[a].setTextColor(Color.parseColor("#a9b4bb"));

            dotLayout.addView(dots[a]);
        }

        //Set current dot active
        if (dots.length > 0) {

            dots[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }//End of setDotStatus

    /**
     * Starts MainActivity when called
     */
    private void startMainActivity() {

        setFirstTimeStartStatus(true);
        startActivity(new Intent(Introduction.this, MainActivity.class));
        finish();
    }//End of startMainActivity

}//End of class Introduction
