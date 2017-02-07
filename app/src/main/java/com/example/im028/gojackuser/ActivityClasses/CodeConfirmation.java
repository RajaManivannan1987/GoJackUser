package com.example.im028.gojackuser.ActivityClasses;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.ImcomeSmsReceiver;
import com.example.im028.gojackuser.Utility.InterfaceClasses.ImComeSms;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Validation;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class CodeConfirmation extends BackCommonActivity implements View.OnClickListener{
    private EditText userNameEditText;
    private String TAG = "CodeConfirmation";
    String userId;
    Button codeSendButton,otpButton;
    private WebServices webServices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_confirmation);
        webServices = new WebServices(CodeConfirmation.this, TAG);
        codeSendButton = (Button) findViewById(R.id.codeConirmationButton);
        otpButton= (Button) findViewById(R.id.otpButton);
//        otpButton.setOnClickListener(this);
//        codeSendButton.setOnClickListener(this);
        userId = getIntent().getExtras().getString("customerId");
        userNameEditText = (EditText) findViewById(R.id.otpEditText);
        // setActionBar();
     /*   ImcomeSmsReceiver.bindMessageListener(new ImComeSms() {
            @Override
            public void messageReceived(String messageText) {
                userNameEditText.setText(messageText);
            }
        });*/
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ConstantFunctions.toast(CodeConfirmation.this, "Hai");
                if (Validation.isOtpValid(userNameEditText.getText().toString())) {
                    userNameEditText.setError(null);
                    webServices.validateOtp(userId, userNameEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                startActivity(new Intent(getApplicationContext(), ChangePassword.class).putExtra("customerId", response.getString("userid")));
                            } else {
                                ConstantFunctions.toast(CodeConfirmation.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {

                        }
                    });

                } else {
                    userNameEditText.setError(Validation.otpError);
                    userNameEditText.requestFocus();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
//        if (!ConstantFunctions.checkmarshmallowPermission(CodeConfirmation.this, Manifest.permission.RECEIVE_SMS, ConstantValues.MY_PERMISSIONS_REQUEST_LOCATION)) {
//            //CommonMethods.toast(CodeConfirmation.this,"Enable");
//        }
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
    public void onClick(View view) {
        ConstantFunctions.toast(CodeConfirmation.this, "Hai");
        if (Validation.isOtpValid(userNameEditText.getText().toString())) {
            userNameEditText.setError(null);
            webServices.validateOtp(userId, userNameEditText.getText().toString(), new VolleyResponseListerner() {
                @Override
                public void onResponse(JSONObject response) throws JSONException {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        startActivity(new Intent(getApplicationContext(), ChangePassword.class).putExtra("customerId", response.getString("userid")));
                    } else {
                        ConstantFunctions.toast(CodeConfirmation.this, response.getString("message"));
                    }
                }

                @Override
                public void onError(String message, String title) {

                }
            });

        } else {
            userNameEditText.setError(Validation.otpError);
            userNameEditText.requestFocus();
        }
    }
}
