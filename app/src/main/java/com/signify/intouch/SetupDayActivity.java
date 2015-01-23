package com.signify.intouch;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.signify.intouch.data.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SetupDayActivity extends ActionBarActivity {

    private Button buttonTimeStart;
    private Button buttonTimeFinish;
    private Button buttonDayNext;
    private String timerUsed;

    private Settings mSettings;
    private String[] times;

    private RelativeLayout layoutStartTimeGroup;
    private RelativeLayout layoutFinishTimeGroup;

    private SimpleDateFormat dateFormat;

    private Boolean[] activeDays;

    private int[] checkBoxIds = new int[] {
            R.id.checkbox_monday,
            R.id.checkbox_tuesday,
            R.id.checkbox_wednesday,
            R.id.checkbox_thursday,
            R.id.checkbox_friday,
            R.id.checkbox_saturday,
            R.id.checkbox_sunday
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_day);

        dateFormat = new SimpleDateFormat("H:mm");


        mSettings = Settings.getInstance(this);

        layoutStartTimeGroup = (RelativeLayout)findViewById(R.id.layoutStartTimeGroup);
        layoutFinishTimeGroup = (RelativeLayout)findViewById(R.id.layoutFinishTimeGroup);

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
                timerUsed = "startTime";
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        buttonTimeFinish = (Button) findViewById(R.id.buttonTimeFinish);
        buttonTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerUsed = "endTime";
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        if(mSettings.getFirstRun()) {
            activeDays = new Boolean[]{false, false, false, false, false, false, false};
            times = new String[]{"00:00", "23:59"};
        } else {
            activeDays = mSettings.getActiveDays();
            times = mSettings.getTimes();
            buttonTimeStart.setText("Start at "+times[0]);
            buttonTimeFinish.setText("Finish at "+times[1]);
            layoutStartTimeGroup.setVisibility(View.VISIBLE);
            layoutFinishTimeGroup.setVisibility(View.VISIBLE);
            buttonDayNext.setVisibility(View.VISIBLE);
            setCheckBoxes();
        }
    }

    private void nextPage(){
        mSettings.setFirstRun(false);
        mSettings.setActiveDays(activeDays);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setCheckBoxes(){
        for(int i = 0; i < 7; i++){
            if(activeDays[i] == true){
                ((CheckBox) findViewById(checkBoxIds[i])).setChecked(true);
            }
        }
    }

    public void timePicked(int hourOfDay, int minute) {
        switch (timerUsed){
            case "startTime":
                times[0] = hourOfDay + ":" + minute;
                try {
                    if(dateFormat.parse(times[1]).after(dateFormat.parse(times[0]))){
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
                times[1] = hourOfDay + ":" + minute;
                try {
                    if(dateFormat.parse(times[1]).after(dateFormat.parse(times[0]))){
                        buttonTimeFinish.setText("Finish at " +hourOfDay+":"+String.format("%02d",minute));
                        buttonDayNext.setVisibility(View.VISIBLE);
                        mSettings.setTimes(times);
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
            if(checkBoxIds[i] == view.getId()){
                if(checked){
                    activeDays[i] = true;
                }else{
                    activeDays[i] = false;
                }
            }
        }
    }
}
