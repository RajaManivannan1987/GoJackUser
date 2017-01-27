package com.example.im028.gojackuser.ActivityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        long now = System.currentTimeMillis() - 1000;

        datePicker.setMinDate(now);
        datePicker.setMaxDate(now + (1000 * 60 * 60 * 24 * 7));
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        dateTimeSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strdateTime = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date1 = null;
                Date date2 = null;

                try {
                    date1 = format.parse(format.format(new Date()));
                    date2 = format.parse(strdateTime);

                    long diff = date2.getTime() - date1.getTime();
                    long diffHours = diff / (60 * 60 * 1000) % 24;

                    if (diffHours >= 1) {
                        String strdateTime1 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                        setResult(RESULT_OK, getIntent().putExtra(ConstantValues.scheduleDateTime, strdateTime));
                        setResult(RESULT_OK, getIntent().putExtra(ConstantValues.scheduleDateTime1, strdateTime1));
                        finish();
                    } else {
                        ConstantFunctions.toast(ScheduleActivity.this, "Please select after one hour from current time");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
