package com.calljack.im028.calljack.ActivityClasses;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.calljack.im028.calljack.ApplicationClass.MyApplication;
import com.calljack.im028.calljack.GCMClass.RegistrationIntentService;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterNet.ConnectivityReceiver;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Session;
import com.calljack.im028.calljack.Utility.Validation;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "LoginActivity";
    private TextInputEditText userNameEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotTextView;
    private WebServices webServices;
    private TextInputLayout loginActivityPasswordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        webServices = new WebServices(this, TAG);

        userNameEditText = (TextInputEditText) findViewById(R.id.loginActivityUserNameEditText);
        passwordEditText = (TextInputEditText) findViewById(R.id.loginActivityPasswordEditText);
        loginButton = (Button) findViewById(R.id.loginActivityLoginButton);
        registerTextView = (TextView) findViewById(R.id.loginActivityRegisterTextView);
        forgotTextView = (TextView) findViewById(R.id.loginActivityForgotPasswordTextView);
        loginActivityPasswordInputLayout = (TextInputLayout) findViewById(R.id.loginActivityPasswordInputLayout);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidate();
            }
        });
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginValidate();
                }
                return false;
            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEditText.setText("");
                passwordEditText.setText("");
                ConstantFunctions.hideKeyboard(LoginActivity.this, v);
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
        forgotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameEditText.setText("");
                passwordEditText.setText("");
                ConstantFunctions.hideKeyboard(LoginActivity.this, view);
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });

    }

    private void loginValidate() {
        if (Validation.emailPhoneValidation(userNameEditText.getText().toString().trim()).equalsIgnoreCase("email") || Validation.emailPhoneValidation(userNameEditText.getText().toString().trim()).equalsIgnoreCase("phone")) {
            userNameEditText.setError(null);
            if (Validation.isPasswordEmpty(passwordEditText.getText().toString().trim())) {
                passwordEditText.setError(null);
                if (Validation.isPasswordValid(passwordEditText.getText().toString().trim())) {
                    passwordEditText.setError(null);
                    login();
                } else {
                    loginActivityPasswordInputLayout.setError(Validation.passwordError);
                    passwordEditText.requestFocus();
                }
            } else {
                loginActivityPasswordInputLayout.setError(Validation.passwordEmptyMessage);
                passwordEditText.requestFocus();
            }
        } else {
            userNameEditText.setError(Validation.emailPhoneValidation(userNameEditText.getText().toString()));
            userNameEditText.requestFocus();
        }

        // developed by karthik

        /*if (Validation.isUserNameValid1(userNameEditText.getText().toString())) {
            userNameEditText.setError(null);
            if (Validation.isPasswordValid(passwordEditText.getText().toString())) {
                passwordEditText.setError(null);
                login();
            } else {
                passwordEditText.setError(Validation.passwordError);
                passwordEditText.requestFocus();
            }
        } else {
            userNameEditText.setError(Validation.userNameError);
            userNameEditText.requestFocus();
        }*/
    }

    private void login() {
        webServices.login(userNameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    if (response.getJSONObject("data").getString("mobileno_verify").equalsIgnoreCase("no")) {
                        startActivity(new Intent(LoginActivity.this, OtpActivity.class).putExtra(ConstantValues.customerId, response.getJSONObject("data").getString("customerid")));
                    } else {
                        new Session(LoginActivity.this, TAG).createSession(response.getJSONObject("data").getString("customerid"), response.getJSONObject("data").getString("name"), response.getJSONObject("data").getString("token"),response.getJSONObject("data").getString("paytmtoken"));
                        ConstantFunctions.toast(LoginActivity.this, response.getString("message"));
                        userNameEditText.setText("");
                        passwordEditText.setText("");
                        startActivity(new Intent(LoginActivity.this, ChooseTypeActivity.class));
                        finish();
                        startService(new Intent(LoginActivity.this, RegistrationIntentService.class));
                    }
                } else {
                    ConstantFunctions.toast(LoginActivity.this, response.getString("message"));
                }
            }

            @Override
            public void onError(String message, String title) {
                ConstantFunctions.showSnakBar(message, userNameEditText);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = null;
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
        } else {
            message = "Good! Connected to Internet";
        }
        ConstantFunctions.showSnakBar(message, userNameEditText);
    }
}
