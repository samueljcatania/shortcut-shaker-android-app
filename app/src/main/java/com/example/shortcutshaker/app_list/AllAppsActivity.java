package com.example.shortcutshaker.app_list;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shortcutshaker.service.ForegroundService;
import com.example.shortcutshaker.R;


public class AllAppsActivity extends ListActivity {

    /**
     * Name of the SharedPreferences variable that tracks the state of the service
     */
    private static final String APP_SELECTED = "App Selected";

    //**********************************************************************************************

    /**
     * Declares all textViews within the activity xml file
     */
    private TextView app_name, app_package;

    private View lastView;

    //**********************************************************************************************

    /**
     *
     */
    private PackageManager packageManager = null;

    /**
     *
     */
    private List<ApplicationInfo> appList = null;

    /**
     *
     */
    private ApplicationAdapter listAdaptor = null;

    //**********************************************************************************************

    /**
     * Name of the SharedPreferences variable that tracks the app chosen
     */
    public static final String APP_STATE = "App State";

    /**
     * Name of the SharedPreferences instance that is used to save user preferences
     */
    public static final String USER_PREFS = "User Preferences";

    /**
     * Executes when this class is called/created
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);

        packageManager = getPackageManager();

        new LoadApplications().execute();
    }

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
    }//End of saveData

    /**
     * Loads all user input from last time this activity was opened
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
    }//End of loadData

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        loadData();

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;

        switch (item.getItemId()) {
            case R.id.menu_about: {
                displayAboutDialog();

                break;
            }
            default: {
                result = super.onOptionsItemSelected(item);

                break;
            }
        }

        return result;
    }

    private void displayAboutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.about_title));
        builder.setMessage(getString(R.string.about_desc));

        builder.setPositiveButton("Know More", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("null"));
                startActivity(browserIntent);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = appList.get(position);
        try {

            ForegroundService.app = (app.packageName);

            Log.d("LISTVIEW", "onListItemClick: " + l);

            v.setBackgroundColor(getResources().getColor(R.color.white));

            //Assigns TextViews
            app_name = v.findViewById(R.id.app_name);
            app_package = v.findViewById(R.id.app_package);

            app_name.setTextColor(Color.BLACK);
            app_package.setTextColor(Color.BLACK);

            saveData(v);

            if (lastView != null && lastView != v) {

                lastView.setBackgroundColor(getResources().getColor(R.color.black));

                app_name = lastView.findViewById(R.id.app_name);
                app_package = lastView.findViewById(R.id.app_package);

                app_name.setTextColor(Color.WHITE);
                app_package.setTextColor(Color.WHITE);

            }

            lastView = v;

            /*
            Intent intent = packageManager
                    .getLaunchIntentForPackage(app.packageName);

            Log.d("APP LIST", "onListItemClick: " + app.packageName);

            if (null != intent) {
                startActivity(intent);
            }

             */
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AllAppsActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        saveData(app);

    }

    /**
     * Saves all user input for next time this activity is opened
     */
    public void saveData(ApplicationInfo app) {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_STATE, app.packageName);

        editor.apply();
    }//End of saveData

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listAdaptor = new ApplicationAdapter(AllAppsActivity.this,
                    R.layout.app_sorter, appList);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listAdaptor);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AllAppsActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}