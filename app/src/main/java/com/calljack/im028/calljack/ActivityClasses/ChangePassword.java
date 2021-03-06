package com.calljack.im028.calljack.ActivityClasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;

import com.calljack.im028.calljack.GCMClass.RegistrationIntentService;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.AlertDialogManager;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.Validation;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class ChangePassword extends BackCommonActivity {
    private EditText newPasswordEditText, confirmNewPasswordEditText;
    private String TAG = "ForgotPassword";
    private TextInputLayout newPasswordTextInputLayout, confirmNewPasswordTextInputLayout;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
//        setActionBar();
        userId = getIntent().getExtras().getString("customerId");
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = (EditText) findViewById(R.id.confirmNewPasswordEditText);
        newPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.newPasswordTextInputLayout);
        confirmNewPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.confirmNewPasswordTextInputLayout);
        findViewById(R.id.changePasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordValidate();
            }
        });
    }

    private void setPasswordValidate() {
        if (Validation.isPasswordValid(newPasswordEditText.getText().toString())) {
//            newPasswordEditText.setError(null);
            newPasswordTextInputLayout.setError(null);
            if (Validation.isPasswordValid(confirmNewPasswordEditText.getText().toString())) {
//                confirmNewPasswordEditText.setError(null);
                confirmNewPasswordTextInputLayout.setError(null);
                if (newPasswordEditText.getText().toString().equalsIgnoreCase(confirmNewPasswordEditText.getText().toString())) {
                    confirmNewPasswordTextInputLayout.setError(null);
                    final ProgressDialog progressBar = new ProgressDialog(ChangePassword.this);
                    progressBar.setMessage("Waiting...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    new WebServices(ChangePassword.this, TAG).changePassword(userId, newPasswordEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            progressBar.dismiss();
                            new Session(ChangePassword.this, TAG).createSession(response.getString("customerid"), response.getString("name"), response.getString("token"),response.getString("paytmtoken"));
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
                    confirmNewPasswordTextInputLayout.setError("Password mismatch");
                    confirmNewPasswordEditText.requestFocus();
                }
            } else {
                confirmNewPasswordEditText.requestFocus();
                confirmNewPasswordTextInputLayout.setError(Validation.passwordError);
            }
        } else {
            newPasswordEditText.requestFocus();
            newPasswordTextInputLayout.setError(Validation.passwordError);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
