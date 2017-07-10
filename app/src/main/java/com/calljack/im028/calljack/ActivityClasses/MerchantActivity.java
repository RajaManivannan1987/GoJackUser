package com.calljack.im028.calljack.ActivityClasses;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.Session;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static android.R.attr.key;




public class MerchantActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytm);
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
        orderIdEditText.setText(orderId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void onStartTransaction(View view) {


        PaytmPGService Service = PaytmPGService.getStagingService();
//        PaytmPGService Service = PaytmPGService.getProductionService();

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("REQUEST_TYPE", "ADD_MONEY");
        paramMap.put("MID", "CallJa65607497328098");
        paramMap.put("ORDER_ID", "ranjeet190049");
        paramMap.put("CUST_ID", "cust1900");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("SSO_TOKEN", "c70d5601-c1a5-4a21-92ea-fb73e708292f");
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("CHECKSUMHASH", "23/CuVwBwNFcp75HaO3bIOLSnuWYG8GYbbt0cZ/Cb7eUB8ZRzB24p+zYxNJFgM0sTdH7n63UvvIhz67DCHUb65Jo5CtpNkuUueOGJls1mgw=");
        //    paramMap.put("EMAIL", "rajamcaarg@gmail.com");
        //  paramMap.put("MOBILE_NO", "9865132365");
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {

            }

            @Override
            public void networkNotAvailable() {
                ConstantFunctions.toast(MerchantActivity.this, "networkNotAvailable");
                Log.d("MerchantActivity", "Payment networkNotAvailable ");
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                ConstantFunctions.toast(MerchantActivity.this, "networkNotAvailable " + s);
                Log.d("MerchantActivity", "Payment clientAuthenticationFailed " + s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.d("MerchantActivity", "Payment someUIErrorOccurred " + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                ConstantFunctions.toast(MerchantActivity.this, "onErrorLoadingWebPage " + s);
                Log.d("MerchantActivity", "Payment onErrorLoadingWebPage " + s + " onErrorLoadingWebPage" + s1);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.d("MerchantActivity", "Payment onBackPressedCancelTransaction");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {

            }
        });


    }}



