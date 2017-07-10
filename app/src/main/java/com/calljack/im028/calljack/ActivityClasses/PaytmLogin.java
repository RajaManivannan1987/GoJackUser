package com.calljack.im028.calljack.ActivityClasses;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Validation;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.paytm.pgsdk.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaytmLogin extends BackCommonActivity {
    private EditText paytmMobileNodEditText, paytmMailIdEditText;
    private Button paytmLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_paytm_login);
        paytmLoginButton = (Button) findViewById(R.id.paytmLoginButton);
        paytmMobileNodEditText = (EditText) findViewById(R.id.paytmMobileNodEditText);
        paytmMailIdEditText = (EditText) findViewById(R.id.paytmMailIdEditText);
        paytmLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paytmMobileNodEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!paytmMailIdEditText.getText().toString().trim().equalsIgnoreCase("")) {
                        login();
                    } else {
                        paytmMailIdEditText.setError(Validation.paytmEmailidError);
                        paytmMailIdEditText.requestFocus();
                    }
                } else {
                    paytmMobileNodEditText.setError(Validation.paytmMobiledError);
                    paytmMobileNodEditText.requestFocus();
                }
            }
        });
    }

    private void login() {
        new WebServices(PaytmLogin.this, "Paytm").SendOTP(paytmMobileNodEditText.getText().toString().trim(), paytmMailIdEditText.getText().toString().trim(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("SUCCESS")) {
                    startActivity(new Intent(PaytmLogin.this, PaytmOtpActivity.class).putExtra("paytmstate", response.getString("state")));
                    finish();
                } else {
                    Log.d("Paytm", response.toString());
                }


            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
