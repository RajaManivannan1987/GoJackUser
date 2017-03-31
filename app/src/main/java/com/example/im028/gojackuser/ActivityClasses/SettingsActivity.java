package com.example.im028.gojackuser.ActivityClasses;

import android.os.Bundle;
import android.view.View;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.Session;

/**
 * Created by Im033 on 3/15/2017.
 */

public class SettingsActivity extends BackCommonActivity {
    private String TAG="SettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Session(SettingsActivity.this, TAG).logout();
                ConstantFunctions.logout(SettingsActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
