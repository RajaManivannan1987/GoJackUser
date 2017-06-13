package com.calljack.im028.calljack.ActivityClasses;

import android.os.Bundle;
import android.view.View;

import com.calljack.im028.calljack.CommonActivityClasses.MenuCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.paytm.pgsdk.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 3/15/2017.
 */

public class SettingsActivity extends MenuCommonActivity {
    private String TAG="SettingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        setTitle("Settings");
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
