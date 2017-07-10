package com.calljack.im028.calljack.ActivityClasses;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Im033 on 7/6/2017.
 */

public class PaytmAddMoneyActivity extends BackCommonActivity {
    private String TAG = "PaytmAddMoneyActivity";
    private String orderId, checksums = "";
    private EditText amountEditText;
    private TextView addMoneyButton, balanceEditText, rs100Text, rs200Text, rs500Text;
    private Session session;
    private int amountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_add_money);
        session = new Session(PaytmAddMoneyActivity.this, TAG);

        findViewById();

        checkBalance(session.getPaytmtoken());

        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amountEditText.getText().toString().equalsIgnoreCase("")) {
                    amountValue = Integer.parseInt(amountEditText.getText().toString());
//                    if (amountValue >= 150) {
                        if (amountValue >= 10) {
                        generateChecksum();
                    } else {
                        Toast.makeText(PaytmAddMoneyActivity.this, "Add a minimum of Rs.150 to wallet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PaytmAddMoneyActivity.this, "Enter amount", Toast.LENGTH_LONG).show();
                }

            }
        });
        rs100Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountValue = Integer.parseInt(amountEditText.getText().toString());
                amountValue = amountValue + 100;
                amountEditText.setText(String.valueOf(amountValue));
            }
        });
        rs200Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountValue = Integer.parseInt(amountEditText.getText().toString());
                amountValue = amountValue + 200;
                amountEditText.setText(String.valueOf(amountValue));
            }
        });
        rs500Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountValue = Integer.parseInt(amountEditText.getText().toString());
                amountValue = amountValue + 500;
                amountEditText.setText(String.valueOf(amountValue));
            }
        });
    }

    private void findViewById() {
        balanceEditText = (TextView) findViewById(R.id.paytmWalletBalanceEditText);

        addMoneyButton = (TextView) findViewById(R.id.addmoneyButton);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        rs100Text = (TextView) findViewById(R.id.rs100Text);
        rs200Text = (TextView) findViewById(R.id.rs200Text);
        rs500Text = (TextView) findViewById(R.id.rs500Text);
    }

    private void checkBalance(final String access_token) {
        new WebServices(PaytmAddMoneyActivity.this, TAG).checkBalance(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                balanceEditText.setText("Rs " + response.getJSONObject("response").getString("amount"));
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public void addBalance(int amount, String checksums) {

        PaytmPGService Service = PaytmPGService.getStagingService();
//        PaytmPGService Service = PaytmPGService.getProductionService();

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "CallJa65607497328098");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", session.getCustomerId());
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", String.valueOf(amount));
//        paramMap.put("TXN_AMOUNT", "10");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("CALLBACK_URL", ConstantValues.ADDMONET_CALLBACKURL);
        paramMap.put("REQUEST_TYPE", ConstantValues.ADDMONET_RQUESTTYPE);
        paramMap.put("SSO_TOKEN", session.getPaytmtoken());
        paramMap.put("CHECKSUMHASH", checksums);
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {
//                checkBalance(session.getPaytmtoken());
                finish();
                Log.d(TAG, "Payment Transaction : " + inResponse);
                Toast.makeText(getApplicationContext(), "Payment Transaction Successful", Toast.LENGTH_LONG).show();
            }

            @Override
            public void networkNotAvailable() {
                Log.d(TAG, "networkNotAvailable ");
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.d(TAG, "clientAuthenticationFailed " + s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.d(TAG, "someUIErrorOccurred " + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.d(TAG, "onErrorLoadingWebPage " + s);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.d(TAG, "onBackPressedCancelTransaction");
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle bundle) {
                Log.d(TAG, "Payment Transaction Failed " + inErrorMessage);
                Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateChecksum() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "dialjackOrder_Id" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(100);

        new WebServices(PaytmAddMoneyActivity.this, TAG).generateAddmoneyChecksum(orderId, session.getCustomerId(), amountEditText.getText().toString(), ConstantValues.ADDMONET_RQUESTTYPE, session.getPaytmtoken(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                String checksumgenerate = response.getString("CHECKSUMHASH");
                checksums = checksumgenerate.replaceAll("\\\\", "");
                addBalance(amountValue, checksums);
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}
