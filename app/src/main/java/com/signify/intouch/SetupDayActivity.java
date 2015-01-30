package com.signify.intouch;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.signify.intouch.data.Settings;
import com.signify.intouch.utils.AlarmMan;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class SetupDayActivity extends ActionBarActivity {

    private Button buttonTimeStart;
    private Button buttonTimeFinish;
    private Button buttonDayNext;
    private String mTimerUsed;

    private Settings mSettings;
    private String[] mTimes;

    private RelativeLayout layoutStartTimeGroup;
    private RelativeLayout layoutFinishTimeGroup;

    private SimpleDateFormat mDateFormat;

    private Boolean[] mActiveDays;

    private  boolean mToggleAlerts;

    private int[] mCheckBoxIds = new int[] {
            R.id.checkbox_monday,
            R.id.checkbox_tuesday,
            R.id.checkbox_wednesday,
            R.id.checkbox_thursday,
            R.id.checkbox_friday,
            R.id.checkbox_saturday,
            R.id.checkbox_sunday
    };

    private RelativeLayout layoutToggleAlerts;
    private ToggleButton buttonToggleAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_day);

        mDateFormat = new SimpleDateFormat("H:mm");


        mSettings = Settings.getInstance(this);

        layoutStartTimeGroup = (RelativeLayout)findViewById(R.id.layoutStartTimeGroup);
        layoutFinishTimeGroup = (RelativeLayout)findViewById(R.id.layoutFinishTimeGroup);
        layoutToggleAlerts = (RelativeLayout)findViewById(R.id.layoutToggleAlerts);

        buttonDayNext = (Button) findViewById(R.id.buttonDayNext);
        buttonDayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        buttonTimeStart = (Button) findViewById(R.id.buttonTimeStart);
        buttonTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerUsed = "startTime";
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        buttonTimeFinish = (Button) findViewById(R.id.buttonTimeFinish);
        buttonTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerUsed = "endTime";
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        buttonToggleAlerts = (ToggleButton)findViewById(R.id.buttonToggleAlerts);
        buttonToggleAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleAlerts = buttonToggleAlerts.isChecked();
            }
        });

        if(mSettings.getFirstRun()) {
            mActiveDays = new Boolean[]{false, false, false, false, false, false, false};
            mTimes = new String[]{"00:00", "23:59"};
            mToggleAlerts = true;
            buttonToggleAlerts.setChecked(mToggleAlerts);
        } else {
            mActiveDays = mSettings.getActiveDays();
            mTimes = mSettings.getTimes();
            buttonTimeStart.setText("Start at " + mTimes[0]);
            buttonTimeFinish.setText("Finish at "+ mTimes[1]);
            Log.w("get alerts",String.valueOf(mSettings.getAlertsOn()));
            mToggleAlerts = mSettings.getAlertsOn();
            buttonToggleAlerts.setChecked(mToggleAlerts);
            layoutStartTimeGroup.setVisibility(View.VISIBLE);
            layoutFinishTimeGroup.setVisibility(View.VISIBLE);
            layoutToggleAlerts.setVisibility(View.VISIBLE);
            buttonDayNext.setVisibility(View.VISIBLE);
            setCheckBoxes();
        }
    }

    private void nextPage(){
        if(mSettings.getFirstRun()){
            AlarmMan.getInstance(this).setDateChangeAlarm();
        }
        mSettings.setFirstRun(false);
        mSettings.setActiveDays(mActiveDays);
        mSettings.setTimes(mTimes);
        mSettings.setAlertsOn(mToggleAlerts);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        setFirstAlarms();
    }

    private void setFirstAlarms(){
        AlarmMan.getInstance(this).newDay();
        AlarmMan.getInstance(this).chooseAlarmType();
    }

    private void setCheckBoxes(){
        for(int i = 0; i < 7; i++){
            if(mActiveDays[i]){
                ((CheckBox) findViewById(mCheckBoxIds[i])).setChecked(true);
            }
        }
    }

    public void timePicked(int hourOfDay, int minute) {
        switch (mTimerUsed){
            case "startTime":
                mTimes[0] = hourOfDay + ":" + minute;
                try {
                    if(mDateFormat.parse(mTimes[1]).after(mDateFormat.parse(mTimes[0]))){
                        buttonTimeStart.setText("Start at " +hourOfDay+":"+String.format("%02d",minute));
                        layoutFinishTimeGroup.setVisibility(View.VISIBLE);
                    } else {
                        buttonTimeStart.setText("Start");
                        Toast.makeText(this, "Start time must be before finish time.", Toast.LENGTH_LONG).show();
                        buttonDayNext.setVisibility(View.INVISIBLE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "endTime":
                mTimes[1] = hourOfDay + ":" + minute;
                try {
                    if(mDateFormat.parse(mTimes[1]).after(mDateFormat.parse(mTimes[0]))){
                        buttonTimeFinish.setText("Finish at " +hourOfDay+":"+String.format("%02d",minute));
                        buttonDayNext.setVisibility(View.VISIBLE);
                        layoutToggleAlerts.setVisibility(View.VISIBLE);
                    } else {
                        buttonTimeFinish.setText("Finish");
                        Toast.makeText(this, "Finish time must be after start time.", Toast.LENGTH_LONG).show();
                        buttonDayNext.setVisibility(View.INVISIBLE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        layoutStartTimeGroup.setVisibility(View.VISIBLE);

        for(int i = 0; i < 7; i++){
            if(mCheckBoxIds[i] == view.getId()){
                mActiveDays[i] = (checked) ? true : false;
            }
        }
    }
}