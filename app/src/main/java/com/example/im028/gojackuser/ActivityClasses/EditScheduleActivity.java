package com.example.im028.gojackuser.ActivityClasses;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Singleton.ActionCompletedSingleton;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Im033 on 12/28/2016.
 */

public class EditScheduleActivity extends BackCommonActivity {
    private String TAG = "EditScheduleActivity";
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button dateTimeSetButton;
    String scheduleId, dateValue, timeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_schedule);
        scheduleId = getIntent().getExtras().getString("scheduleId");
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
                        new WebServices(EditScheduleActivity.this, TAG).editSchedule(scheduleId, strdateTime, new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getString("status").equalsIgnoreCase("1")) {
                                    ConstantFunctions.toast(EditScheduleActivity.this, response.getString("message"));
                                    ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                                    finish();
                                } else {
                                    ConstantFunctions.toast(EditScheduleActivity.this, response.getString("message"));
                                }

                            }

                            @Override
                            public void onError(String message, String title) {

                            }
                        });
//                        String strdateTime1 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + " " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
//                        setResult(RESULT_OK, getIntent().putExtra(ConstantValues.scheduleDateTime, strdateTime));
//                        setResult(RESULT_OK, getIntent().putExtra(ConstantValues.scheduleDateTime1, strdateTime1));

                    } else {
                        ConstantFunctions.toast(EditScheduleActivity.this, "Please select after one hour from current time");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
