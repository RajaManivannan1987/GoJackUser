package com.calljack.im028.calljack.CommonActivityClasses;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.calljack.im028.calljack.ApplicationClass.MyApplication;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.InterNet.ConnectivityReceiver;

/**
 * Created by IM028 on 8/2/16.
 */
public class BackCommonActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private ImageView menuImageView;
    private FrameLayout backActivityFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_back_activity_layout);


        toolbar = (Toolbar) findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        backActivityFrameLayout = (FrameLayout) findViewById(R.id.backActivityFrameLayout);
        menuImageView = (ImageView) findViewById(R.id.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantFunctions.hideKeyboard(BackCommonActivity.this, v);
                onBackPressed();
            }
        });
        /*menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

    }

    public void setView(int viewLayout) {
        frameLayout = (FrameLayout) findViewById(R.id.commonMenuActivityFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        frameLayout.addView(activityView);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = null;
        int color = 0;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
            color = Color.WHITE;
        } else {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        }
        Snackbar snackbar = Snackbar.make(backActivityFrameLayout, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }
}
