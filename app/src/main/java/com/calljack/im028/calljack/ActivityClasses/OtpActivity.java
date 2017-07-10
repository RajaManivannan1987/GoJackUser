package com.calljack.im028.calljack.ActivityClasses;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.GCMClass.RegistrationIntentService;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.ImcomeSmsReceiver;
import com.calljack.im028.calljack.Utility.InterfaceClasses.InComeSms;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpActivity extends BackCommonActivity {
    private static String TAG = "OtpActivity";
    private TextInputEditText pinTextInputEditText;
    private TextView resendOtpTextView;
    private Button submitButton;
    private String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_otp);
        customerID = getIntent().getExtras().getString(ConstantValues.customerId, "");
        pinTextInputEditText = (TextInputEditText) findViewById(R.id.otpActivityNameEditText);
        resendOtpTextView = (TextView) findViewById(R.id.resendOtpTextView);
        submitButton = (Button) findViewById(R.id.otpActivitySubmitButton);
        
        ImcomeSmsReceiver.bindMessageListener(new InComeSms() {
            @Override
            public void messageReceived(String messageText) {
                pinTextInputEditText.setText(messageText);
            }
        });
        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinTextInputEditText.setText("");
                ConstantFunctions.hideKeyboard(OtpActivity.this, v);
                new WebServices(OtpActivity.this, TAG).reSendOtp(customerID, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("status").equalsIgnoreCase("1")) {
                            ConstantFunctions.toast(OtpActivity.this, response.getString("message"));
                        }
                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinTextInputEditText.getText().toString().length() == 4) {
                    pinTextInputEditText.setError(null);
                    final ProgressDialog progressBar = new ProgressDialog(OtpActivity.this);
                    progressBar.setMessage("Waiting...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    new WebServices(OtpActivity.this, TAG).verifyPIN(customerID, pinTextInputEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            progressBar.dismiss();
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                new Session(OtpActivity.this, TAG).createSession(response.getJSONObject("data").getString("customerid"), response.getJSONObject("data").getString("name"), response.getJSONObject("data").getString("token"),response.getJSONObject("data").getString("paytmtoken"));
                                ConstantFunctions.toast(OtpActivity.this, response.getString("message"));
                                startActivity(new Intent(OtpActivity.this, ChooseTypeActivity.class));
                                startService(new Intent(OtpActivity.this, RegistrationIntentService.class));
                                finish();
                            } else {
                                ConstantFunctions.toast(OtpActivity.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            progressBar.dismiss();
                            ConstantFunctions.showSnakBar(message,pinTextInputEditText);
                        }
                    });
                } else {
                    pinTextInputEditText.setError("Enter 4 digit PIN");
                    pinTextInputEditText.requestFocus();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!ConstantFunctions.checkmarshmallowPermission(OtpActivity.this, Manifest.permission.RECEIVE_SMS, ConstantValues.MY_PERMISSIONS_REQUEST_LOCATION)) {
//            ConstantFunctions.toast(CodeConfirmation.this,"Enable");
        } else {
            ConstantFunctions.toast(OtpActivity.this, "Enable sms ");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantValues.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length != 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    onStart();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
