package com.calljack.im028.calljack.ActivityClasses;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.paytm.pgsdk.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class PaytmOtpActivity extends BackCommonActivity {
    private EditText paytmotpEditText;
    private Button paytmotpButton;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_paytm_otp);
        state = getIntent().getExtras().getString("paytmstate");
        paytmotpButton = (Button) findViewById(R.id.paytmotpButton);
        paytmotpEditText = (EditText) findViewById(R.id.paytmotpEditText);
        paytmotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paytmotpEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    login();
                } else {
                    paytmotpEditText.setError("Enter otp");
                    paytmotpEditText.requestFocus();
                }
            }
        });
    }

    private void login() {
        new WebServices(PaytmOtpActivity.this, "Paytm").verifyPaytmOTP(paytmotpEditText.getText().toString().trim(), state, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                Log.d("Paytm", response.toString());
                String access_token = response.getString("access_token");
                String resourceOwnerId = response.getString("resourceOwnerId");
                String expires = response.getString("expires");
                checkBalance(access_token);
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void checkBalance(String access_token) {
        new WebServices(PaytmOtpActivity.this, "Paytm").checkBalance(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                Log.d("Paytm", response.toString());
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

}

