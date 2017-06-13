package com.calljack.im028.calljack.ActivityClasses;

import android.os.Bundle;

import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.R;

/**
 * Created by Im033 on 2/15/2017.
 */
public class AboutActivity extends MenuCommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_about);
        setTitle("About");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
