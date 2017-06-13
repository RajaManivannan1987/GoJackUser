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

/**
 * Created by Im033 on 12/21/2016.
 */

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

        //



//         for staging
        PaytmPGService Service = PaytmPGService.getStagingService();
//         for production
//        PaytmPGService Service = PaytmPGService.getProductionService();

        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "CallJa65607497328098");
        paramMap.put("REQUEST_TYPE", "ADD_MONEY");
        paramMap.put( "ORDER_ID" , ((EditText) findViewById(R.id.order_id)).getText().toString());
        paramMap.put( "CUST_ID" , new Session(MerchantActivity.this,"Paytm").getCustomerId());
        paramMap.put( "TXN_AMOUNT" , "1");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "INDUSTRY_TYPE_ID" , "Recharge");
        paramMap.put( "WEBSITE" , "APP_STAGING");
        paramMap.put( "SSO_TOKEN" , "APP_STAGING");
//        paramMap.put( "CALLBACK_URL" , "https://www.paytm.com");
//        paramMap.put( "EMAIL" , "rajamcaarg@gmail.com");
//        paramMap.put( "MOBILE_NO" , "9865132365");
        paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1/BNdEnJEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
//        CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
//        String checksum = checksumHelper.genrateCheckSum(ConstantValues.paytmMID, paramMap);


        /*Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "CallJa65607497328098");
        paramMap.put("REQUEST_TYPE", "ADD_MONEY");
        paramMap.put("ORDER_ID", ((EditText) findViewById(R.id.order_id)).getText().toString());
        paramMap.put("CUST_ID", new Session(MerchantActivity.this,"Paytm").getCustomerId());
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Recharge");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("THEME", ((EditText) findViewById(R.id.theme)).getText().toString());
        paramMap.put("EMAIL", " jacobdavid.mathew@gmail.com");
        paramMap.put("MOBILE_NO", "7777777777");*/
        Log.d("MerchantActivity", paramMap.toString());
        PaytmOrder Order = new PaytmOrder(paramMap);

        final PaytmMerchant Merchant = new PaytmMerchant(
               /* "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

             /*   "https://steptest.in/demo/lib/generateChecksum.php",
                "https://steptest.in/demo/lib/verifyChecksum.php");
*/

                "http://imaginetventures.me/sample/steptest/generateChecksum.php",
                "http://imaginetventures.me/sample/steptest/verifyChecksum.php");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionSuccess(Bundle inResponse) {
                Log.d("MerchantActivity", "Payment Transaction is successful " + inResponse);
                Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionFailure(String s, Bundle inErrorMessage) {
                Log.d("MerchantActivity", "Payment Transaction Failed " + inErrorMessage);
                ConstantFunctions.toast(MerchantActivity.this, "onErrorLoadingWebPage " + s);
            }

            @Override
            public void networkNotAvailable() {
                ConstantFunctions.toast(MerchantActivity.this, "networkNotAvailable");
                Log.d("LOG", "Payment networkNotAvailable ");
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
        });
    }

}
