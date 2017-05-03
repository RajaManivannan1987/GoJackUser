package com.example.im028.gojackuser.ActivityClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;

public class ChooseTypeActivity extends MenuCommonActivity {
    private LinearLayout commuteLinearLayout, courierLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_choose_type);
        setView(R.layout.activity_choose_type);
        setTitle("Rides");

        commuteLinearLayout = (LinearLayout) findViewById(R.id.chooseTypeActivityCommuteLinearLayout);
        courierLinearLayout = (LinearLayout) findViewById(R.id.chooseTypeActivityCourierLinearLayout);
        commuteLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LocationCheckActivity.getLocationCheck(ChooseTypeActivity.this, ConstantValues.rideTypeRide));
            }
        });
        courierLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LocationCheckActivity.getLocationCheck(ChooseTypeActivity.this, ConstantValues.rideTypeCourier));
            }
        });
    }
}
