package com.example.im028.gojackuser.ActivityClasses;

import android.os.Bundle;

import com.example.im028.gojackuser.ApplicationClass.MyApplication;
import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.CommonActivityClasses.MenuCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.InterNet.ConnectivityReceiver;

/**
 * Created by Im033 on 2/15/2017.
 */
public class AboutActivity extends BackCommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_about);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
