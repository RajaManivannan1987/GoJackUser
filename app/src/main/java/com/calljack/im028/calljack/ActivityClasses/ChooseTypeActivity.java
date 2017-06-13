package com.calljack.im028.calljack.ActivityClasses;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;

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
