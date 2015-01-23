package com.signify.intouch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.signify.intouch.data.Settings;


public class MainActivity extends ActionBarActivity {

    private static final String First_Run = "com.signify.intouch.firstrun";
    Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = Settings.getInstance(this);
        firstRunCheck();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_contact) {
            Intent intent = new Intent(this, SetupContactActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_change_days) {
            Intent intent = new Intent(this, SetupDayActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SetupDayActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void firstRunCheck(){
        if(mSettings.getFirstRun()){
            Intent intent = new Intent(this, SetupContactActivity.class);
            startActivity(intent);
        }
    }
}
