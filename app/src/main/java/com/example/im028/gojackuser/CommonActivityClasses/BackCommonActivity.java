package com.example.im028.gojackuser.CommonActivityClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.im028.gojackuser.R;

/**
 * Created by IM028 on 8/2/16.
 */
public class BackCommonActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private ImageView menuImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_back_activity_layout);


        toolbar = (Toolbar) findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menuImageView = (ImageView) findViewById(R.id.menu);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void setView(int viewLayout) {
        frameLayout = (FrameLayout) findViewById(R.id.commonMenuActivityFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        frameLayout.addView(activityView);
    }
}
