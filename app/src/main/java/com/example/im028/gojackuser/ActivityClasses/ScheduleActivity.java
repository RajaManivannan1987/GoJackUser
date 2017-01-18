package com.example.im028.gojackuser.ActivityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

/**
 * Created by Im033 on 12/28/2016.
 */

public class ScheduleActivity extends BackCommonActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button dateTimeSetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_schedule);
        dateTimeSetButton = (Button) findViewById(R.id.dateTimeSetButton);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis());
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        dateTimeSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strdateTime = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                String strdateTime1 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
//                Toast.makeText(getApplicationContext(), strdateTime, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, getIntent().putExtra(ConstantValues.scheduleDateTime, strdateTime));
                setResult(RESULT_OK, getIntent().putExtra(ConstantValues.scheduleDateTime1, strdateTime1));
                finish();
            }
        });
    }
}
