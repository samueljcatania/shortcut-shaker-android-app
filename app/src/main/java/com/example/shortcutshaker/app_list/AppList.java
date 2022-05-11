package com.example.shortcutshaker.app_list;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shortcutshaker.R;

public class AppList extends AppCompatActivity implements View.OnClickListener {

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);

    }

    @Override
    public void onClick(View v) {

    }
}