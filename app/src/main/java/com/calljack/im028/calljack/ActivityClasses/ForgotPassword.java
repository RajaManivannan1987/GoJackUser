package com.calljack.im028.calljack.ActivityClasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Validation;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by IM0033 on 8/2/2016.
 */
public class ForgotPassword extends BackCommonActivity {
    private String TAG = "ForgotPassword";
    SeekBar mySeek;
    Button forgotButton;
    private EditText userNameEditText;
    private WebServices webServices;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        webServices = new WebServices(this, TAG);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        // setActionBar();
        forgotButton = (Button) findViewById(R.id.forgotButton);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.emailPhoneValidation(userNameEditText.getText().toString()).equalsIgnoreCase("email") || Validation.emailPhoneValidation(userNameEditText.getText().toString()).equalsIgnoreCase("phone")) {
                    userNameEditText.setError(null);
                    ConstantFunctions.hideKeyboard(ForgotPassword.this, view);
                    final ProgressDialog progressBar = new ProgressDialog(ForgotPassword.this);
                    progressBar.setMessage("Waiting...");
                    progressBar.setCancelable(false);
                    progressBar.show();
                    webServices.forgotPassword(userNameEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            progressBar.dismiss();
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                startActivity(new Intent(getApplicationContext(), CodeConfirmation.class).putExtra("customerId", response.getString("userid")));
                                finish();
                            } else {
                                ConstantFunctions.toast(ForgotPassword.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            progressBar.dismiss();
                            ConstantFunctions.showSnakBar(message, userNameEditText);
                        }
                    });

                } else {
                    userNameEditText.requestFocus();
                    userNameEditText.setError(Validation.emailPhoneValidation(userNameEditText.getText().toString()));
                }

            }
        });
        mySeek = (SeekBar) findViewById(R.id.myseek);
        mySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress > 95) {
                    // seekBar.setThumb(getResources().getDrawable(R.drawable.accounts_menu_icon));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() > 95) {

                } else {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.about_menu_icon));
                }

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
