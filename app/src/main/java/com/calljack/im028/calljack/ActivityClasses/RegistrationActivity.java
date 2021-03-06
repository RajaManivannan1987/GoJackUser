package com.calljack.im028.calljack.ActivityClasses;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.calljack.im028.calljack.CommonActivityClasses.BackCommonActivity;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.AlertDialogManager;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterfaceClasses.VolleyResponseListerner;
import com.calljack.im028.calljack.Utility.Validation;
import com.calljack.im028.calljack.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegistrationActivity extends BackCommonActivity {
    private static String TAG = "RegistrationActivity";
    private TextInputEditText  nameTextInputEditText, emailTextInputEditText, mobileTextInputEditText, passwordTextInputEditText, referralTextInputEditText;
    private RadioButton femaleRadioButton, maleRadioButton;
    private CheckBox checkBox;
    private Button submitButton;
    // private DatePickerDialog dobDatePickerDialog;
    private Calendar calendar;
    TextView termsTextView;
    private WebServices webServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_registration);
        webServices = new WebServices(this, TAG);
        calendar = Calendar.getInstance();
        termsTextView = (TextView) findViewById(R.id.registerTermsTextView);
        nameTextInputEditText = (TextInputEditText) findViewById(R.id.registrationActivityNameEditText);
        emailTextInputEditText = (TextInputEditText) findViewById(R.id.registrationActivityEmailAddressEditText);
        mobileTextInputEditText = (TextInputEditText) findViewById(R.id.registrationActivityMobileNumberEditText);
        passwordTextInputEditText = (TextInputEditText) findViewById(R.id.registrationActivityPasswordEditText);
//        dobTextInputEditText = (TextInputEditText) findViewById(R.id.registrationActivityDOBEditText);
        referralTextInputEditText = (TextInputEditText) findViewById(R.id.registrationActivityReferralEditText);
        femaleRadioButton = (RadioButton) findViewById(R.id.registrationActivityFemaleRadioButton);
        maleRadioButton = (RadioButton) findViewById(R.id.registrationActivityMaleRadioButton);
        checkBox = (CheckBox) findViewById(R.id.registrationActivityAgreeCheckbox);
        submitButton = (Button) findViewById(R.id.registrationActivitySubmitButton);
       /* dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(year, monthOfYear, dayOfMonth);
                dobTextInputEditText.setText(ConstantValues.dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR)-13, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
*/
      /*  dobTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dobDatePickerDialog.show();
            }
        });*/
        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, TermsActivity.class));
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameTextInputEditText.getText().toString().trim().length() > 3) {
                    nameTextInputEditText.setError(null);
                    if (Validation.isUserNameValid(emailTextInputEditText.getText().toString())) {
                        emailTextInputEditText.setError(null);
                        if (Validation.isMobileNumberValid(mobileTextInputEditText.getText().toString())) {
                            mobileTextInputEditText.setError(null);
                            if (Validation.isPasswordValid(passwordTextInputEditText.getText().toString())) {
                                passwordTextInputEditText.setError(null);
//                                if (dobTextInputEditText.getText().toString().length() > 0) {
//                                    dobTextInputEditText.setError(null);
                                if (maleRadioButton.isChecked() || femaleRadioButton.isChecked()) {
                                    if (checkBox.isChecked()) {
                                        submit();
                                    } else {
                                        ConstantFunctions.toast(RegistrationActivity.this, "Please agree to CallJack terms");
//                                            checkBox.setError("Please agree gojack terms");
                                    }
                                } else {
                                    ConstantFunctions.toast(RegistrationActivity.this, "Select gender");
                                }
//                                } else {
//                                    dobTextInputEditText.setError("Select date of birth");
//                                }
                            } else {
                                passwordTextInputEditText.setError("Password must be greater than or equal to 6 characters");
                                passwordTextInputEditText.requestFocus();
                            }
                        } else {
                            mobileTextInputEditText.setError("Enter a valid phone number");
                            mobileTextInputEditText.requestFocus();
                        }
                    } else {
                        emailTextInputEditText.setError("Enter a valid email ID");
                        emailTextInputEditText.requestFocus();
                    }
                } else {
                    nameTextInputEditText.setError("Enter your name");
                    nameTextInputEditText.requestFocus();
                }
            }
        });

    }

    private void submit() {
        webServices.registration(nameTextInputEditText.getText().toString().trim(),
                "",
                emailTextInputEditText.getText().toString().trim(),
                passwordTextInputEditText.getText().toString(),
                (maleRadioButton.isChecked()) ? "male" : "female",
//                ConstantValues.serverFormat.format(calendar.getTime()),
                mobileTextInputEditText.getText().toString().trim(),
                referralTextInputEditText.getText().toString().trim(),
                new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        switch (response.getString("status")) {
                            case "1":
                                mobileTextInputEditText.setError(response.getString("message"));
                                mobileTextInputEditText.requestFocus();
                                break;
                            case "2":
                                emailTextInputEditText.setError(response.getString("message"));
                                emailTextInputEditText.requestFocus();
                                break;
                            case "3":
                                referralTextInputEditText.setError(response.getString("message"));
                                referralTextInputEditText.requestFocus();
                                break;
                            case "4":
                                startActivity(new Intent(RegistrationActivity.this, OtpActivity.class).putExtra(ConstantValues.customerId, response.getJSONObject("data").getString("customer_id")));
                                break;
                            case "5":
                                AlertDialogManager.alertBox(RegistrationActivity.this, "Registration!", response.getString("message"));
                                break;
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        ConstantFunctions.showSnakBar(message, emailTextInputEditText);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication.getInstance().setConnectivityListener(this);
    }
}
