package com.calljack.im028.calljack.ActivityClasses;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaytmOtpActivity extends BackCommonActivity {
    private String TAG = "PaytmOtpActivity";
    private EditText paytmotpEditText;
    private Button paytmotpButton;
    private String state;


    private WebServices webServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_paytm_otp);

        webServices = new WebServices(PaytmOtpActivity.this, TAG);

        state = getIntent().getExtras().getString("paytmstate");
        paytmotpButton = (Button) findViewById(R.id.paytmotpButton);
        paytmotpEditText = (EditText) findViewById(R.id.paytmotpEditText);
        paytmotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paytmotpEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    paytmLogin();
                } else {
                    paytmotpEditText.setError("Enter otp");
                    paytmotpEditText.requestFocus();
                }
            }
        });
    }


    private void paytmLogin() {
        webServices.verifyPaytmOTP(paytmotpEditText.getText().toString().trim(), state, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                updatePaytmToken(response.getString("access_token"));
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void updatePaytmToken(final String access_token) {
        webServices.updatePaytmToken(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    new Session(PaytmOtpActivity.this, TAG).setPaytmtoken(access_token);
                    startActivity(new Intent(PaytmOtpActivity.this, PaymentMethodActivity.class).putExtra(ConstantValues.paymentType, "paytm"));
                    finish();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }


}

