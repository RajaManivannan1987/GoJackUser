package com.calljack.im028.calljack.ActivityClasses;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends BackCommonActivity {
    private String TAG = "PaymentActivity";
    private TextView paymentTypeBalanceTextView;
    private static float paytmBalance;
    private Button addMoneyButton;
    private Session session;
    private String orderId, checksums = "", IpAddess = "";
    private WebServices webServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_payment_type);
        session = new Session(PaymentActivity.this, TAG);
        webServices = new WebServices(PaymentActivity.this, TAG);

        paymentTypeBalanceTextView = (TextView) findViewById(R.id.paymentTypeBalanceTextView);
        addMoneyButton = (Button) findViewById(R.id.paymentmethodAddMoneyButton);
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, PaytmAddMoneyActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!session.getPaytmtoken().equalsIgnoreCase("")) {
            checkBalance(session.getPaytmtoken());
        }
    }

    private void checkBalance(final String access_token) {
        new WebServices(PaymentActivity.this, TAG).checkBalance(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                paytmBalance = Float.parseFloat(response.getJSONObject("response").getString("amount"));
                paymentTypeBalanceTextView.setText("Balance : Rs " + response.getJSONObject("response").getString("amount"));
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }


}

