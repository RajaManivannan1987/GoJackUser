package com.example.im028.gojackuser.ActivityClasses;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.im028.gojackuser.GCMClass.RegistrationIntentService;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Session;
import com.example.im028.gojackuser.Utility.Validation;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputEditText userNameEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotTextView;
    private WebServices webServices;

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
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

    }

    private void loginValidate() {
        if (Validation.isUserNameValid(userNameEditText.getText().toString())) {
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
        }
    }

    private void login() {
        webServices.login(userNameEditText.getText().toString(), passwordEditText.getText().toString(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    if (response.getJSONObject("data").getString("mobileno_verify").equalsIgnoreCase("no")) {
                        startActivity(new Intent(LoginActivity.this, OtpActivity.class).putExtra(ConstantValues.customerId, response.getJSONObject("data").getString("customerid")));
                    } else {
                        new Session(LoginActivity.this, TAG).createSession(response.getJSONObject("data").getString("customerid"), response.getJSONObject("data").getString("name"), response.getJSONObject("data").getString("token"));
                        ConstantFunctions.toast(LoginActivity.this, response.getString("message"));
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
                AlertDialogManager.showAlertDialog(LoginActivity.this, title, message, false);
            }
        });
    }
}
