package com.example.im028.gojackuser.ActivityClasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;

import com.example.im028.gojackuser.GCMClass.RegistrationIntentService;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Session;
import com.example.im028.gojackuser.Utility.Validation;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class ChangePassword extends BackCommonActivity {
    private EditText newPasswordEditText, confirmNewPasswordEditText;
    private String TAG = "ForgotPassword";
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
//        setActionBar();
        userId = getIntent().getExtras().getString("customerId");
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = (EditText) findViewById(R.id.confirmNewPasswordEditText);
        findViewById(R.id.changePasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordValidate();
            }
        });
    }

    private void setPasswordValidate() {
        if (Validation.isPasswordValid(newPasswordEditText.getText().toString())) {
            newPasswordEditText.setError(null);
            if (Validation.isPasswordValid(confirmNewPasswordEditText.getText().toString())) {
                confirmNewPasswordEditText.setError(null);
                if (newPasswordEditText.getText().toString().startsWith(confirmNewPasswordEditText.getText().toString())) {
                    final ProgressDialog progressBar = new ProgressDialog(ChangePassword.this);
                    progressBar.setMessage("Waiting...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    new WebServices(ChangePassword.this, TAG).changePassword(userId, newPasswordEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            progressBar.dismiss();
                            new Session(ChangePassword.this, TAG).createSession(response.getString("customerid"), response.getString("name"), response.getString("token"));
                            startActivity(new Intent(ChangePassword.this, ChooseTypeActivity.class));
                            finish();
                            startService(new Intent(ChangePassword.this, RegistrationIntentService.class));
                        }

                        @Override
                        public void onError(String message, String title) {
                            progressBar.dismiss();
                            AlertDialogManager.showAlertDialog(ChangePassword.this, title, message, false);
                        }
                    });

                } else {
                    confirmNewPasswordEditText.setError("Password mismatch");
                    confirmNewPasswordEditText.requestFocus();
                }
            } else {
                confirmNewPasswordEditText.requestFocus();
                confirmNewPasswordEditText.setError(Validation.passwordError);
            }
        } else {
            newPasswordEditText.requestFocus();
            newPasswordEditText.setError(Validation.passwordError);
        }
    }
}
