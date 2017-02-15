package com.example.im028.gojackuser.ActivityClasses;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.example.im028.gojackuser.CommonActivityClasses.BackCommonActivity;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.AlertDialogManager;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantValues;
import com.example.im028.gojackuser.Utility.InterfaceClasses.VolleyResponseListerner;
import com.example.im028.gojackuser.Utility.Validation;
import com.example.im028.gojackuser.Utility.WebServicesClasses.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegistrationActivity extends BackCommonActivity {
    private static String TAG = "RegistrationActivity";
    private TextInputEditText nameTextInputEditText, emailTextInputEditText, mobileTextInputEditText, passwordTextInputEditText, referralTextInputEditText;
    private RadioButton femaleRadioButton, maleRadioButton;
    private CheckBox checkBox;
    private Button submitButton;
   // private DatePickerDialog dobDatePickerDialog;
    private Calendar calendar;
    private WebServices webServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_registration);
        webServices = new WebServices(this, TAG);
        calendar = Calendar.getInstance();
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
                                            ConstantFunctions.toast(RegistrationActivity.this, "Please agree gojack terms");
//                                            checkBox.setError("Please agree gojack terms");
                                        }
                                    } else {
                                        ConstantFunctions.toast(RegistrationActivity.this, "Select gender");
                                    }
//                                } else {
//                                    dobTextInputEditText.setError("Select date of birth");
//                                }
                            } else {
                                passwordTextInputEditText.setError("Password must be greater than or equal to 6");
                                passwordTextInputEditText.requestFocus();
                            }
                        } else {
                            mobileTextInputEditText.setError("Enter the valid phone number");
                            mobileTextInputEditText.requestFocus();
                        }
                    } else {
                        emailTextInputEditText.setError("Enter the valid Email Id");
                        emailTextInputEditText.requestFocus();
                    }
                } else {
                    nameTextInputEditText.setError("Enter the name");
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
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        AlertDialogManager.showAlertDialog(RegistrationActivity.this, title, message, false);
                    }
                });
    }

}
