package com.example.im028.gojackuser.ActivityClasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.GCMClass.RegistrationIntentService;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Session;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpActivity extends BackCommonActivity {
    private static String TAG = "OtpActivity";
    private TextInputEditText pinTextInputEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_otp);
        pinTextInputEditText = (TextInputEditText) findViewById(R.id.otpActivityNameEditText);
        submitButton = (Button) findViewById(R.id.otpActivitySubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinTextInputEditText.getText().toString().length() == 4) {
                    pinTextInputEditText.setError(null);
                    final ProgressDialog progressBar = new ProgressDialog(OtpActivity.this);
                    progressBar.setMessage("Waiting...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    new WebServices(OtpActivity.this, TAG).verifyPIN(getIntent().getExtras().getString(ConstantValues.customerId, ""), pinTextInputEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            progressBar.dismiss();
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                new Session(OtpActivity.this, TAG).createSession(response.getJSONObject("data").getString("customerid"), response.getJSONObject("data").getString("name"), response.getJSONObject("data").getString("token"));
                                ConstantFunctions.toast(OtpActivity.this, response.getString("message"));
                                startActivity(new Intent(OtpActivity.this, ChooseTypeActivity.class));
                                finish();
                                startService(new Intent(OtpActivity.this, RegistrationIntentService.class));
                            } else {
                                ConstantFunctions.toast(OtpActivity.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            progressBar.dismiss();
                            AlertDialogManager.showAlertDialog(OtpActivity.this, title, message, false);
                        }
                    });
                } else {
                    pinTextInputEditText.setError("Enter 4 digit PIN");
                    pinTextInputEditText.requestFocus();
                }
            }
        });
    }
}
